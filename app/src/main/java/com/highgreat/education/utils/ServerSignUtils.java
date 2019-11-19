package com.highgreat.education.utils;


import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by chengbin on 2016/4/26.
 * 服务器加密签名工具类
 */
public class ServerSignUtils {


    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparatorUp());

        sortMap.putAll(map);
        return sortMap;
    }
    static  class MapKeyComparatorUp implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }



    public static String getSignMD5(Map<String, String> map) {
        String str = "";

        Set<String> set = map.keySet();
        for (String s : set) {
            System.out.println(s + "," + map.get(s));
            str = str + map.get(s);
        }
        //LogUtil.e("getSignMD5", "getSignMD5==" + str);
        //234u30u1n1h2gh    是服务器给的固定值
        return MD5Util.MD5(str + "d318faa46ba267f51f6f583f704dccf4");
    }


    public static String getMapParamStr(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
        Map.Entry<String, String> entry;
        while (iter.hasNext()) {
            entry = iter.next();
            try {
                builder.append('&');
                builder.append(entry.getKey());
                builder.append('=');
                builder.append(entry.getValue());

            } catch (Exception e) {
//                e.printStackTrace();
                LogUtil.e("Exception",e.getMessage());
            }
        }
        return builder.substring(1, builder.length());
    }

}
