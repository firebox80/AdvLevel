package com.example.maxim.socket;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawingView dv ;
    private Paint mPaint;
    private static final CharSequence START_SERVER = "START_SERVER";
    private static final CharSequence START_CLIENT = "START_CLIENT";
    private static final CharSequence IP = "IP";
    private static final int MENU_ITEM_START_SERVER = 1;
    private static final int MENU_ITEM_START_CLIENT = 2;
    private static final int MENU_ITEM_IP = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPaint = new Paint();
        dv = new DrawingView(this, mPaint);
        setContentView(dv);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, MENU_ITEM_START_SERVER, 0, START_SERVER);
        menu.add(0, MENU_ITEM_START_CLIENT, 0, START_CLIENT);
        menu.add(0, MENU_ITEM_IP, 0, IP);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ITEM_START_SERVER:
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        new Server().startServer();
                    }
                }).start();
                break;
            case MENU_ITEM_START_CLIENT:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new Client().startClient();
                    }
                }).start();

                break;
            case MENU_ITEM_IP:
                try {
                    List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                    if (interfaces == null) {
                        Log.d("q", "null");
                    }
                    for (NetworkInterface intf : interfaces) {
                        List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                        for (InetAddress addr : addrs) {
                            if (!addr.isLoopbackAddress()) {
                                String sAddr = addr.getHostAddress();
                                Log.d("q", sAddr);
                                //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                                boolean isIPv4 = sAddr.indexOf(':')<0;

                                if (true) {
                                    if (isIPv4)
                                        Toast.makeText(this, sAddr, Toast.LENGTH_LONG).show();
                                } else {
                                    if (!isIPv4) {
                                        int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                        Toast.makeText(this,  delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) { }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}