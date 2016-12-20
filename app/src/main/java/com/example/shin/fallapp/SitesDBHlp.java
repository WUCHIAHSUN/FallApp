package com.example.shin.fallapp;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by user on 2015/5/31.
 */
public class SitesDBHlp extends SQLiteOpenHelper {
    final private static int _DB_VERSION = 2;
    final private static String _DB_DATABASE_NAME = "MyDatabases.db";
    public SitesDBHlp(Context context) {
        super(context,_DB_DATABASE_NAME,null,_DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE MyTable (" +
                        " _ID INTEGER PRIMARY KEY, " +
                        " _NAME VARCHAR(50), " +
                        " _OLD VARCHAR(10), " +
                        " _ADDRESS VARCHAR(200), " +
                        " _PHONE VARCHAR(50), " +
                        " _PHONE2 VARCHAR(50), " +
                        " _PHONE3 VARCHAR(50)" +
                        ")");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS MyTable");
        onCreate(db);
    }
}
