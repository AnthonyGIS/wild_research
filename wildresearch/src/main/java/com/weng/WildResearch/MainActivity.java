package com.weng.WildResearch;

import android.Manifest;
import android.app.Activity;
import android.app.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.weng.R;
import com.weng.services.LocationTrackingService;


public class MainActivity extends Activity implements View.OnClickListener {
	private static final int REQUEST_CODE_FGS_LOCATION = 1001;
	private static final int REQUEST_BACKGROUND_LOCATION = 1001;
	private LinearLayout newsLayout;
	private LinearLayout readLayout;
	private LinearLayout videoLayout;
	private LinearLayout lampLayout;
	private LinearLayout pcLayout;

	private Fragment[] mFragments;
	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("TAG", "start wild research main activity!");
		super.onCreate(savedInstanceState);
		checkPermissions_avoid_quite_before_start();		// 显示之前就应该申请权限，否则下面显示的主界面过程中，可能由于app原先设置了拒绝定位权限而界面没有显示出来就闪退了。
		setContentView(R.layout.activity_main);

		// others
		SetEvent();

		// 检查并请求定位权限
		checkAndRequestFineLocationPermissions();
	}


	private void checkPermissions_avoid_quite_before_start(){
		if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
			 ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
			// Android 10+ 需要单独申请后台定位权限
			// ACCESS_BACKGROUND_LOCATION要想授权成功，需要确保 「定位」权限 设置为 「始终允许」（而非「仅使用时允许」）。
			(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
			Log.i("TAG", "权限均满足!");
			return;
		}

		// 开启的权限设置为： 始终允许GPS使用、精定位（不开启，不会立即引起开app的直接闪退）、允许后台、文件存储(不文件导出，就非必须)
		Log.i("TAG", "权限不满足，需要开启!");
		openAutoStartSettings(); // 跳转魅族设置页
	}


	private void checkAndRequestFineLocationPermissions() {
		// 检查是否已授予前台定位权限
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Log.i("TAG", "gps精确定位未授权！");
			Toast.makeText(this, "GPS精确定位未授权, 下面操作将申请精确定位授权", Toast.LENGTH_SHORT).show();

			// 申请定位权限
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
				new AlertDialog.Builder(this)
						.setTitle("需要精确定位权限")
						.setMessage("我们需要获取您的精确米级位置信息，以有效且准确的记录运动轨迹！")
						.setPositiveButton("确定", (dialog, which) -> {
							ActivityCompat.requestPermissions(this, new String[]{
									Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);

						})
						.setNegativeButton("取消", null)
						.show();
			}
			else {
				// 未授权，请求权限
				ActivityCompat.requestPermissions(
						this,
						new String[]{
								Manifest.permission.ACCESS_FINE_LOCATION,
								Manifest.permission.ACCESS_COARSE_LOCATION
						},
						LOCATION_PERMISSION_REQUEST_CODE
				);
			}
		}



		// 开启定位后台
		// 已授权所有权限，可以启动定位服务
		// 已授权前台定位，再申请 FOREGROUND_SERVICE_LOCATION

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
				ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.FOREGROUND_SERVICE_LOCATION}, REQUEST_BACKGROUND_LOCATION);
		}

		Log.i("TAG", "LocationService start!");
		startLocationService();
	}


	/**
	 * 跳转系统默认应用设置页
	 */
	private void openDefaultAppSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.fromParts("package", getPackageName(), null));
		startActivity(intent);
	}

	/**
	 * 跳转魅族手机的自启动和后台权限设置页
	 */
	private void openMeizuAutoStartSettings() {
		try {
			// 尝试跳转魅族专属权限管理页
			Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
			intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
			intent.putExtra("packageName", getPackageName());
			startActivity(intent);
		} catch (Exception e) {
			// 跳转失败，改用通用设置页
			openDefaultAppSettings();
		}
	}


	private void openAutoStartSettings() {
		String manufacturer = Build.MANUFACTURER.toLowerCase();
		switch (manufacturer) {
			case "meizu":
				openMeizuAutoStartSettings();
				break;
			case "xiaomi":
				// 跳转小米自启动设置
				break;
			case "huawei":
				// 跳转华为后台启动管理
				break;
			default:
				openDefaultAppSettings();
		}
	}



	// 启动定位服务
	private void startLocationService() {
		Log.i("TAG", "LocationService start a");
		// 检查权限后启动服务
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			Intent serviceIntent = new Intent(this, LocationTrackingService.class);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				Log.i("TAG", "LocationService start b");
				startForegroundService(serviceIntent);
			} else {
				Log.i("TAG", "LocationService start c");
				startService(serviceIntent);
			}
		}
		else {
			Log.i("TAG", "LocationService start d");
			new AlertDialog.Builder(this)
					.setTitle("需要后台权限")
					.setMessage("请在设置中允许后台权限，以便连续的记录轨迹信息！")
					.setPositiveButton("设置", (dialog, which) -> {
						// 跳转到应用设置页
						Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
						intent.setData(Uri.fromParts("package", getPackageName(), null));
						startActivity(intent);
					})
					.setNegativeButton("取消", null)
					.show();
		}

	}


	private void SetEvent() {
		this.newsLayout = (LinearLayout) findViewById(R.id.bottom_status_layout);
		this.readLayout = (LinearLayout) findViewById(R.id.bottom_map_layout);
		this.videoLayout = (LinearLayout) findViewById(R.id.bottom_discovery_layout);
		this.lampLayout = (LinearLayout) findViewById(R.id.bottom_research_layout);
		this.pcLayout = (LinearLayout) findViewById(R.id.bottom_my_layout);
		this.newsLayout.setOnClickListener(this);
		this.readLayout.setOnClickListener(this);
		this.videoLayout.setOnClickListener(this);
		this.lampLayout.setOnClickListener(this);
		this.pcLayout.setOnClickListener(this);
		this.newsLayout.setSelected(true);
		mFragments = new Fragment[5];
		this.fragmentManager = this.getFragmentManager();
		mFragments[0] = fragmentManager.findFragmentById(R.id.fragement_status);
		mFragments[1] = fragmentManager.findFragmentById(R.id.fragement_map);
		mFragments[2] = fragmentManager.findFragmentById(R.id.fragement_discovery);
		mFragments[3] = fragmentManager.findFragmentById(R.id.fragement_research);
		mFragments[4] = fragmentManager.findFragmentById(R.id.fragement_my);
		fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);
		fragmentTransaction.show(mFragments[0]).commit();
	}


	public void onClick(View v) {
		fragmentTransaction = fragmentManager.beginTransaction().hide(mFragments[0]).hide(mFragments[1]).hide(mFragments[2]).hide(mFragments[3]).hide(mFragments[4]);

		int id = v.getId();
		if (id == R.id.bottom_status_layout) {
			this.newsLayout.setSelected(true);
			this.readLayout.setSelected(false);
			this.videoLayout.setSelected(false);
			this.lampLayout.setSelected(false);
			this.pcLayout.setSelected(false);
			fragmentTransaction.show(mFragments[0]).commit();
		}
		else if (id == R.id.bottom_map_layout) {
			this.newsLayout.setSelected(false);
			this.readLayout.setSelected(true);
			this.videoLayout.setSelected(false);
			this.lampLayout.setSelected(false);
			this.pcLayout.setSelected(false);
			fragmentTransaction.show(mFragments[1]).commit();
		}
		else if (id == R.id.bottom_discovery_layout) {
			this.newsLayout.setSelected(false);
			this.readLayout.setSelected(false);
			this.videoLayout.setSelected(true);
			this.lampLayout.setSelected(false);
			this.pcLayout.setSelected(false);
			fragmentTransaction.show(mFragments[2]).commit();
		}
		else if (id == R.id.bottom_research_layout) {
			this.newsLayout.setSelected(false);
			this.readLayout.setSelected(false);
			this.videoLayout.setSelected(false);
			this.lampLayout.setSelected(true);
			this.pcLayout.setSelected(false);
			fragmentTransaction.show(mFragments[3]).commit();
		}
		else if (id == R.id.bottom_my_layout) {
			this.newsLayout.setSelected(false);
			this.readLayout.setSelected(false);
			this.videoLayout.setSelected(false);
			this.lampLayout.setSelected(false);
			this.pcLayout.setSelected(true);
			fragmentTransaction.show(mFragments[4]).commit();
		}
	}
}
