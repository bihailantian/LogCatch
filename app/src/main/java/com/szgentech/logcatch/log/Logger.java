package com.szgentech.logcatch.log;

import android.util.Log;


public class Logger {
    private static Logger logger;
    private String TAG;

    public Logger(String TAG) {
        this.TAG = TAG;
    }


    public static Logger getLogger(String tag) {
        if (logger == null) {
            synchronized (StatisticsHelper.class) {
                if (logger == null) {
                    logger = new Logger(tag);
                }
            }
        }
        return logger;
    }

    public void i(String msg){
        Log.i(TAG,msg);
    }

    public void d(String msg){
        Log.i(TAG,msg);
    }
}
