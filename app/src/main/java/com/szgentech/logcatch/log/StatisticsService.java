package com.szgentech.logcatch.log;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.List;

/**
 * 该Service拥有独立进程, 处理完任务后会自动退出
 * 只有主进程发生异常后，才会启动该Service来保存统计信息
 */
public class StatisticsService extends IntentService {

    public static final String EXTRA_KEY = "extra_key";
    private Logger logger = Logger.getLogger("StatisticsService");
    private final Gson gson;

    public StatisticsService() {
        this("StatisticsService");
    }

    public StatisticsService(String name) {
        super(name);
        gson = new GsonBuilder()
                .setPrettyPrinting()//自动换行和添加缩进
                .serializeNulls()//保留null的变量并将值设为null
                .disableHtmlEscaping()//不会对用于表示HTML标签的"<"和">"编码
                .setDateFormat("yyyy-MM-dd:HH-MM-SS")
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES)
                .create();
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            List<StatisticsEvent> events = intent.getParcelableArrayListExtra(EXTRA_KEY);
            logger.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n" + "StatisticsService-> onHandleIntent: "
                    + "\n" + events + "\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            StatisticsHelper.getInstance().recoveryLastEvents(events);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
