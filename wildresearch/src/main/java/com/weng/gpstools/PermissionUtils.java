package com.weng.gpstools;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
	public static final int REQUEST_CODE_STORAGE_PERMISSION = 100;
	public static final int REQUEST_CODE_MANAGE_STORAGE = 101;

	// 检查权限
	public static boolean checkPermissions(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			// Android 11及以上
			return Environment.isExternalStorageManager() ||
					(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED &&
							ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED &&
							ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED);
		}
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			// Android 6-10
			return ContextCompat.checkSelfPermission(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
					ContextCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
		}
		else {
			// Android 5.1及以下
			return true;
		}
	}

	// 请求权限
	public static void requestPermissions(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			try {
				Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("package:" + activity.getPackageName()));
				activity.startActivityForResult(intent, REQUEST_CODE_MANAGE_STORAGE);
			}
			catch (Exception e) {
				Intent intent = new Intent();
				intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
				activity.startActivityForResult(intent, REQUEST_CODE_MANAGE_STORAGE);
			}
		}
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			activity.requestPermissions(
					new String[]{
							android.Manifest.permission.READ_EXTERNAL_STORAGE,
							android.Manifest.permission.WRITE_EXTERNAL_STORAGE
					},
					REQUEST_CODE_STORAGE_PERMISSION
			);
		}
	}
}