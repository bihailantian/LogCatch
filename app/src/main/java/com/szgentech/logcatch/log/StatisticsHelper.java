package com.szgentech.logcatch.log;

import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.szgentech.logcatch.MyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisticsHelper {

    private static final String DATE_FORMAT_YYYY_MM_DD_HH_MM_SS2 = "yyyy-MM-dd HH:mm:ss" ;
    private static StatisticsHelper sInstance;
    private Logger logger = Logger.getLogger("StatisticsHelper");
    private final ArrayList<StatisticsEvent> statisticsEvents = new ArrayList<>();
    private final HandlerThread handlerThread;
    private final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYY_MM_DD_HH_MM_SS2);
    private final StringBuilder sb = new StringBuilder();
    private final Gson gson;
    private final Handler handler;
    public static final String ACTION_CRASH = "action_crash";
    private boolean isCrashRecord;


    private StatisticsHelper() {
        handlerThread = new HandlerThread("StatisticsHelper");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        gson = new GsonBuilder()
                .setPrettyPrinting()//自动换行和添加缩进
                .serializeNulls()//保留null的变量并将值设为null
                .disableHtmlEscaping()//不会对用于表示HTML标签的"<"和">"编码
                .setDateFormat("yyyy-MM-dd:HH-MM-SS")
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES)
                .create();
    }

    public static StatisticsHelper getInstance() {
        if (sInstance == null) {
            synchronized (StatisticsHelper.class) {
                if (sInstance == null) {
                    sInstance = new StatisticsHelper();
                }
            }
        }
        return sInstance;
    }

    public void addEvent(String action) {
        StatisticsEvent event = new StatisticsEvent();
        event.action = action;
        addEvent(event);
    }

    public void addEvent(String action, String desc) {
        StatisticsEvent event = new StatisticsEvent();
        event.action = action;
        event.desc = desc;
        addEvent(event);
    }

    public void addEvent(StatisticsEvent event) {
        addEvent(event, false);
    }

    /**
     * @param event            事件
     * @param writeImmediately 立刻写入日志文件
     */
    public void addEvent(StatisticsEvent event, boolean writeImmediately) {
        logger.i("addEvent, writeImmediately=" + writeImmediately + " action=" + event.action);

        event.time = dateFormat.format(new Date());
        statisticsEvents.add(event);

        //如果是异常Action, 则启动独立进程service进行保存
        if (ACTION_CRASH.equals(event.action)) {
            Intent service = new Intent(MyApplication.getContext(), StatisticsService.class);
            service.putParcelableArrayListExtra(StatisticsService.EXTRA_KEY, statisticsEvents);
            MyApplication.getContext().startService(service);
            statisticsEvents.clear();
            return;
        }

        //达100条数据或者特殊要求，写入文件一次
        if (statisticsEvents.size() >= 10 || writeImmediately) {
            List<StatisticsEvent> temp = new ArrayList<>();
            temp.addAll(statisticsEvents);
            writeFileRunnable.setArgs(temp);
            handler.post(writeFileRunnable);
            statisticsEvents.clear();
        }
    }

    /**
     * 批量恢复事件记录
     *
     * @param events
     */
    public void recoveryLastEvents(List<StatisticsEvent> events) {
        logger.i("recoveryLastEvents");
        isCrashRecord = true;
        statisticsEvents.addAll(events);
        List<StatisticsEvent> temp = new ArrayList<>();
        temp.addAll(statisticsEvents);
        statisticsEvents.clear();
        writeFileRunnable.setArgs(temp);
        handler.post(writeFileRunnable);
    }

    private final ArgsRunnable<List<StatisticsEvent>> writeFileRunnable = new ArgsRunnable<List<StatisticsEvent>>() {
        @Override
        public void run() {
            try {
                sb.delete(0, sb.length());
                sb.append(gson.toJson(getArgs()));

                String path;
                if (!isCrashRecord) {
                    path = UnionUtils.createInnerCacheFileDependDate(UnionUtils.getStatisticsCachePath(), ".txt");
                } else {
                    path = UnionUtils.createInnerCacheFileDependDate(UnionUtils.getCrashLogCachePath(), ".txt");
                }
                logger.d("path=="+path);
                addTxtToFileBuffered(new File(path), sb.toString());
            } catch (Exception e) {
                logger.d("writeFileRunnable error: " + Log.getStackTraceString(e));
            }
        }
    };

    /**
     * 往文件末端追加字符串
     *
     * @param file
     * @param content
     */
    private void addTxtToFileBuffered(File file, String content) {
        logger.d("addEvent2File\n" + content);
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out.newLine();
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
