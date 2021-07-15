package com.retron.robotmqtt.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
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
        public static long stringToTimestamp(String dateString) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            try{
                date = dateFormat.parse(dateString);
            } catch(ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return date.getTime();
    }

}
