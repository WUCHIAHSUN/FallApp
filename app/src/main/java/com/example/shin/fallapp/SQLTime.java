package com.example.shin.fallapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2015/6/3.
 */
public class SQLTime extends SQLiteOpenHelper {
    final private static int _DB_VERSION = 5;
    final private static String _DB_DATABASE_NAME = "Time.db";
    public SQLTime(Context context) {
        super(context,_DB_DATABASE_NAME,null,_DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL( "CREATE TABLE MyTable (" +
                " _ID INTEGER PRIMARY KEY, " +
                " _DATETIME DATETIME NULL, "+
                " _LOCATION VARCHAR(50),"+
                " _LOCATION2 VARCHAR(50)"+
                ")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS MyTable");
        onCreate(db);
    }
}