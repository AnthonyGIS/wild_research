/*
Copyright 2012 fangqing.fan@hotmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.weng.gpstools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.weng.R;


import java.util.ArrayList;
import java.util.List;

public class DataListActivity extends Activity implements OnClickListener {

	private DataAdapter mDataAdapter;
	private List<Gps> mListGps;
	private ListView mListView;
	private DBUtil mDBUtil;

	private TextView pageInfo;
	private Button export;
	private Button back;
	private Button swith;
	private Button previousPage;
	private Button nextPage;

	private boolean dataSwitch;
	private int page = 0;
	private int pages = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.data_list);

		pageInfo = (TextView) findViewById(R.id.pageInfo);
		export = (Button) findViewById(R.id.export);
		back = (Button) findViewById(R.id.back);
		swith = (Button) findViewById(R.id.swith);
		previousPage = (Button) findViewById(R.id.previousPage);
		nextPage = (Button) findViewById(R.id.nextPage);
		export.setOnClickListener(this);
		back.setOnClickListener(this);
		swith.setOnClickListener(this);
		previousPage.setOnClickListener(this);
		nextPage.setOnClickListener(this);

		mListGps = new ArrayList<Gps>();
		mDBUtil = new DBUtil(this);
		mListView = (ListView) findViewById(R.id.data_list_items);
		updateListView(page);

		// 检查并请求权限
		if (!PermissionUtils.checkPermissions(this)) {
			PermissionUtils.requestPermissions(this);
		}
	}

	// 处理权限请求结果
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (requestCode == PermissionUtils.REQUEST_CODE_STORAGE_PERMISSION) {
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				// 权限已授予
			} else {
				// 权限被拒绝
			}
		}
	}

	// 处理MANAGE_EXTERNAL_STORAGE权限请求结果
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PermissionUtils.REQUEST_CODE_MANAGE_STORAGE) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				if (Environment.isExternalStorageManager()) {
					// 管理所有文件权限已授予
				} else {
					// 权限被拒绝
				}
			}
		}
	}


	private void updateListView(int page) {
		if (dataSwitch) {
			mListGps = mDBUtil.getGpsByPageNumber(page, mDBUtil.TAB_GPS_EDIT);
			pages = mDBUtil.getGpsDataCount(mDBUtil.TAB_GPS_EDIT);
		}
		else {
			mListGps = mDBUtil.getGpsByPageNumber(page, mDBUtil.TAB_GPS_AUTO);
			pages = mDBUtil.getGpsDataCount(mDBUtil.TAB_GPS_AUTO);
		}
		pageInfo.setText(page + 1 + "/" + (pages / Constant.PAGE_SIZE + 1));
		mDataAdapter = new DataAdapter(DataListActivity.this, mListGps, dataSwitch);
		mListView.setAdapter(mDataAdapter);
	}

	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.export) {
			new ExportaAsyncTask().execute();
		}
		else if (id == R.id.back) {
			this.finish();
		}
		else if (id == R.id.swith) {
			this.dataSwitch = !dataSwitch;
			page = 0;
			updateListView(page);
		}
		else if (id == R.id.previousPage) {
			if (page > 0)
				updateListView(--page);
		}
		else if (id == R.id.nextPage) {
			if (page < (pages / Constant.PAGE_SIZE))
				updateListView(++page);
		}
	}

	private class ExportaAsyncTask extends AsyncTask<Object, Void, String> {

		private ProgressDialog pd;

		protected void onPostExecute(String string) {
			pd.cancel();
			Toast.makeText(DataListActivity.this, string, 1).show();
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(DataListActivity.this);
			pd.setMessage("数据导出中...");
			pd.setCancelable(false);
			pd.show();
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Object... params) {
			return Export.export(mDBUtil.getGpsData(dataSwitch ? mDBUtil.TAB_GPS_EDIT : mDBUtil.TAB_GPS_AUTO), dataSwitch);
		}

	}
}
