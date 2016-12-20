package com.example.shin.fallapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.shin.fallapp.R.*;


public class fall extends ActionBarActivity {

    private TextView tvfall1, tvfall2;
    private Button btnSet, btnRecord, btnBlue, btnAbout, btnChangetype;
    MOD mHelper = new MOD(this);
    SQLiteDatabase mDB = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_fall);

        loadwidget();
        mDB = mHelper.getWritableDatabase();
        Cursor cursor = mDB.rawQuery("SELECT _ID, _MOD,_S  FROM MyTable", null);

        mDB.execSQL("INSERT INTO MyTable (_MOD,_S) VALUES('一般模式','1')");

        mDB.execSQL("delete from MyTable where _ID='2'");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Log.e("SQLiteDBTestingActivity", "_ID = " + cursor.getInt(0));
            Log.e("SQLiteDBTestingActivity", "_MOD = " + cursor.getString(1));
            Log.e("SQLiteDBTestingActivity", "_S = " + cursor.getString(2));
            tvfall2.setText(cursor.getString(1));

            cursor.moveToNext();
        }

        startManagingCursor(cursor);
        cursor.close();
        mDB.close();

        btnSet.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(fall.this, set.class);
                startActivity(intent);
                fall.this.finish();

            }
        });

        btnRecord.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(fall.this, record.class);
                startActivity(intent);
                fall.this.finish();
            }
        });

        btnBlue.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(fall.this, blue.class);
                startActivity(intent);
                fall.this.finish();
            }
        });

        btnAbout.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(fall.this, about.class);
                startActivity(intent);
                fall.this.finish();
            }
        });
        btnChangetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changetype();
            }
        });
    }

    private void loadwidget() {
        btnSet = (Button) findViewById(R.id.btnSet);
        btnRecord = (Button) findViewById(R.id.btnRecord);
        btnBlue = (Button) findViewById(R.id.btnBlue);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnChangetype = (Button)findViewById(id.btnChangetype);
        tvfall1 = (TextView) findViewById(R.id.tvfall1);
        tvfall2 = (TextView) findViewById(R.id.tvfall2);
    }
    public void changetype(){
        Dialog alertDialog = new AlertDialog.Builder(this).
                setTitle("模式").
                setPositiveButton("一般", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvfall2.setText("一般模式");
                        Toast.makeText(fall.this, "修改模式", Toast.LENGTH_SHORT).show();
                        mDB = mHelper.getWritableDatabase();
                        mDB.execSQL("Update MyTable  set  _MOD ='一般模式',_S ='1' where _ID='1'");

                        mDB.close();
                    }
                }).
                setNegativeButton("登山", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvfall2.setText("登山模式");
                        Toast.makeText(fall.this, "修改成功", Toast.LENGTH_SHORT).show();
                        mDB = mHelper.getWritableDatabase();
                        mDB.execSQL("Update MyTable  set  _MOD ='登山模式',_S ='2' where _ID='1'");

                        mDB.close();
                    }
                }).show();
}


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fall, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
