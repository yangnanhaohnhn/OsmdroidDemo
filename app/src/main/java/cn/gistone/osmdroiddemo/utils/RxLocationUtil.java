package cn.gistone.osmdroiddemo.utils;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.SparseArray;

import org.osmdroid.util.GeoPoint;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Create 2020/10/28
 *
 * @author N
 * desc: 经纬度转换工具类
 */
public class RxLocationUtil {
    public static final String DEGREE = "°";
    public static final String MINUTE = "′";
    public static final String SECOND = "″";
    private static final double TEMP = 0.01745329251994329D;

    /**
     * 判断Gps是否可用
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 判断定位是否可用
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    public static boolean isLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 打开Gps设置界面
     */
    public static void openGpsSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 度分秒
     * 转换为
     * 度
     *
     * @param res 111°11′11.11″
     * @return 111.111111
     */
    public static String dfmToLocation(String res) {
        if (TextUtils.isEmpty(res)) {
            return "";
        }
        if (!res.contains(DEGREE)) {
            return res;
        }
        DecimalFormat df = new DecimalFormat("0.0000000000");
        int degreeIndex = res.indexOf(DEGREE);
        String degreeStr = res.substring(0, degreeIndex);
        int degreeInt = Integer.parseInt(degreeStr);
        if (!res.contains(MINUTE)) {
            return df.format(Math.abs(degreeInt));
        }
        int minuteIndex = res.indexOf(MINUTE);
        int minuteInt = Integer.parseInt(res.substring(degreeIndex + 1, minuteIndex));
        if (!res.contains(SECOND)) {
            return df.format(Math.abs(degreeInt)) + (Math.abs(minuteInt) / 60);
        }
        double secondDouble = Double.parseDouble(res.substring(minuteIndex + 1, res.indexOf(SECOND)));
        return df.format(Math.abs(degreeInt) + (Math.abs(minuteInt) + (Math.abs(secondDouble) / 60)) / 60);
    }

    /**
     * 经纬度
     * 转换为
     * 度分秒
     *
     * @param res 111.111111
     * @return 111°11′11.11″
     */
    public static String locationToDfm(String res) {
        if (TextUtils.isEmpty(res)) {
            return "";
        }
        //如果包含了 度分秒 则 直接返回该数据 记了
        if (res.contains(DEGREE) || res.contains(MINUTE) || res.contains(SECOND)) {
            return res;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        double locationDouble = Double.parseDouble(res);
        double degree = Math.floor(locationDouble);
        double minuteTemp = (locationDouble - degree) * 60;
        double minute = Math.floor(minuteTemp);
        String second = df.format((minuteTemp - minute) * 60);
        return (int) degree + DEGREE + (int) minute + MINUTE + second + SECOND;
    }

    /**
     * 度分
     * 转
     * 经纬度
     *
     * @param dfStr
     * @return
     */
    public static String dfToLocation(String dfStr) {
        if (TextUtils.isEmpty(dfStr)) {
            return "";
        }
        if (!dfStr.contains(DEGREE)) {
            return dfStr;
        }
        DecimalFormat df = new DecimalFormat("0.0000000000");
        int degreeIndex = dfStr.indexOf("°");
        double degreeDouble = Double.parseDouble(dfStr.substring(0, degreeIndex));
        if (!dfStr.contains(MINUTE)) {
            return df.format(Math.abs(degreeDouble));
        }
        double minuteDouble = Double.parseDouble(dfStr.substring(degreeIndex + 1, dfStr.indexOf(MINUTE)));
        return df.format(Math.abs(degreeDouble + minuteDouble / 60));
    }

    /**
     * 经纬度
     * 转为
     * 度分
     *
     * @param location
     * @return
     */
    public static String locationToDf(String location) {
        if (TextUtils.isEmpty(location)) {
            return "";
        }
        if (location.contains(DEGREE) || location.contains(MINUTE) || location.contains(SECOND)) {
            return location;
        }
        DecimalFormat df = new DecimalFormat("0.000");
        double locationDouble = Double.parseDouble(location);
        double degree = Math.floor(locationDouble);
        String minute = df.format((locationDouble - degree) * 60);
        return (int) degree + DEGREE + minute + MINUTE;
    }

    /**
     * 经纬度
     * 转换成  度分秒集合
     *
     * @param location 经纬度
     * @return
     */
    public static SparseArray<String> locationToDfmForArray(String location) {
        if (TextUtils.isEmpty(location)) {
            return null;
        }
        SparseArray<String> resList = new SparseArray<>(3);
        try {
            double locationDouble = Double.parseDouble(location);
            DecimalFormat df = new DecimalFormat("0.00");
            double degree = Math.floor(locationDouble);
            double minuteTemp = (locationDouble - degree) * 60;
            double minute = Math.floor(minuteTemp);
            String second = df.format((minuteTemp - minute) * 60);
            resList.put(0, String.valueOf((int) degree));
            resList.put(1, String.valueOf((int) minute));
            resList.put(2, second);
            return resList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 经纬度 转成 度分 集合
     *
     * @param location
     * @return
     */
    public static SparseArray<String> locationToDfForArray(String location) {
        if (TextUtils.isEmpty(location)) {
            return null;
        }
        SparseArray<String> resList = new SparseArray<>(3);
        try {
            DecimalFormat df = new DecimalFormat("0.000");
            double locationDouble = Double.parseDouble(location);
            double degree = Math.floor(locationDouble);
            String minute = df.format((locationDouble - degree) * 60);
            resList.put(0, String.valueOf((int) degree));
            resList.put(1, minute);
            return resList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 计算距离
     *
     * @param pointList:经纬度的集合
     * @return :返回值
     */
    public static double calculateDistanceToPointList(List<GeoPoint> pointList) {
        if (null == pointList || pointList.isEmpty()) {
            return 0;
        }
        if (pointList.size() == 1) {
            return 0;
        }
        double distance = 0;
        for (int i = 0; i < pointList.size() - 1; i++) {
            GeoPoint fromPoint = pointList.get(i);
            GeoPoint toPoint = pointList.get(i + 1);
            distance += calculateDistanceToPoint(fromPoint, toPoint);
        }
        return distance;
    }

    /**
     * 计算俩个点之间的距离
     *
     * @param fromPoint
     * @param toPoint
     * @return
     */
    public static double calculateDistanceToPoint(GeoPoint fromPoint, GeoPoint toPoint) {
        if (null == fromPoint || toPoint == null) {
            return 0;
        }
        try {
            double tmpFromLon = fromPoint.getLongitude() * TEMP;
            double tmpFromLat = fromPoint.getLatitude() * TEMP;
            double tmpToLon = toPoint.getLongitude() * TEMP;
            double tmpToLat = toPoint.getLatitude() * TEMP;
            double value = Math.cos(tmpFromLat) * Math.cos(tmpFromLon) - Math.cos(tmpToLat) * Math.cos(tmpToLon);
            double value1 = Math.cos(tmpFromLat) * Math.sin(tmpFromLon) - Math.cos(tmpToLat) * Math.sin(tmpToLon);
            double value2 = Math.sin(tmpFromLat) - Math.sin(tmpToLat);
            double valueDouble = Math.sqrt(value * value + value1 * value1 + value2 * value2);
            return Math.asin(valueDouble / 2.0D) * 1.27420015798544E7D;
        } catch (Throwable e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 计算面积
     *
     * @param pointList
     * @return
     */
    public static double calculateArea(List<GeoPoint> pointList) {
        if (pointList == null || pointList.isEmpty()) {
            return 0;
        }
        if (pointList.size() < 3) {
            return 0;
        }
        double var2 = 0.0D;
        double var4 = 111319.49079327357D;
        int var6 = pointList.size();

        for (int i = 0; i < var6; ++i) {
            GeoPoint var8 = pointList.get(i);
            GeoPoint var9 = pointList.get((i + 1) % var6);
            double var10 = var8.getLongitude() * var4 * Math.cos(var8.getLatitude() * TEMP);
            double var12 = var8.getLatitude() * var4;
            double var14 = var9.getLongitude() * var4 * Math.cos(var9.getLatitude() * TEMP);
            double var16 = var9.getLatitude() * var4;
            var2 += var10 * var16 - var14 * var12;
        }
        return Math.abs(var2 / 2.0D);
    }

    /**
     * 计算经纬度围成的实际面积（平方公里）
     * 以俩个点位点的面积
     *
     * @param fromPoint
     * @param toPoint
     * @return
     */
    public static double calculateArea(GeoPoint fromPoint, GeoPoint toPoint) {
        if (null == fromPoint || toPoint == null) {
            return 0;
        }
        try {
            double var2 = Math.sin(fromPoint.getLatitude() * Math.PI / 180.0D) - Math.sin(toPoint.getLatitude() * Math.PI / 180.0D);
            double var4 = (toPoint.getLongitude() - fromPoint.getLongitude()) / 360.0D;
            if (var4 < 0) {
                ++var4;
            }
            return 2.5560394669790553E14D * var2 * var4;
        } catch (Throwable var6) {
            var6.printStackTrace();
            return 0.0F;
        }
    }

//    /**
//     * 是否超过该点组合的面
//     *
//     * @param curGeoPoint
//     * @param resPointList
//     * @return true:包含
//     */
//    public static boolean isContainPoint(GeoPoint curGeoPoint, List<GeoPoint> resPointList) {
//        if (UIUtil.isEmptyByList(resPointList)) {
//            return false;
//        }
//        if (null == curGeoPoint) {
//            return false;
//        }
//        LatLngBounds.Builder builder = new LatLngBounds.Builder();
//        for (GeoPoint point : resPointList) {
//            builder.include(new LatLng(point.getLatitude(), point.getLongitude()));
//        }
//        LatLng latLng = new LatLng(curGeoPoint.getLatitude(), curGeoPoint.getLongitude());
//        return builder.build().contains(latLng);
//    }

//    /**
//     * 是否在多边形内
//     *
//     * @param geoPoint
//     * @param pointList
//     * @return 没有点,就在区域内
//     */
//    public static boolean isIncludePoint(GeoPoint geoPoint, List<GeoPoint> pointList) {
//        Point point = Point.fromLngLat(geoPoint.getLongitude(), geoPoint.getLatitude());
//        List<Point> resList = new ArrayList<>();
//        for (GeoPoint item : pointList) {
//            resList.add(Point.fromLngLat(item.getLongitude(), item.getLatitude()));
//        }
//        List<List<Point>> pointListList = new ArrayList<>();
//        pointListList.add(resList);
//        com.mapbox.geojson.Polygon polygon = com.mapbox.geojson.Polygon.fromLngLats(pointListList);
//        return TurfJoins.inside(point, polygon);
//    }


    public static void main(String[] args) {
//        System.out.println(Math.floor(1113.91111));

//        String s = dfmToLocation("106°13′37.26″");//106.22701666666667 √
        String s = locationToDfm("106.22701666666667");//106°13′37.26″ √
        String s1 = locationToDf("106.22701666666667");//106°13.621′ √

//        SparseArray<String> s2 = locationToDfmForArray("106.22701666666667");
//        SparseArray<String> s3 = locationToDfForArray("106.22701666666667");
        String s2 = dfToLocation("106°13.621′");//106.2270166667
//        String s3 = locationToDf("106.22701666666667");//106°13.621′
        System.out.println(s + "|" + s1 + "|" + s2);
    }
}
