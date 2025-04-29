package com.weng.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import com.weng.R;

import static android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION;

public class LocationTrackingService extends Service {
	private LocationManager locationManager;
	private LocationListener locationListener;
	private static final int NOTIFICATION_ID = 1;
	private static final String CHANNEL_ID = "LocationServiceChannel";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("TAG", "LocationService start 1");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
			// Android 14+ 必须指定类型
			Log.i("TAG", "LocationService start 2");
			Notification notification = createNotification();
			startForeground(NOTIFICATION_ID, notification, FOREGROUND_SERVICE_TYPE_LOCATION);
		} else {
			// 旧版本正常启动
			// 必须在前台服务启动后 5 秒内调用 startForeground()
			Log.i("TAG", "LocationService start 3");
			startForeground(NOTIFICATION_ID, createNotification());
		}
		initLocationUpdates();
		Log.i("TAG", "---------LocationService start ok！-----------");
	}

	private Notification createNotification() {
		// 创建通知渠道（Android 8.0+ 必需）
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationChannel channel = new NotificationChannel(
					CHANNEL_ID,
					"Location Tracking",
					NotificationManager.IMPORTANCE_LOW
			);
			getSystemService(NotificationManager.class).createNotificationChannel(channel);
		}

		return new NotificationCompat.Builder(this, CHANNEL_ID)
				.setContentTitle("位置追踪中")
				.setContentText("正在后台记录GPS数据")
				.setSmallIcon(R.drawable.icon1)
				.build();
	}

	private void initLocationUpdates() {
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				// 处理位置更新（如存储到数据库或发送到服务器）
				Log.i("MARK", "loc tracking service, Lat: " + location.getLatitude() + ", Lng: " + location.getLongitude());
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {}

			@Override
			public void onProviderEnabled(String provider) {}

			@Override
			public void onProviderDisabled(String provider) {}
		};

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			// 请求高精度位置更新（最小时间间隔5000ms，最小距离变化10m）
			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					5000,
					10,
					locationListener
			);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (locationManager != null && locationListener != null) {
			locationManager.removeUpdates(locationListener);
		}
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	// @Override
	// public int onStartCommand(Intent intent, int flags, int startId) {
	// 	return START_STICKY; // 确保 Service 被杀死后重启
	// }
}
