package com.example.shin.fallapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Shin on 2015/4/24.
 */

public class blue extends Activity {

    private Button  btnBlueOpen, btInput;
    private ImageButton btnBlueBack;
    private ListView List;
    private TextView myLabel;
    private ArrayList<String> items;
    private ArrayAdapter<String> mArrayAdapter;
    private final static int REQUEST_ENABLE_BT = 1;
    private final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//獲得當前的藍芽
    private  BluetoothSocket mmSocket;
    private  InputStream mmInStream;
    Handler mHandler = new Handler();
    SQLTime mHelper = new SQLTime(this);
    SitesDBHlp mHelper1 = new SitesDBHlp(this);
    SQLiteDatabase mDB=null, mDB1=null;
    private String best,loca,locb;
    private LocationManager locationmgr;
    private final static String CALL = "android.intent.action.CALL";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutblue);
        loadWidget();



        items = new ArrayList<String>();
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

        locationmgr = (LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria c = new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        best = locationmgr.getBestProvider(c, true);
        if(best != null) {
            Location loc = locationmgr.getLastKnownLocation(best);
            showLocation(loc);
        }

        btnBlueBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(blue.this, fall.class);
                startActivity(intent);
                blue.this.finish();
            }
        });

        btnBlueOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                // If there are paired devices
                if (pairedDevices.size() > 0) {
                    // Loop through paired devices
                    for (BluetoothDevice device : pairedDevices) {
                        // Add the name and address to an array adapter to show in a ListView
                        mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                        List.setAdapter(mArrayAdapter);
                    }
                }
                // Create a BroadcastReceiver for ACTION_FOUND
                BroadcastReceiver mReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        // When discovery finds a device
                        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                            // Get the BluetoothDevice object from the Intent
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            // Add the name and address to an array adapter to show in a ListView
                            mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                            List.setAdapter(mArrayAdapter);
                            if (device.getName().equals("HC-06")) {
                                try {
                                    // 一進來一定要停止搜尋
                                    mBluetoothAdapter.cancelDiscovery();
                                    // 連結到該裝置
                                    mmSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
                                    mmSocket.connect();
                                } catch (IOException e) {
                                }
                            }
                        }
                    }
                };
                // Register the BroadcastReceiver
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
                mBluetoothAdapter.startDiscovery();
                //mBluetoothAdapter.cancelDiscovery();
                // 判斷那個裝置是不是你要連結的裝置，根據藍芽裝置名稱判斷
            }
        });

        btInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final byte[] thedata = new byte[2];
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        while (true) {
                            try {
                                Message msg = new Message();
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                                Thread.sleep(250);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();

                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 1:
                                try {
                                    mmInStream = mmSocket.getInputStream();
                                    BufferedInputStream ins = new BufferedInputStream(mmInStream);
                                    ins.read(thedata);
                                    myLabel.setText(String.valueOf(thedata[0]));
                                    if (myLabel.getText().length() > 1) {
                                        record();
                                        phonecall();
                                    }
                                    break;
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                        }
                        super.handleMessage(msg);
                    }
                };
            }
        });
    }

    private void record(){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cc = Calendar.getInstance();
        String str = df.format(cc.getTime());
        mDB = mHelper.getWritableDatabase();
        mDB.execSQL("INSERT INTO MyTable (_DATETIME,_LOCATION,_LOCATION2) VALUES('" + str + "','" + loca + "','" + locb + "')");
        mDB.close();
    }

    private void phonecall(){
        //測試
        mDB1 = mHelper1.getWritableDatabase();
        Cursor cursor = mDB1.rawQuery("SELECT _ID, _NAME, _OLD, _ADDRESS, _PHONE, _PHONE2, _PHONE3  FROM MyTable", null);
        cursor.moveToFirst();      

        if(cursor.getString(4) != null)
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(cursor.getString(4), null, "請注意，此位用戶跌倒了！",PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0),null);
            Intent call = new Intent(CALL, Uri.parse("tel:" + cursor.getString(5)));
            startActivity(call);
        }
        if(cursor.getString(5) != null)
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(cursor.getString(4), null, "請注意，此位用戶跌倒了！",PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0),null);
            Intent call = new Intent(CALL, Uri.parse("tel:" + cursor.getString(5)));
            startActivity(call);
        }
        if(cursor.getString(6) != null)
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(cursor.getString(4), null, "請注意，此位用戶跌倒了！",PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), 0),null);
            Intent call = new Intent(CALL, Uri.parse("tel:" + cursor.getString(6)));
            startActivity(call);
        }
        cursor.close();
        mDB1.close();
    }

    private void showLocation (Location loc) {
        if (loc != null) {
            loca = String.valueOf(loc.getLongitude());
            locb =  String.valueOf(loc.getLatitude());
        }
    }

    private void loadWidget() {
        btnBlueBack = (ImageButton) findViewById(R.id.btnBlueBack);
        btnBlueOpen = (Button) findViewById(R.id.btnBlueOpen);
        btInput = (Button)findViewById(R.id.btInput);
        List = (ListView)findViewById(R.id.listView);
        myLabel = (TextView)findViewById(R.id.textView2);
    }
}