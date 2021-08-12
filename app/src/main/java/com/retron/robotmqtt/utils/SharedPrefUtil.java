package com.retron.robotmqtt.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefUtil {

    private static SharedPrefUtil sharedPrefUtil;
    private static final String SpName = "SpName";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private SharedPrefUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(SpName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * 获取唯一的instance
     */
    public static SharedPrefUtil getInstance(Context context) {
        if (sharedPrefUtil == null) {
            synchronized (SharedPrefUtil.class) {
                if (sharedPrefUtil == null) {
                    sharedPrefUtil = new SharedPrefUtil(context);
                }
            }
        }
        return sharedPrefUtil;
    }

    //历史任务时间
    public void setHistoryTime(String key, long time) {
        editor.putLong(key, time);
        editor.commit();
    }

    public long getHistoryTime(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    //token
    public void setToken(String key, String time) {
        editor.putString(key, time);
        editor.commit();
    }

    public String getToken(String key) {
        return sharedPreferences.getString(key, "provision");
    }



    /**
     * 清除所有数据
     */
    public void deleteAll(){
        editor.clear();
        editor.commit();
    }

    public void delete(String key){
        editor.remove(key);
        editor.commit();
    }

}
