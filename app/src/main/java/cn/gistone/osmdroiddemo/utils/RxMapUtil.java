package cn.gistone.osmdroiddemo.utils;

/**
 * Create 2020/10/28
 *
 * @author N
 * desc:
 */
public class RxMapUtil {

    /**
     * Krasovsky 1940 (北京54)椭球长半轴
     */
    private static final double A = 6378245.0;
    /**
     * 椭球的偏心率
     */
    private static final double EE = 0.00669342162296594323;

    /**
     * 国际 GPS84 坐标系
     * 转换成
     * [国测局坐标系] 火星坐标系 (GCJ-02)
     *
     * @param lon 经度
     * @param lat 纬度
     * @return double[] 0:lat 1:lgt
     */
    public static double[] gps84ToGcj02(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((A * (1 - EE)) / (magic * sqrtMagic) * Math.PI);
        dLon = (dLon * 180.0) / (A / sqrtMagic * Math.cos(radLat) * Math.PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * [国测局坐标系] 火星坐标系 (GCJ-02)
     * 转换成
     * 国际 GPS84 坐标系
     *
     * @param lon 火星经度
     * @param lat 火星纬度
     * @return double[] 0:lat 1:lgt
     */
    public static double[] gcj02ToGps84(double lat, double lon) {
        double[] res = transform(lat, lon);
        double resLat = lat * 2 - res[0];
        double resLon = lon * 2 - res[1];
        return new double[]{resLat, resLon};
    }


    /**
     * 火星坐标系 (GCJ-02)
     * 转换成
     * 百度坐标系 (BD-09)
     *
     * @param lat 纬度
     * @param lon 经度
     * @return double[] 0:lat 1:lg
     */
    public static double[] gcj02ToBd09(double lat, double lon) {
        double z = Math.sqrt(lon * lon + lat * lat) + 0.00002 * Math.sin(lat * Math.PI);
        double theta = Math.atan2(lat, lon) + 0.000003 * Math.cos(lon * Math.PI);
        double bdLon = z * Math.cos(theta) + 0.0065;
        double bdLat = z * Math.sin(theta) + 0.006;
        return new double[]{bdLat, bdLon};
    }

    /**
     * 百度坐标系 (BD-09)
     * 转换成
     * 火星坐标系 (GCJ-02)
     *
     * @param bdLon 百度*经度
     * @param bdLat 百度*纬度
     * @return GPS实体类
     */
    public static double[] bd09ToGcj02(double bdLat, double bdLon) {
        double x = bdLon - 0.0065;
        double y = bdLat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * Math.PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * Math.PI);
        double ggLat = z * Math.sin(theta);
        double ggLon = z * Math.cos(theta);
        return new double[]{ggLat, ggLon};
    }

    /**
     * 国际 GPS84 坐标系
     * 转换成
     * 百度坐标系 (BD-09)
     *
     * @param lat:
     * @param lon:
     * @return double[] 0:lat 1:lg
     */
    public static double[] gps84ToBd09(double lat, double lon) {
        double[] gcj02 = gps84ToGcj02(lat, lon);
        return gcj02ToBd09(gcj02[0], gcj02[1]);
    }

    /**
     * 百度坐标系 (BD-09)
     * 转换成
     * 国际 GPS84 坐标系
     *
     * @param bdLon 百度*经度
     * @param bdLat 百度*纬度
     * @return GPS实体类 0:lat 1:lgt
     */
    public static double[] bd09ToGps84(double bdLat, double bdLon) {
        double[] gcj02 = bd09ToGcj02(bdLat, bdLon);
        return gcj02ToGps84(gcj02[0], gcj02[1]);
    }

    /**
     * 判断坐标是否在中国境内
     *
     * @param lat:经度
     * @param lon:纬度
     * @return :返回值
     */
    private static boolean outOfChina(double lat, double lon) {
        return lon < 72.004 || lon > 137.8347 || lat < 0.8293 || lat > 55.8271;
    }

    /**
     * 转化算法
     *
     * @param lat 纬度
     * @param lon 经度
     * @return double[] 0:lat 1:lgt
     */
    private static double[] transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new double[]{lat, lon};
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * Math.PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = dLat * 180.0 / ((A * (1 - EE)) / (magic * sqrtMagic) * Math.PI);
        dLon = dLon * 180.0 / (A / sqrtMagic * Math.cos(radLat) * Math.PI);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new double[]{mgLat, mgLon};
    }

    /**
     * 纬度转化算法
     *
     * @param x x坐标
     * @param y y坐标
     * @return 纬度
     */
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * Math.PI) + 40.0 * Math.sin(y / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * Math.PI) + 320 * Math.sin(y * Math.PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转化算法
     *
     * @param x x坐标
     * @param y y坐标
     * @return 经度
     */
    private static double transformLon(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * Math.PI) + 20.0 * Math.sin(2.0 * x * Math.PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * Math.PI) + 40.0 * Math.sin(x / 3.0 * Math.PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * Math.PI) + 300.0 * Math.sin(x / 30.0 * Math.PI)) * 2.0 / 3.0;
        return ret;
    }

}
