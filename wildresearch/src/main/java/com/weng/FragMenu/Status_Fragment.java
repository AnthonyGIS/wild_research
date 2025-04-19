package com.weng.FragMenu;

import android.app.AlertDialog;
import android.app.Fragment;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.weng.gpstools.Constant;
import com.weng.gpstools.CustomMenu;
import com.weng.gpstools.CustomMenuItem;
import com.weng.gpstools.DBUtil;
import com.weng.gpstools.DataListActivity;
import com.weng.gpstools.FeedbackActivity;
import com.weng.gpstools.Gps;
import com.weng.gpstools.HelpActivity;
import com.weng.R;
import com.weng.gpstools.SettingActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;



public class Status_Fragment extends Fragment implements View.OnClickListener, CustomMenu.OnMenuItemSelectedListener {
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
    private TextView tv_star_cnt;// 搜星个数
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fp_status_frag_layout,container,false);

        tv_lat = (TextView) view.findViewById(R.id.lat_value);
        tv_lon = (TextView)  view.findViewById(R.id.lon_value);
        tv_high = (TextView)  view.findViewById(R.id.high_value);
        tv_direct = (TextView)  view.findViewById(R.id.direct_value);
        tv_speed = (TextView)  view.findViewById(R.id.speed_value);
        tv_gps_time = (TextView)  view.findViewById(R.id.gps_time_value);
        tv_gps_type = (TextView)  view.findViewById(R.id.gps_type_value);
        tv_db_status = (TextView)  view.findViewById(R.id.db_status);

        btn_edit = (Button)  view.findViewById(R.id.edit);
        btn_exit = (Button)  view.findViewById(R.id.exit);
        btn_edit.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
//        Typeface face=Typeface.createFromAsset(getAssets(),"HandmadeTypewriter.ttf");//也可以使用其他的静态方法获取
//        startCount.setTypeface(face);

        mDBUtil = new DBUtil(view.getContext());
        storeDate = new Gps();
        //初始化GPS定位
        InitLocation();


        // 初始化菜单 (v0.1版本中，无法弹出菜单，在有些设备中)
        // -----------------------------------------------------------------------------------------
        mMenu = new CustomMenu(getActivity(), this, getActivity().getLayoutInflater());
        mMenu.setHideOnSelect(true);
        mMenu.setItemsPerLineInPortraitOrientation(4);
        mMenu.setItemsPerLineInLandscapeOrientation(8);
        //load the menu items
        loadMenuItems();



        // Restore preferences
        SharedPreferences settings = getActivity().getSharedPreferences(Constant.SETTING_PREF, 0); // 获取一个SharedPreferences对象
        Constant.PAGE_SIZE = Integer.valueOf(settings.getString(Constant.PAGE_SIZE_PREF, "20"));
        Constant.EXPORT_XML = settings.getBoolean(Constant.EXPORT_XML_PREF, true);
        Constant.EXPORT_CSV = settings.getBoolean(Constant.EXPORT_CSV_PREF, true);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 结束GPS定位服务
     */
    public void finish() {
        mLocationManager.removeUpdates(LocationListener);
        getActivity().finish();
    }


    /**
     * GPS初始化
     */
    private void InitLocation() {
        mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
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
    private final android.location.LocationListener LocationListener = new LocationListener() {
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
                Toast.makeText(getActivity(), "GPS功能已关闭", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
                startActivityForResult(intent, 0); //此为设置完成后返回到获取界面
            }
        }
        else {

            cdata.setInfoType(infotype);

            //跟新页面的数据显示
            this.tv_lat.setText(String.format("%.6f", cdata.getLatitude()));
            this.tv_lon.setText(String.format("%.6f", cdata.getLongitude()));
            this.tv_high.setText(String.format("%.2f m", cdata.getHigh()));
            this.tv_direct.setText(String.format("%.2f°", cdata.getDirect()));
            this.tv_speed.setText(String.format("%.2f m/s (%4.1f km/h)", cdata.getSpeed(),cdata.getSpeed()*3.6));
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
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Iterator<GpsSatellite> iterator = manager.getGpsStatus(null).getSatellites().iterator();

        int count = 0;
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }
        return count;   // return manager.getGpsStatus(null).getMaxSatellites();
    }

    public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.exit) {
			finish();
		}
		else if (id == R.id.edit) {// POI添加的对话框
			// This example shows how to add a custom layout to an AlertDialog
			LayoutInflater factory = LayoutInflater.from(this.getActivity());
			final View textEntryView = factory.inflate(R.layout.edit_dialog, null);
			descript = (EditText) textEntryView.findViewById(R.id.desciption);

			new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.alert_dialog_icon)
					.setTitle(R.string.description)
					.setView(textEntryView)
					.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							/* User clicked OK so do some stuff */
							Toast.makeText(getActivity(), descript.getText(), Toast.LENGTH_LONG).show();
							ShowInfo(getLastPosition(), Constant.GPS_EDIT_LOCAL); // 将最后一个移植点加入到数据库
						}
					}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {

							/* User clicked cancel so do some stuff */
						}
					}).create().show();
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
        return getActivity().onKeyDown(keyCode, event);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
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
            mMenu.show(getActivity().findViewById(R.id.any_old_widget));
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
                intent.setClass(getActivity(), DataListActivity.class);
                startActivity(intent);
                break;
            case 2:
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent.setClass(getActivity(), HelpActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent.setClass(getActivity(), FeedbackActivity.class);
                startActivity(intent);
                break;
        }
    }
}








