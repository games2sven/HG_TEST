package com.highgreat.education.utils;

import java.io.File;
import java.util.Comparator;

/**
 * Do Good App
 * 项目名称：Camera
 * 类描述： FileList  按时间排序
 * 创建人：mac-likh
 * 创建时间：16/1/25 09:50
 * 修改人：mac-likh
 * 修改时间：16/1/25 09:50
 * 修改备注：
 */

public class YMComparator implements Comparator<File> {

    @Override
    public int compare(File lhs, File rhs) {
        String time1 = TimeUtil.paserTimeToHs(lhs.lastModified());
        String time2 = TimeUtil.paserTimeToHs(rhs.lastModified());
        return time2.compareTo(time1);
    }
}

