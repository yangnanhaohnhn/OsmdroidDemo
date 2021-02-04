package cn.gistone.osmdroiddemo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Create 2021/1/23
 *
 * @author N
 * desc:
 */
public class DateUtil {
    public static final String TYPE_1 = "yyyyMMdd";
    private static SimpleDateFormat getCommonDate(String type) {
        return new SimpleDateFormat(type, Locale.CHINA);
    }

    public static String getDateByLong(long time, String type) {
        return getCommonDate(type).format(new Date(time));
    }
}
