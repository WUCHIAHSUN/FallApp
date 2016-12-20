package com.example.shin.fallapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class set extends Activity implements LocationListener{
    private LocationManager locationmgr;
    private String best;
    private TextView longitude_txt,latitude_txt;
    private Button button2, button3;
    private ImageButton btnSetBack,btnChange;
    private EditText edName, edOld, edAddress, edPhone, edPhone2, edPhone3;
    SitesDBHlp mHelper = new SitesDBHlp(this);
    SQLiteDatabase mDB = null;
    private final static String CALL = "android.intent.action.CALL";


    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.layoutset);
        loadWidget();
        locationmgr = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        best = locationmgr.getBestProvider(c, true);
        if(best != null) {
            Location loc = locationmgr.getLastKnownLocation(best);
            showLocation(loc);
        }

        mDB = mHelper.getWritableDatabase();
        Cursor cursor = mDB.rawQuery("SELECT _ID, _NAME, _OLD, _ADDRESS, _PHONE, _PHONE2, _PHONE3  FROM MyTable", null);

        mDB.execSQL("INSERT INTO MyTable (_NAME, _OLD, _ADDRESS, _PHONE, _PHONE2, _PHONE3) VALUES(' ',' ',' ',' ',' ',' ')");

        mDB.execSQL("delete from MyTable where _ID='2'");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.e("SQLiteDBTestingActivity", "_ID = " + cursor.getInt(0));
            Log.e("SQLiteDBTestingActivity", "_DATA = " + cursor.getString(1));
            Log.e("SQLiteDBTestingActivity", "_DATETIME = " + cursor.getString(2));
            edName.setText(cursor.getString(1));
            edOld.setText(cursor.getString(2));
            edAddress.setText(cursor.getString(3));
            edPhone.setText(cursor.getString(4));
            edPhone2.setText(cursor.getString(5));
            edPhone3.setText(cursor.getString(6));
            cursor.moveToNext();
        }

        startManagingCursor(cursor);
        cursor.close();
        mDB.close();


        btnSetBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(set.this, fall.class);
                startActivity(intent);
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeItem();
            }
        });
    }


    private void showLocation (Location loc) {
        if (loc != null) {
            longitude_txt.setText("經度:" + String.valueOf(loc.getLongitude()));
            latitude_txt.setText("緯度:"+String.valueOf(loc.getLatitude()));
        }
   /*else {
            Toast.makeText(this, "無法定位座標", Toast.LENGTH_LONG).show();
        }*/
    }






    private void loadWidget() {
        btnSetBack = (ImageButton) findViewById(R.id.btnSetBack);
        btnChange = (ImageButton) findViewById(R.id.btnChange);

        edName = (EditText) findViewById(R.id.edName);
        edOld = (EditText) findViewById(R.id.edOld);
        edAddress = (EditText) findViewById(R.id.edAddress);
        edPhone = (EditText) findViewById(R.id.edPhone);
        edPhone2 = (EditText) findViewById(R.id.edPhone2);
        edPhone3 = (EditText) findViewById(R.id.edPhone3);

        longitude_txt = (TextView) findViewById(R.id.textView3);
        latitude_txt = (TextView) findViewById(R.id.textView4);
    }

    private void changeItem() {
        // Insert by raw SQL
        String name = edName.getText().toString();
        String old = edOld.getText().toString();
        String address = edAddress.getText().toString();
        String phone = edPhone.getText().toString();
        String phone2 = edPhone2.getText().toString();
        String phone3 = edPhone3.getText().toString();
        mDB = mHelper.getWritableDatabase();

        mDB.execSQL("Update MyTable  set  _NAME = '" + name + "',  _OLD = '" + old +
                "',  _ADDRESS = '" + address + "',  _PHONE = '" + phone +
                "',  _PHONE2 = '" + phone2 + "',  _PHONE3 = '" + phone3 + "' where _ID='1'");

        mDB.close();
    }

    @Override
    protected void onResume(){
        super.onResume();
        locationmgr.requestLocationUpdates(best, 1000, 1, this);
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
