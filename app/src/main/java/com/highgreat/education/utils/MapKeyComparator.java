package com.highgreat.education.utils;

import java.util.Comparator;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述：
 * 创建人：mac-likh
 * 创建时间：16/1/27 10:26
 * 修改人：mac-likh
 * 修改时间：16/1/27 10:26
 * 修改备注：
 */
public class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        if (str1!=null&&str2!=null)
        return str2.compareTo(str1);
        else
            return 0;
    }

}
