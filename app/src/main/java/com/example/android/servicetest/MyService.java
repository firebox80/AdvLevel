package com.example.android.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class MyService extends Service {
//    public MyService() {
//    }
    static boolean isServiseWork;
    static boolean isStop;

    private OurHandler handler;
    private static MyThread t;

//    private Intent intent;
    private final static String ACTION_PROGRESSBAR = "com.example.android.servicetest";
    private static final String TYPE = "type";
    private static final int ID_ACTION_START = 0;
    private static final int ID_ACTION_STOP = 1;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "In Create()", Toast.LENGTH_SHORT).show();

        handler = new OurHandler();
        isServiseWork = true;

        Runnable r = new Runnable() {
            @Override
            public void run() {
                int counter = 0;

                while (!isStop) {
                    Message m = new Message();
                    m.obj = "Counter: " + counter;
                    handler.sendMessage(m);
//                    Toast.makeText(getApplicationContext(), "counter"+ counter, Toast.LENGTH_SHORT).show();
                    counter++;
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                stopSelf();
            }
        };
        t = new MyThread(r);
        t.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand" + startId, Toast.LENGTH_SHORT).show();

        if(startId == 2){
            Intent intent1 = new Intent(ACTION_PROGRESSBAR);
            intent1.putExtra(TYPE, ID_ACTION_START);
            intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendBroadcast(intent1);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
        isServiseWork = false;

//        if(intent!=null) {
            Intent intent = new Intent(ACTION_PROGRESSBAR);
            intent.putExtra(TYPE, ID_ACTION_STOP);
            sendBroadcast(intent);
//        }
    }

    public class OurHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            String mess = (String)msg.obj;
            Toast.makeText(getApplicationContext(), "counter"+ mess, Toast.LENGTH_SHORT).show();
        }
    }

    public class MyThread extends Thread {

        public MyThread(Runnable runnable) {
            super(runnable);
        }
    }
}
