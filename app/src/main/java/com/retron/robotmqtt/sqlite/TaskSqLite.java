package com.retron.robotmqtt.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.retron.robotmqtt.utils.Content;

public class TaskSqLite extends SQLiteOpenHelper {

    public TaskSqLite(@Nullable Context context, int version) {
        super(context,
                Content.dbName,
                null,
                2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Content.MapNameDeatabase + "(_id integer primary key autoincrement, " + Content.MapNameUuid + " varchar(200), " + Content.MapDumpMd5 + " varchar(200))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
