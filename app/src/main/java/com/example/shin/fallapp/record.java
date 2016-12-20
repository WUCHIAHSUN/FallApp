package com.example.shin.fallapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Shin on 2015/4/24.
 */
public class record extends Activity implements LocationListener {

    private LocationManager locationmgr;
    private String best,loca,locb;
    private ListView lvRecord;
    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private Button  button,btnDelete;
    private ImageButton btnRecordBack;
    SQLTime mHelper = new SQLTime(this);
    SQLiteDatabase mDB=null;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutrecord);

        loadwidget();
        locationmgr = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        best = locationmgr.getBestProvider(c, true);
        if(best != null) {
            Location loc = locationmgr.getLastKnownLocation(best);
            showLocation(loc);
        }

        items = new ArrayList<String>();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        mDB = mHelper.getWritableDatabase();
        Cursor cursor = mDB.rawQuery("SELECT _ID,_DATETIME,_LOCATION,_LOCATION2  FROM MyTable", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Log.e("SQLiteDBTestingActivity", "_ID = " + cursor.getInt(0));
            Log.e("SQLiteDBTestingActivity","_DATETIME = "+cursor.getString(1));
            Log.e("SQLiteDBTestingActivity","_LOCATION = "+cursor.getString(2));
            itemsAdapter.add((cursor.getString(1))+"\n經度:"+(cursor.getString(2))+"\n緯度:"+(cursor.getString(3)));
            cursor.moveToNext();
        }
        startManagingCursor(cursor);
        cursor.close();
        mDB.close();

        lvRecord.setAdapter(itemsAdapter);


        btnRecordBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(record.this,fall.class);
                startActivity(intent);
                record.this.finish();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDB = mHelper.getWritableDatabase();
                mDB.execSQL("delete from MyTable where _ID>='1'");
                lvRecord.setAdapter(null);
            }
        });
    }
    private void showLocation (Location loc) {
        if (loc != null) {
            loca = String.valueOf(loc.getLongitude());
            locb =  String.valueOf(loc.getLatitude());
        }
    }

    private void loadwidget(){
        btnRecordBack = (ImageButton)findViewById(R.id.btnRecordBack);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        lvRecord = (ListView)findViewById(R.id.lvRecord);
    }

    @Override
    protected void onResume(){
        super.onResume();
        locationmgr.requestLocationUpdates(best, 30000, 1, this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        locationmgr.removeUpdates(this);
    }
    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
