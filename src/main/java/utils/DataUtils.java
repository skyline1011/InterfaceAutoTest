package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 数据工具类：String转MAP，获取当前时间等
 */
public class DataUtils {
    /**
     * String类型转MAP
     * @param str
     * @return
     */
    public static Map<String,Object> covertStringToMp(String str){
        //str=method=loginMobile&loginname=test1&loginpass=test1
        if(null==str){
            return null;
        }
        Map<String, Object> paramMp = new LinkedHashMap<String, Object>();//为防止数据有序
        String[] key_array = str.split("&");
        for(String keys:key_array){
            String[] kStrings = keys.split("=");
            String key = kStrings[0];
            String value = kStrings[1];
            paramMp.put(key, value);
        }
        System.out.println("封装后的Map为：" + paramMp.toString());
        return paramMp;
    }

    //以分号分割的字符串；
    public static Map<String, Object> covertStringToMp2(String str) {
        if(str==null) {
            return null;
        }
        Map<String, Object> paramMp = new LinkedHashMap<String, Object>();
        String[] key_array = str.split(";");
        for (String keys : key_array) {
            String[] kStrings = keys.split("=");
            String key = kStrings[0];
            String value = kStrings[1];
            paramMp.put(key, value);
        }
        return paramMp;
    }

    /**
     *
     * @param time
     * @return
     */
    public static TimeUnit getTimeUnit(String time) {
        if("-h".equals(time)) {
            return TimeUnit.HOURS;
        }else if("-m".equals(time)) {
            return TimeUnit.MINUTES;
        }else if("-d".equals(time)) {
            return TimeUnit.DAYS;
        }else {
            return TimeUnit.SECONDS;
        }
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");
        String time = formatter.format(date);
        return time;
    }
}
