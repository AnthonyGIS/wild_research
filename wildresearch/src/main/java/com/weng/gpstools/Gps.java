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

import java.io.Serializable;


/**
 * GPS对象类
 */
public class Gps implements Serializable {

    private static final long serialVersionUID = 1725552388282878449L;
    private int infoType;           //数据类型
    private double latitude;        //纬度
    private double longitude;       //经度
    private double high;            //海拔
    private double direct;          //方向
    private double speed;           //速度
    private String gpsTime;        //GPS时间
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public int getInfoType() {
        return infoType;
    }
    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {return longitude;}
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public double getHigh() {
        return high;
    }
    public void setHigh(double high) {this.high = high;}
    public double getDirect() {
        return direct;
    }
    public void setDirect(double direct) {
        this.direct = direct;
    }
    public double getSpeed() {
        return speed;
    }
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public String getGpsTime() {
        return gpsTime;
    }
    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    @Override
    public String toString() {
        return "数据类型:" + infoType + ",纬度:" + latitude + ",经度:" + longitude + ",海拔:" + high + ",方向:" + direct + ",速度:" + speed + ",GPS时间:" + gpsTime;
    }

    public boolean equals(Gps o) {
        boolean result = false;
        switch (infoType) {
            case Constant.GPS_AUTO_LOCAL:
                if (this.latitude == o.getLatitude()
                        && this.longitude == o.getLongitude()) {
                    result = true;
                }
                break;
            case Constant.GPS_EDIT_LOCAL:
//			if (null==o.getDescription() || null==this.description || this.description.equals(o.getDescription())) {
//				result = true;
//			}
                break;
        }

        return result;
    }
}
