package com.example.maxim.map;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class ServiceLocation extends Service {

    static boolean isActionService;

    private Thread t;
    private Intent intent;

    private final int LOCATION_PLAY = 0;
    private final int LOCATION_STOP = 1;
    private final String LOCATION_ACTION = "com.example.maxim.map";
    private final String TYPE = "TYPE";
    private final String LATITUDE = "LATITUDE";
    private final String LONGITUDE = "LONGITUDE";

    private LocationListener locationListener;
    private LocationManager locationManager;

    private String mName = "mName";
    private volatile Looper mServiceLooper;
    private volatile ServiceHandler  mServiceHandler ;

    public ServiceLocation() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "ServiceLocation In Create()", Toast.LENGTH_SHORT).show();

        intent = new Intent(LOCATION_ACTION);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        HandlerThread thread = new HandlerThread(mName);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
//        Runnable r = new Runnable() {
//            @Override
//            public void run() {
//                while (!isActionService) {
//                    locationManager.requestLocationUpdates(providerGPSStr, 1000, 2, locationListener, Looper.getMainLooper());
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                stopSelf();
//            }
//        };
//        t = new Thread(r);
//        t.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StringBuilder sb = new StringBuilder();
        String providerGPSStr = LocationManager.GPS_PROVIDER;
        String providerNetworkStr = LocationManager.NETWORK_PROVIDER;
        String providerPassiveStr = LocationManager.PASSIVE_PROVIDER;

        sb.append("\n providerGPSStr : \t" + providerGPSStr);
        sb.append("\n providerNetworkStr : \t" + providerNetworkStr);
        sb.append("\n providerPassiveStr : \t" + providerPassiveStr);
        sb.append("\n");

        boolean isGPSProviderEnabled = false;
        boolean isNeworkProviderEnabled = false;
        boolean isPassiveProviderEnabled = false;

        try {
            isGPSProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            sb.append("\n isGPSProviderEnabled : \t" + isGPSProviderEnabled);
        } catch (Exception e) {
            sb.append("\n isGPSProviderEnabled : \t" + e.getMessage());
        }

        try {
            isNeworkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            sb.append("\n isNeworkProviderEnabled : \t" + isNeworkProviderEnabled);
        } catch (Exception e) {
            sb.append("\n isNeworkProviderEnabled : \t" + e.getMessage());
        }

        try {
            isPassiveProviderEnabled = locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
            sb.append("\n isPassiveProviderEnabled : \t" + isPassiveProviderEnabled);
        } catch (Exception e) {
            sb.append("\n isPassiveProviderEnabled : \t" + e.getMessage());
        }

        if (!isGPSProviderEnabled) {
            Intent intentSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intentSetting.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intentSetting);
        }

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
//                Message msg = mServiceHandler.obtainMessage();
                            Message msg = new Message();
                Bundle data = new Bundle();
                data.putDouble(LATITUDE, location.getLatitude());
                data.putDouble(LONGITUDE, location.getLongitude());
                msg.setData(data);
                mServiceHandler.sendMessage(msg);

////                            intent.putExtra(TYPE, LOCATION_PLAY);
//                            intent.putExtra(LATITUDE, location.getLatitude());
//                            intent.putExtra(LONGITUDE, location.getLongitude());
//                            sendBroadcast(intent);

                String mess = "latitude :" + location.getLatitude() + " longitude : " +
                        location.getLongitude();
                Toast.makeText(getApplicationContext(), mess, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return 0;
        }
        locationManager.requestLocationUpdates(providerGPSStr, 0, 2, locationListener);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "ServiceLocation onDestroy()", Toast.LENGTH_SHORT).show();

        isActionService = true;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);

        mServiceLooper.quit();
//        if(intent!=null) {
//        Intent intent = new Intent(LOCATION_ACTION);
//        intent.putExtra(TYPE, LOCATION_STOP);
//        sendBroadcast(intent);

        Log.d("q", "ServiceLocation onDestroy()");
    }

    public class ServiceHandler extends Handler {

        public ServiceHandler (Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Bundle b = msg.getData();

            double latitude = b.getDouble(LATITUDE);
            double longitude = b.getDouble(LONGITUDE);

//            intent.putExtra(TYPE, LOCATION_PLAY);
            intent.putExtra(LATITUDE, latitude);
            intent.putExtra(LONGITUDE, longitude);
            sendBroadcast(intent);
        }
    }
}
