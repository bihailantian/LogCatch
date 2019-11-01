package com.szgentech.logcatch;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.szgentech.logcatch.log.MyCrashHandleCallback;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class MyApplication extends Application {
    public static final String BUGLY_APPID = "6266dd75de";
    private boolean isDebug = true;
    //log路径
    public static final String LOG_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "Live" + File.separator + "log" + File.separator;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        //存储未捕获的异常日志到本地
        CrashHandler.getInstance().init(this,LOG_PATH);


        // =====================================初始化Bugly==============================================
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程

        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //设置回调，存储异常日志到本地
        strategy.setCrashHandleCallback(new MyCrashHandleCallback());
        // 初始化Bugly
        CrashReport.initCrashReport(context, BUGLY_APPID, isDebug, strategy);
        // =====================================初始化Bugly==============================================


    }


    public static Context getContext() {
        return context;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

}
