package com.szgentech.logcatch.log;

import android.os.Environment;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnionUtils {

    private static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    private static final String ROOT = "LogCatch";

    private UnionUtils() {
    }

    public static String getStatisticsCachePath() {
        return getRootPath() +  File.separator  + "log"+  File.separator+ "Statistics";
    }

    /**
     *
     * @param statisticsCachePath 目录
     * @param suffixName          后缀名
     * @return 文件目录
     */
    public static String createInnerCacheFileDependDate(String statisticsCachePath, String suffixName) {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD);
        String fileName = dateFormat.format(new Date());
        File file = new File(statisticsCachePath);
        if (!file.exists()) {
            file.mkdirs();
        }

        return statisticsCachePath + File.separator + fileName + suffixName;
    }

    public static String getCrashLogCachePath() {
        return getRootPath() +  File.separator  + "log"+  File.separator+ "Crash";
    }

    private static String getRootPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + ROOT ;
    }
}
