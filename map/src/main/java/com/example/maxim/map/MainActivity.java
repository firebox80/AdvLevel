package com.example.maxim.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private final int LOCATION_PLAY = 0;
    private final int LOCATION_STOP = 1;
    private final String TYPE = "TYPE";
    private final String LOCATION_ACTION = "com.example.maxim.map";
    private final String LATITUDE = "LATITUDE";
    private final String LONGITUDE = "LONGITUDE";

    private CheckBox chLocation;
    private ImageView ivMyLocation;
    private ImageView ivMap;

    private Intent intentService;

    private Thread t;
    private final int threadSleep = 100;

    private ReceiverLocation myReceiverLocation;
    private IntentFilter filterRecLocation;
    private boolean isActionReceiver = true;

    private final double LongitudeX0 = 30.639548;
    private final double LatitudeY0 = 50.423451;
    private final double LatitudeY1 = 50.418159;
    private final double LongitudeX1 = 30.647912;

    private int widthIvMap;
    private int heightIvMap;

    private double currentLongitudeX;
    private double currentLatitudeY;

    private double XdeltaLongitude;
    private double YdeltaLatitude;

    private double screenX;
    private double screenY;
    private double weightX;
    private double weightY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myReceiverLocation = new ReceiverLocation();
        filterRecLocation = new IntentFilter(LOCATION_ACTION);
        registerReceiver(myReceiverLocation, filterRecLocation);

        intentService = new Intent(this, ServiceLocation.class);

        ivMyLocation = (ImageView) findViewById(R.id.myLocation);
        ivMap = (ImageView) findViewById(R.id.map);

        chLocation = (CheckBox) findViewById(R.id.checkBox);
        chLocation.setOnCheckedChangeListener(this);
    }

    private void getWeightX() {
        widthIvMap = ivMap.getWidth();
        XdeltaLongitude = LongitudeX1 - LongitudeX0;
        weightX = widthIvMap/XdeltaLongitude;
    }

    private void getWeightY() {
        heightIvMap = ivMap.getHeight();
        YdeltaLatitude = LatitudeY0 - LatitudeY1;
        weightY = heightIvMap/YdeltaLatitude;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked) {
            getWeightX();
            getWeightY();

            Log.d("q", String.valueOf(isChecked));
//            Toast.makeText(getApplicationContext(), String.valueOf(isChecked), Toast.LENGTH_SHORT).show();

            startService(intentService);
        } else {
            Log.d("q", String.valueOf(isChecked));
//            Toast.makeText(getApplicationContext(), String.valueOf(isChecked), Toast.LENGTH_SHORT).show();

            stopService(intentService);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(myReceiverLocation);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        stopService(intentService);
    }

    public class ReceiverLocation extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(getApplicationContext(), "ReceiverLocation onReceive()", Toast.LENGTH_SHORT).show();

//            int type = intent.getIntExtra(TYPE, LOCATION_STOP);

            currentLatitudeY = intent.getDoubleExtra(LATITUDE, 0);
            currentLongitudeX = intent.getDoubleExtra(LONGITUDE, 0);

//            getWeightX();
//            getWeightY();

            screenX = (currentLongitudeX - LongitudeX0)* weightX;
            screenY = (LatitudeY0 - currentLatitudeY)* weightY;

            ivMyLocation.setX((float) (screenX));
            ivMyLocation.setY((float) (screenY));

//            switch (type) {
//                case LOCATION_PLAY:
//                    Runnable r = new Runnable() {
//                        @Override
//                        public void run() {
//
//                            try {
//                                Thread.sleep(threadSleep);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                            while (isActionReceiver) {
//                                try {
//                                    Thread.sleep(threadSleep);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                    };
//
//                    if(!(t!=null && t.isAlive()) && isActionReceiver) {
//                        t = new Thread(r);
//                        t.start();
//                    }
//                    break;
//                case LOCATION_STOP:
//                    isActionReceiver = false;
//                    break;
//            }
        }
    }

}
