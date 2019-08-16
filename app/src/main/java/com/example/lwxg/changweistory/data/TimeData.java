package com.example.lwxg.changweistory.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TimeData extends SQLiteOpenHelper {
    static final String NAME = "timesc.db";
    static final int VERSION = 1;

    public TimeData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TimeData(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tb_users(time text)";
        db.execSQL(sql);
    }


    public String selectTime() {
        String sql = "select time from tb_users";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        String id = null;
        if (cursor.moveToNext())
            id = cursor.getString(0);
        cursor.close();
        return id;
    }

    public void add(String time) {
        String sql22 = "insert into tb_users(time) values (?)";
        SQLiteDatabase db2 = getWritableDatabase();
        db2.execSQL(sql22, new Object[]{ "da"});

        String sql1 = "delete from tb_users";
        SQLiteDatabase db1 = getWritableDatabase();
        db1.execSQL(sql1);


        String sql = "insert into tb_users(time) values (?)";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql, new Object[]{ time});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
