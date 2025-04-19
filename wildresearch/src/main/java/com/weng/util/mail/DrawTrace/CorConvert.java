package com.weng.util.mail.DrawTrace;

/**
 * Created by Administrator on 2017/10/5.
 */

public class CorConvert {

//    /**
//     * 标准的GPS经纬度坐标直接在地图上绘制会有偏移，这是测绘局和地图商设置的加密，要转换成百度地图坐标
//     *
//     * @return 百度地图坐标
//     */
//    public GeoPoint gpsToBaidu(String data) {//data格式  nmea标准数据  ddmm.mmmmm,ddmm.mmmm 如3030.90909,11449.1234
//        String[] p = data.split(",");
//        int lat = (int) (((int) (Float.valueOf(p[0]) / 100) + (100 * (Float//将ddmm.mmmm格式转成dd.ddddd
//                .valueOf(p[0]) / 100.0 - (int) (Float.valueOf(p[0]) / 100)) / 60.0)) * 1E6);
//        int lon = (int) (((int) (Float.valueOf(p[1]) / 100) + (100 * (Float
//                .valueOf(p[1]) / 100.0 - (int) (Float.valueOf(p[1]) / 100)) / 60.0)) * 1E6);
//        GeoPoint pt = new GeoPoint(lat, lon);
//        return CoordinateConvert.fromWgs84ToBaidu(pt);//转成百度坐标
//
//    }

}
