package com.retron.robotmqtt.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.retron.robotmqtt.utils.Content;

import java.util.ArrayList;

public class SqLiteOpenHelperUtils {
    private Context mContext;
    private TaskSqLite taskSqLite;
    private SQLiteDatabase sqLiteDatabase;
    private ArrayList<String> arrayList;


    public SqLiteOpenHelperUtils(Context mContext) {
        this.mContext = mContext;
        taskSqLite = new TaskSqLite(mContext, 1);
    }

    //地图名字
    public void saveMapName(String mapNameUuid, String db_dump_md5) {
        sqLiteDatabase = taskSqLite.getWritableDatabase();
        sqLiteDatabase.execSQL("insert into " + Content.MapNameDeatabase + "(" + Content.MapNameUuid + ", "  + Content.MapDumpMd5 + ") values ('" + mapNameUuid + "','" + db_dump_md5 + "')");
    }

    public Cursor searchMapName(String typeName, String typeString) {
        sqLiteDatabase = taskSqLite.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Content.MapNameDeatabase, new String[]{"_id", Content.MapNameUuid, Content.MapDumpMd5}, typeName + "=?", new String[]{typeString}, null, null, null);
        return cursor;
    }

    public void updateDumpMd5(String type, String oldMapUuid, String newdump) {
        sqLiteDatabase = taskSqLite.getWritableDatabase();
        Log.d("updateDumpMd5 : ", "update " + Content.MapNameDeatabase + " set " + Content.MapDumpMd5 + "='" + newdump + "' where " + type + "='" + oldMapUuid + "'");
        sqLiteDatabase.execSQL("update " + Content.MapNameDeatabase + " set " + Content.MapDumpMd5 + "='" + newdump + "' where " + type + "='" + oldMapUuid + "'");
    }

    public Cursor searchAllMapName() {
        sqLiteDatabase = taskSqLite.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(Content.MapNameDeatabase, new String[]{"_id", Content.MapNameUuid, Content.MapDumpMd5}, null, null, null, null, null);
        return cursor;
    }

    public void deleteMapName(String mapTaskName) {
        sqLiteDatabase = taskSqLite.getWritableDatabase();
        sqLiteDatabase.delete(Content.MapNameDeatabase, Content.MapNameUuid + "=?", new String[]{mapTaskName});
        close();
    }

    public void close() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }

    public void reset_Db(String tab_name) {
        sqLiteDatabase = taskSqLite.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from " + tab_name + "");
    }

}


