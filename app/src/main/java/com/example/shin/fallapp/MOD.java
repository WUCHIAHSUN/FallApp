package com.example.shin.fallapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2015/6/3.
 */
public class MOD extends SQLiteOpenHelper {
    final private static int _DB_VERSION = 1;
    final private static String _DB_DATABASE_NAME = "MOD.db";
    public MOD(Context context) {
        super(context,_DB_DATABASE_NAME,null,_DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL( "CREATE TABLE MyTable (" +
                " _ID INTEGER PRIMARY KEY, " +
                " _MOD VARCHAR(50),"+
                "_S VARCHAR(10)"+
                ")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS MyTable");
        onCreate(db);
    }
}