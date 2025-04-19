package com.weng.util.mail.DrawTrace;

/**
 * http://blog.csdn.net/lgzvic/article/details/17842697?locationNum=5
 * Created by Administrator on 2017/10/5.
 */

public class DrawRoute {
//
//    /**
//     * 绘制点线
//     */
//    public void addCustomElementsDemo(String[] data) {
//        GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
//        mMapView.getOverlays().add(graphicsOverlay);
//        // 添加折线
//        graphicsOverlay.setData(drawLine(data));//轨迹
//        // 添加点
//        graphicsOverlay.setData(drawPoint(data[0]));//起点
//        graphicsOverlay.setData(drawPoint(data[count - 1]));//终点
//        // 执行地图刷新使生效
//        mMapView.refresh();
//    }
//
//    /**
//     * 绘制单点，该点状态不随地图状态变化而变化
//     *
//     * @return 点对象
//     */
//    public Graphic drawPoint(String data) {
//
//        GeoPoint pt1 = gpsToBaidu(data);
//
//        // 构建点
//        Geometry pointGeometry = new Geometry();
//        // 设置坐标
//        pointGeometry.setPoint(pt1, 10);
//        // 设定样式
//        Symbol pointSymbol = new Symbol();
//        Symbol.Color pointColor = pointSymbol.new Color();
//        pointColor.red = 0;
//        pointColor.green = 126;
//        pointColor.blue = 255;
//        pointColor.alpha = 255;
//        pointSymbol.setPointSymbol(pointColor);
//        // 生成Graphic对象
//        Graphic pointGraphic = new Graphic(pointGeometry, pointSymbol);
//        return pointGraphic;
//    }
//
//    /**
//     * 绘制折线，该折线状态随地图状态变化
//     *
//     * @return 折线对象
//     */
//    public Graphic drawLine(String[] data) {
//
//        // 构建线
//        Geometry lineGeometry = new Geometry();
//        // 设定折线点坐标
//        GeoPoint[] linePoints = new GeoPoint[count];
//        for (int i = 0; i < count; i++) {
//            linePoints[i] = gpsToBaidu(data[i]);
//        }
//
//        lineGeometry.setPolyLine(linePoints);
//        // 设定样式
//        Symbol lineSymbol = new Symbol();
//        Symbol.Color lineColor = lineSymbol.new Color();
//        lineColor.red = 33;
//        lineColor.green = 99;
//        lineColor.blue = 255;
//        lineColor.alpha = 255;
//        lineSymbol.setLineSymbol(lineColor, 10);
//        // 生成Graphic对象
//        Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
//        return lineGraphic;
//    }


}
