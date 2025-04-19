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
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weng.R;
import com.weng.gpstools.CustomMenu.OnMenuItemSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class _del_MainActivity extends Activity implements OnClickListener, OnMenuItemSelectedListener {
    /**
     * Called when the activity is first created.
     */

    private LocationManager mLocationManager;
    private Location mLocation;
    private Criteria mCriteria;
    private String provider;
    private static final int EDIT_DIALOG = 1;
    private EditText descript;
    private DBUtil mDBUtil;
    private Gps storeDate;  // 重复数据只保留一份


    private TextView tv_lat;//纬度
    private TextView tv_lon;//经度
    private TextView tv_high;//海拔
    private TextView tv_direct;//方向
    private TextView tv_speed;//速度
    //	private TextView starCount;// 搜星个数
    private TextView tv_gps_time;
    private TextView tv_gps_type;
    private TextView tv_db_status;

    private Button btn_edit;
    private Button btn_exit;

    private CustomMenu mMenu;
    private static final int MENU_ITEM_1 = 1;
    private static final int MENU_ITEM_2 = 2;
    private static final int MENU_ITEM_3 = 3;
    private static final int MENU_ITEM_4 = 4;

    private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fp_gps_main_layout);

        tv_lat = (TextView) findViewById(R.id.lat_value);
        tv_lon = (TextView) findViewById(R.id.lon_value);
        tv_high = (TextView) findViewById(R.id.high_value);
        tv_direct = (TextView) findViewById(R.id.direct_value);
        tv_speed = (TextView) findViewById(R.id.speed_value);
        tv_gps_time = (TextView) findViewById(R.id.gps_time_value);
        tv_gps_type = (TextView) findViewById(R.id.gps_type_value);
        tv_db_status = (TextView) findViewById(R.id.db_status);

        btn_edit = (Button) findViewById(R.id.edit);
        btn_exit = (Button) findViewById(R.id.exit);
        btn_edit.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
//        Typeface face=Typeface.createFromAsset(getAssets(),"HandmadeTypewriter.ttf");//也可以使用其他的静态方法获取
//        startCount.setTypeface(face);

        mDBUtil = new DBUtil(this);
        storeDate = new Gps();
        //初始化GPS定位
        InitLocation();

        // 初始化菜单 (v0.1版本中，无法弹出菜单，在有些设备中)
        mMenu = new CustomMenu(this, this, getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(4);
        mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();



        // Restore preferences
        SharedPreferences settings = getSharedPreferences(Constant.SETTING_PREF, 0); // 获取一个SharedPreferences对象
        Constant.PAGE_SIZE = Integer.valueOf(settings.getString(Constant.PAGE_SIZE_PREF, "20"));
        Constant.EXPORT_XML = settings.getBoolean(Constant.EXPORT_XML_PREF, true);
        Constant.EXPORT_CSV = settings.getBoolean(Constant.EXPORT_CSV_PREF, true);
    }


    /**
     * 结束GPS定位服务
     */
    @Override
    public void finish() {
        mLocationManager.removeUpdates(LocationListener);
        super.finish();
    }


    /**
     * GPS初始化
     */
    private void InitLocation() {
        mLocationManager = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (mLocationManager.isProviderEnabled(mLocationManager.GPS_PROVIDER)) {
            mCriteria = new Criteria();
            mCriteria.setAccuracy(Criteria.ACCURACY_COARSE);// 高精度
            mCriteria.setAltitudeRequired(true);// 显示海拔
            mCriteria.setBearingRequired(true);// 显示方向
            mCriteria.setSpeedRequired(true);// 显示速度
            mCriteria.setCostAllowed(true);// 不允许有花费
            mCriteria.setPowerRequirement(Criteria.POWER_MEDIUM);// 低功耗POWER_LOW
            provider = mLocationManager.getBestProvider(mCriteria, true); // 获取GPS信息
            // 位置变化监听,默认5秒一次,距离10米以上
            mLocationManager.requestLocationUpdates(provider, 1000, 1, LocationListener);
        }
        else{
            ShowInfo(null, Constant.GPS_LOST);
        }
    }

    /**
     * 初始化GPS监听
     */
    private final LocationListener LocationListener = new LocationListener() {
        public void onLocationChanged(Location arg0) {
            ShowInfo(getLastPosition(), Constant.GPS_AUTO_LOCAL);
        }
        public void onProviderDisabled(String arg0) {
            ShowInfo(null, Constant.GPS_LOST);
        }
        public void onProviderEnabled(String arg0) { }
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) { }
    };


    /**
     * 获取GPS数据
     * @return GPS对象信息
     */
    private Gps getLastPosition() {
        Gps result = new Gps();
        if (null != provider) {
            mLocation = mLocationManager.getLastKnownLocation(provider);
        }
        if (mLocation != null) {
            result.setLatitude(mLocation.getLatitude());
            result.setLongitude(mLocation.getLongitude());
            result.setHigh(mLocation.getAltitude());
            result.setDirect(mLocation.getBearing());
            result.setSpeed(mLocation.getSpeed());
            result.setGpsTime(df.format(new Date()));
        }
        return result;
    }


    /**
     * 显示 GPS数据
     *
     * @param cdata cdata
     * @param infotype GPS状态的变化
     */
    private void ShowInfo(Gps cdata, int infotype) {
        if (cdata == null) {
            if (infotype == Constant.GPS_LOST) {
                Toast.makeText(this, "GPS功能已关闭", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivityForResult(intent, 0); //此为设置完成后返回到获取界面
            }
        }
        else {

            cdata.setInfoType(infotype);

            //跟新页面的数据显示
            this.tv_lat.setText(String.format("%.6f", cdata.getLatitude()));
            this.tv_lon.setText(String.format("%.6f", cdata.getLongitude()));
            this.tv_high.setText(String.format("%.2fm", cdata.getHigh()));
            this.tv_direct.setText(String.format("%.2f°", cdata.getDirect()));
            this.tv_speed.setText(String.format("%.4fm/s (%.0fkm/h)", cdata.getSpeed()/3.6,cdata.getSpeed()));
            this.tv_gps_time.setText(String.format("%s", cdata.getGpsTime()));
            this.tv_gps_type.setText(String.format("%d", cdata.getInfoType()));


            switch (infotype) {
                case Constant.GPS_AUTO_LOCAL:

                    // 自动获取GPS数据
                    if (null != storeDate && storeDate.equals(cdata)) {
                        return;
                    }
                    else if (cdata.getLatitude() != 0 && cdata.getLongitude() != 0) {
                        this.mDBUtil.addGpsData(cdata); // data save to db
                        storeDate = cdata;
                    }
                    break;
                case Constant.GPS_EDIT_LOCAL:
                    //	Toast.makeText(this, "手动获取GPS数据 "+infotype, 1).show();
                    if (cdata.getLatitude() != 0 && cdata.getLongitude() != 0) {
                        this.mDBUtil.addGpsData(cdata, descript.getText().toString());
                    }
                    break;
            }


            // 查询本地数据库，更新记录条数信息
            int totall_anto, totall_poi;
            totall_anto = mDBUtil.getGpsDataCount(mDBUtil.TAB_GPS_AUTO);
            totall_poi = mDBUtil.getGpsDataCount(mDBUtil.TAB_GPS_EDIT);
            this.tv_db_status.setText(String.format("GPS Tracks Record number is: %d\tPOI Totall is: %d",totall_anto,totall_poi));



        }
    }

    //获取当前已收到卫星的个数
    private int getStarCount() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Iterator<GpsSatellite> iterator = manager.getGpsStatus(null).getSatellites().iterator();

        int count = 0;
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        return count;   // return manager.getGpsStatus(null).getMaxSatellites();
    }




    // POI添加的对话框
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case EDIT_DIALOG:
                // This example shows how to add a custom layout to an AlertDialog
                LayoutInflater factory = LayoutInflater.from(this);
                final View textEntryView = factory.inflate(R.layout.edit_dialog, null);
                descript = (EditText) textEntryView.findViewById(R.id.desciption);
                return new AlertDialog.Builder(this)
                        .setIcon(R.drawable.alert_dialog_icon)
                        .setTitle(R.string.description)
                        .setView(textEntryView)
                        .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                        /* User clicked OK so do some stuff */
                                Toast.makeText(_del_MainActivity.this, descript.getText(), 1).show();
                                ShowInfo(getLastPosition(), Constant.GPS_EDIT_LOCAL);
                            }
                        })
                        .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                            }
                        })
                        .create();
        }
        return null;
    }

    public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.exit) {
			finish();
		}
		else if (id == R.id.edit) {
			showDialog(this.EDIT_DIALOG);
		}
    }







    // Menu
    // ------------------------------------------------------------------------------


    /**
     * Snarf the menu key.
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            doMenu();
            return true; //always eat it!
        }
        return super.onKeyDown(keyCode, event);
    }


    // Load up our menu.
    private void loadMenuItems() {
        //This is kind of a tedious way to load up the menu items.
        //Am sure there is room for improvement.
        ArrayList<CustomMenuItem> menuItems = new ArrayList<CustomMenuItem>();
        CustomMenuItem cmi = new CustomMenuItem();
        cmi.setCaption(getResources().getString(R.string.export));
        cmi.setImageResourceId(R.drawable.menu_icon_export);
        cmi.setId(MENU_ITEM_1);
        menuItems.add(cmi);
        cmi = new CustomMenuItem();
        cmi.setCaption(getResources().getString(R.string.setting));
        cmi.setImageResourceId(R.drawable.menu_icon_setting);
        cmi.setId(MENU_ITEM_2);
        menuItems.add(cmi);
        cmi = new CustomMenuItem();
        cmi.setCaption(getResources().getString(R.string.help));
        cmi.setImageResourceId(R.drawable.menu_icon_help);
        cmi.setId(MENU_ITEM_3);
        menuItems.add(cmi);
        cmi = new CustomMenuItem();
        cmi.setCaption(getResources().getString(R.string.feedback));
        cmi.setImageResourceId(R.drawable.menu_icon_feedback);
        cmi.setId(MENU_ITEM_4);
        menuItems.add(cmi);
        if (!mMenu.isShowing())
            try {
                mMenu.setMenuItems(menuItems);
            } catch (Exception e) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Egads!");
                alert.setMessage(e.getMessage());
                alert.show();
            }
    }

    // Toggle our menu on user pressing the menu key.
    private void doMenu() {
        if (mMenu.isShowing()) {
            mMenu.hide();
        } else {
            //Note it doesn't matter what widget you send the menu as long as it gets view.
            mMenu.show(findViewById(R.id.any_old_widget));
        }
    }


    // For the demo just toast the item selected.
    public void MenuItemSelectedEvent(CustomMenuItem selection) {
        //	Toast t = Toast.makeText(this, "You selected item #"+Integer.toString(selection.getId()), Toast.LENGTH_SHORT);
        //	t.setGravity(Gravity.CENTER, 0, 0);
        //	t.show();

        Intent intent = new Intent();
        switch (selection.getId()) {
            case 1:
                intent.setClass(_del_MainActivity.this, DataListActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(_del_MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent.setClass(_del_MainActivity.this, HelpActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent.setClass(_del_MainActivity.this, FeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }
}