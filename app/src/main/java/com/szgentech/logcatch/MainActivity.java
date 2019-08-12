package com.szgentech.logcatch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,MyApplication.LOG_PATH);
//        CrashHandler.getInstance().init(this,LOG_PATH);

//        Log.d(TAG, Build.CPU_ABI);
//        Log.d(TAG, Arrays.toString(Build.SUPPORTED_ABIS));
//        Log.d(TAG, Build.MODEL );
    }

    public void getLog(View view) {
//        try {
            int sum = 2 / 0;
//        } catch (Exception e) {
//            Log.d(TAG,"除以零");
//            e.printStackTrace();
//        }
    }
}
