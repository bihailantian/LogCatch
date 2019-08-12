package com.szgentech.logcatch;

import android.app.Application;
import android.os.Environment;

import java.io.File;


public class MyApplication extends Application {

    //log路径
    public static final String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Live" + File.separator + "log" + File.separator;

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this,LOG_PATH);
    }
}
