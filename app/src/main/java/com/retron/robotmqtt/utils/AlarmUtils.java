package com.retron.robotmqtt.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AlarmUtils {

    private Context mContext;
    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    public AlarmUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取指定时间对应的时间
     *
     * @param time "HH:mm:ss"
     * @return
     */
    public String getTimeMillis(long time) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(new Date(time));
    }

    public String getTimeYear(long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return dateFormat.format(new Date(time));
    }

    public String getTime(long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(time));
    }

    public String getTimeMonth(long time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        return dateFormat.format(new Date(time));
    }

    //时间转为时间戳
    public long stringToTimestamp(String time){
        long times = 0;
        try {
            times = (long) ((Timestamp.valueOf(time).getTime())/1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(times==0){
            System.out.println("String转10位时间戳失败");
        }
        return times;

    }

}
