package com.szgentech.logcatch.log;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class StatisticsEvent implements Parcelable {

    public String action;
    public String time;
    public String desc;
    public HashMap<String, String> attach;

    public StatisticsEvent() {
    }

    protected StatisticsEvent(Parcel in) {
        action = in.readString();
        time = in.readString();
        desc = in.readString();
        attach = in.readHashMap(HashMap.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(action);
        dest.writeString(time);
        dest.writeString(desc);
        dest.writeMap(attach);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<StatisticsEvent> CREATOR = new Creator<StatisticsEvent>() {
        @Override
        public StatisticsEvent createFromParcel(Parcel in) {
            return new StatisticsEvent(in);
        }

        @Override
        public StatisticsEvent[] newArray(int size) {
            return new StatisticsEvent[size];
        }
    };

    @Override
    public String toString() {
        return "StatisticsEvent{" +
                "action='" + action + '\'' +
                ", time='" + time + '\'' +
                ", desc='" + desc + '\'' +
                ", attach=" + attach +
                '}';
    }
}
