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

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBUtil {

    public static final String DB_NAME = "GPSData.db";
    public static final String TAB_GPS_AUTO = "tab_gps_auto";
    public static final String TAB_GPS_EDIT = "tab_gps_edit";

    public final Context mContext;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSQLiteDatabase;



    public DBUtil(Context context)
    {
        mContext = context;
    }


    // 打开数据库
    public void openDB() {
        mDatabaseHelper = new DatabaseHelper(mContext);
        mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
    }

    // 关闭数据库
    public void closeDB() {
        mDatabaseHelper.close();
    }

    // 获取数据库实例
    public SQLiteDatabase getSQLiteDatabase() { return mSQLiteDatabase;}


    // 增加自动获取的GPS点入数据库
    public boolean addGpsData(Gps cdata) {
        this.openDB();
        boolean result = true;
        try {
            String sql = String.format("insert into tab_gps_auto (latitude,longitude,high,direct,speed,gpstime) values (%f,%f,%f,%f,%f,'%s');",
                            cdata.getLatitude(), cdata.getLongitude(),
                            cdata.getHigh(), cdata.getDirect(),
                            cdata.getSpeed(), cdata.getGpsTime());
            this.getSQLiteDatabase().execSQL(sql);
            result = true;
        }
        catch (SQLException e) {
            result = false;
            Toast.makeText(mContext, "保存GPS数据失败:" + e.getMessage(),Toast.LENGTH_LONG).show();
        }
        this.closeDB();
        return result;
    }

    // 增加手动添加的GPS数据到数据库
    public boolean addGpsData(Gps cdata, String description) {
        this.openDB();
        boolean result = true;
        try {
            String sql = String
                    .format("insert into tab_gps_edit (latitude,longitude,high,direct,speed,gpstime,desciption) values (%f,%f,%f,%f,%f,'%s','%s');",
                            cdata.getLatitude(), cdata.getLongitude(),
                            cdata.getHigh(), cdata.getDirect(),
                            cdata.getSpeed(), cdata.getGpsTime(), description);
            this.getSQLiteDatabase().execSQL(sql);
            result = true;
        } catch (SQLException e) {
            result = false;
            Toast.makeText(mContext, "保存GPS数据失败:" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        this.closeDB();
        return result;
    }

    // 从表格中获取已经记录的GPS点数目
    public int getGpsDataCount(String tableName) {
        int result = 0;
        this.openDB();

        String sql = "select count(id) as count from " + tableName;
        Cursor c = this.getSQLiteDatabase().rawQuery(sql, null);
        // Cursor c = mDBUtil.getSQLiteDatabase().query("tab_gps", new String[]{"count(*)"}, null, null, null, null, null);
        // result = c.getColumnIndex("count");
        if (c.moveToLast()) {
            result = (int) c.getLong(0);
        }

        // return
        this.closeDB();
        return result;
    }

    // 以页的形式获取GPS数据
    public List<Gps> getGpsByPageNumber(int pageNum/*began from 0*/, String tableName) {
        this.openDB();
        List<Gps> result = new ArrayList<Gps>();
        String sql = "select * from " + tableName + " limit " + Constant.PAGE_SIZE + " offset " + pageNum * Constant.PAGE_SIZE;//
        // String[] columns = new String[]{"id","infotype","latitude","longitude","high","direct","speed","gpstime"};
        Cursor c = this.getSQLiteDatabase().rawQuery(sql, null);
        Gps gps;
        if (DBUtil.TAB_GPS_AUTO.equals(tableName) && c.getCount() != 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                gps = new Gps();
                gps.setLatitude(c.getDouble(1));
                gps.setLongitude(c.getDouble(2));
                gps.setHigh(c.getDouble(3));
                gps.setDirect(c.getDouble(4));
                gps.setSpeed(c.getDouble(5));
                gps.setGpsTime(c.getString(6));
                result.add(gps);
                c.moveToNext();
            }
        } else if (DBUtil.TAB_GPS_EDIT.equals(tableName) && c.getCount() != 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                gps = new Gps();
                gps.setLatitude(c.getDouble(1));
                gps.setLongitude(c.getDouble(2));
                gps.setHigh(c.getDouble(3));
                gps.setDirect(c.getDouble(4));
                gps.setSpeed(c.getDouble(5));
                gps.setGpsTime(c.getString(6));
                gps.setDescription(c.getString(7));
                result.add(gps);
                c.moveToNext();
            }
        }
        this.closeDB();
        return result;
    }

    // 获取表格中的所有GPS数据
    public List<Gps> getGpsData(String tableName) {
        this.openDB();
        List<Gps> result = new ArrayList<Gps>();
        String sql = "select * from " + tableName;
        // String[] columns = new String[]{"id","infotype","latitude","longitude","high","direct","speed","gpstime"};
        Cursor c = this.getSQLiteDatabase().rawQuery(sql, null);
        Gps gps;
        if (DBUtil.TAB_GPS_AUTO.equals(tableName) && c.getCount() != 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                gps = new Gps();
                gps.setLatitude(c.getDouble(1));
                gps.setLongitude(c.getDouble(2));
                gps.setHigh(c.getDouble(3));
                gps.setDirect(c.getDouble(4));
                gps.setSpeed(c.getDouble(5));
                gps.setGpsTime(c.getString(6));
                result.add(gps);
                c.moveToNext();
            }
        } else if (DBUtil.TAB_GPS_EDIT.equals(tableName) && c.getCount() != 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                gps = new Gps();
                gps.setLatitude(c.getDouble(1));
                gps.setLongitude(c.getDouble(2));
                gps.setHigh(c.getDouble(3));
                gps.setDirect(c.getDouble(4));
                gps.setSpeed(c.getDouble(5));
                gps.setGpsTime(c.getString(6));
                gps.setDescription(c.getString(7));
                result.add(gps);
                c.moveToNext();
            }
        }
        this.closeDB();
        return result;
    }



    public void deleteAllRecordData() {
        this.openDB();
        String sql = "delete from " + TAB_GPS_AUTO;
        this.getSQLiteDatabase().execSQL(sql);
        sql = "delete from " + TAB_GPS_EDIT;
        this.getSQLiteDatabase().execSQL(sql);
        this.closeDB();
    }


}



// 数据库操作帮助类
class DatabaseHelper extends SQLiteOpenHelper
{

    public DatabaseHelper(Context context)
    {
        super(context,DBUtil.DB_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase sdb) {
        // 建表(id, lat, lng, heigh, direct, speed, gpstime/[desciption])
        sdb.execSQL("create table tab_gps_auto (id integer primary key autoincrement,latitude double,longitude double,high double,direct double,speed double,gpstime text);");
        sdb.execSQL("create table tab_gps_edit (id integer primary key autoincrement,latitude double,longitude double,high double,direct double,speed double,gpstime text,desciption text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion) {
        sdb.execSQL("drop table if exists tab_gps_auto");
        sdb.execSQL("drop table if exists tab_gps_edit");
        onCreate(sdb);
    }
}
