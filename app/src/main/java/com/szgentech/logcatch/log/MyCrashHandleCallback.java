package com.szgentech.logcatch.log;

import android.util.Log;

import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

public class MyCrashHandleCallback extends CrashReport.CrashHandleCallback {

    private static final String TAG = MyCrashHandleCallback.class.getSimpleName();

    public MyCrashHandleCallback() {
        super();
    }

    @Override
    public synchronized Map<String, String> onCrashHandleStart(int i, String s, String s1, String desc) {
        Log.d(TAG, "=================onCrashHandleStart=================");
        Log.d(TAG, i + "");
        Log.d(TAG, s);
        Log.d(TAG, s1);
        Log.d(TAG, desc);
        Log.d(TAG, "=================onCrashHandleStart=================");

        StatisticsEvent event = new StatisticsEvent();
        event.action = StatisticsHelper.ACTION_CRASH;
        event.desc =  desc;

        //event.attach = (HashMap<String, String>) super.onCrashHandleStart(i, s, s1, desc);
        StatisticsHelper.getInstance().addEvent(StatisticsHelper.ACTION_CRASH,desc);

        return super.onCrashHandleStart(i, s, s1, desc);
    }

//    @Override
//    public synchronized byte[] onCrashHandleStart2GetExtraDatas(int i, String s, String s1, String s2) {
//        Log.d(TAG, "=================onCrashHandleStart2GetExtraDatas=================");
//        Log.d(TAG, i + "");
//        Log.d(TAG, s);
//        Log.d(TAG, s1);
//        Log.d(TAG, s2);
//        Log.d(TAG, "=================onCrashHandleStart2GetExtraDatas=================");
//        return super.onCrashHandleStart2GetExtraDatas(i, s, s1, s2);
//    }
}
