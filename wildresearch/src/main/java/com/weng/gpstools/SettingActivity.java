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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;
import com.weng.R;


public class SettingActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static final String KEY_EXPORT_XML_PREFERENCE = "export_xml_preference";
	public static final String KEY_EXPORT_CSV_PREFERENCE = "export_csv_preference";
	public static final String KEY_PAGE_SIZE_PREFERENCE = "page_size_preference";
	public static final String KEY_CLEAR_PREFERENCE = "clear_preference";
	private static final int DIALOG_YES_NO_MESSAGE = 1;

	private CheckBoxPreference exportXML;
	private CheckBoxPreference exportCSV;
	private ListPreference pageSize;
	private MyPreference clear;
	private DBUtil mDBUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.setting);
		exportXML = (CheckBoxPreference) getPreferenceScreen().findPreference(
				KEY_EXPORT_XML_PREFERENCE);
		exportCSV = (CheckBoxPreference) getPreferenceScreen().findPreference(
				KEY_EXPORT_CSV_PREFERENCE);
		pageSize = (ListPreference) getPreferenceScreen().findPreference(
				KEY_PAGE_SIZE_PREFERENCE);
		clear = (MyPreference)getPreferenceScreen().findPreference(
				KEY_CLEAR_PREFERENCE);
		clear.setOnPreferenceClickListener(new OnPreferenceClickListener(){

			public boolean onPreferenceClick(Preference preference) {
				showDialog(DIALOG_YES_NO_MESSAGE);
				return false;
			}
			
		});
		
		this.mDBUtil = new DBUtil(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// Let's do something when my counter preference value changes
//		Toast.makeText(
//				this,
//				key,
//				Toast.LENGTH_SHORT).show();
		// Restore preferences
		Constant.PAGE_SIZE = Integer.valueOf(sharedPreferences.getString(Constant.PAGE_SIZE_PREF, "20"));
        Constant.EXPORT_XML = sharedPreferences.getBoolean(Constant.EXPORT_XML_PREF, true);
        Constant.EXPORT_CSV = sharedPreferences.getBoolean(Constant.EXPORT_CSV_PREF, true);
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_YES_NO_MESSAGE:
			return new AlertDialog.Builder(SettingActivity.this)
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle(R.string.clear)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked OK so do some stuff */
									clearHistory();
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Cancel so do some stuff */
								}
							}).create();
		}
		return null;
	}
	
	private void clearHistory(){
		try{
			mDBUtil.deleteAllRecordData();
			Toast.makeText(this, "数据已成功清除", 1).show(); 
		}catch (Exception e) {
			Toast.makeText(this, "数据清除失败", 1).show(); 
		}
	}

}
