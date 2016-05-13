package com.example.maxim.quicksearch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class ServiceFind extends Service {

    private String pathParent = "/mnt/shared/SD_CARD";

    private final String SEARCH_END = "SEARCH_END";
    private final String FILE_QUANTITY = "FILE_QUANTITY";
    private final String FILE_FOUND = "FILE_FOUND";
    private final String LIST_FILE = "listDataFile";

    static boolean isServise;
    static boolean isStop = true;
    static boolean isActivity;

    private int counter;
    private Intent intentBroadcast;
    private Intent intentNotifiedActivity;

    private PendingIntent pi;
    private NotificationManager nm;
    private Notification notif;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private Runnable r;
    private MyThread t;

    private String fileFind;
    private Vibrator v;
    private Uri notification;
    private Ringtone ring;

    private ArrayList<Data> listDataFile = new ArrayList<Data>();

//    private String backItem = "..";
//    private FileFinder fileFinder;
//    private File file;
//    private ArrayList<String> listFile = new ArrayList<String>();
//    private ArrayList<String> listDirectory = new ArrayList<String>();
//    private ArrayList<String> listResultFind = new ArrayList<String>();
//    private ArrayList<String> listFileStart = new ArrayList<String>();
//    private ArrayList<String> listFileContains = new ArrayList<String>();
//    private FileFilter filterDirectory;
//    private FileFilter filterFile;
//    private FilenameFilter filenameFilterStart;
//    private FilenameFilter filenameFilterContains;
//    private FilenameFilter filenameFilterEnd;
//    private String fileNameToSearch;
//    private List<String> result = new ArrayList<String>();
//    private List result = new ArrayList();
//    private ArrayList<Data> testDataList = new ArrayList<>();


//    public ServiceFind() { }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "In Create()", Toast.LENGTH_SHORT).show();

        isServise = true;

        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        intentNotifiedActivity = new Intent(this, NotifiedActivity.class);

//        notif = null;

        sp = getSharedPreferences("my_prefs", MODE_PRIVATE);
        editor = sp.edit();

        intentBroadcast = new Intent(MainActivity.SEARCH_RESULT);

        v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ring = RingtoneManager.getRingtone(getApplicationContext(), notification);

        r = new Runnable() {
            @Override
            public void run() {

                while (isStop) {

                    findFile(new File(pathParent), fileFind);

                    isStop = false;

                    intentBroadcast.putExtra(SEARCH_END, isStop);
                    intentBroadcast.putExtra(LIST_FILE, listDataFile);
                    sendBroadcast(intentBroadcast);

                    if (isActivity && nm != null) {
                        nm.cancel(1);
                    }

                    try {
                        ring.play();
                    } catch (Exception e) {}

                }
                stopSelf();
            }
        };
    }

    public void addListData (File valuefile){
        String pathFile = valuefile.getParent();
        String nameFile = valuefile.getName();
        int avatarFile = R.drawable.fileicon_bg;

            switch (valuefile.getAbsolutePath().substring(valuefile.getAbsolutePath().lastIndexOf("."))){
                case ".zip" :
                    avatarFile = R.drawable.compressed;
                    break;
                case ".css" :
                    avatarFile = R.drawable.css;
                    break;
                case ".java" :
                    avatarFile = R.drawable.developer;
                    break;
                case ".xlsx" :
                    avatarFile = R.drawable.excel;
                    break;
                case ".swf" :
                    avatarFile = R.drawable.flash;
                    break;
                case ".html" :
                    avatarFile = R.drawable.html;
                    break;
                case ".ai" :
                    avatarFile = R.drawable.illustrator;
                    break;
                case ".bmp" :
                    avatarFile = R.drawable.image;
                    break;
                case ".key" :
                    avatarFile = R.drawable.keynote;
                    break;
                case ".avi" :
                    avatarFile = R.drawable.movie;
                    break;
                case ".mp3" :
                    avatarFile = R.drawable.music;
                    break;
                case ".pdf" :
                    avatarFile = R.drawable.pdf;
                    break;
                case ".psd" :
                    avatarFile = R.drawable.photoshop;
                    break;
                case ".ppt" :
                    avatarFile = R.drawable.powerpoint;
                    break;
                case ".txt" :
                    avatarFile = R.drawable.text;
                    break;
                case ".doc" :
                    avatarFile = R.drawable.word;
                    break;
            }
        listDataFile.add(new Data(pathFile, nameFile, avatarFile));
    }

    public void findFile(File file, String name) {
        File[] list = file.listFiles();
        if (list != null) {
            for (File fil : list) {
                if (fil.isDirectory()) {
                    Log.d("z", fil.getAbsolutePath());
                    findFile(fil, name);
                } else if (fil.getName().toLowerCase().contains(name.toLowerCase()) && !fil.getName().toLowerCase().endsWith("." + name.toLowerCase())) {
//                    Log.d("x", fil.getParentFile().toString());
//                    Log.d("x", fil.getAbsolutePath());
                    addListData(fil);
                    counter++;
//                    editor.putInt("KEY", counter);
//                    editor.commit();

                    intentBroadcast.putExtra(FILE_QUANTITY, counter);
                    intentBroadcast.putExtra(FILE_FOUND, fil.getName());
                    sendBroadcast(intentBroadcast);

                    if (isActivity && nm != null) {
                        nm.cancel(1);
                    } else {
                        intentNotifiedActivity.putExtra(LIST_FILE, listDataFile);
                        pi = PendingIntent.getActivity(getBaseContext(), 0, intentNotifiedActivity, PendingIntent.FLAG_UPDATE_CURRENT);
                        notification(String.valueOf(counter), fil.getName());
                        v.vibrate(500);
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "onStartCommand" + startId, Toast.LENGTH_SHORT).show();

        Bundle extras = intent.getExtras();
        fileFind = extras.getString(MainActivity.SEARCH_START, "no_file");

        if (flags == START_FLAG_REDELIVERY) {
            if (sp.contains("KEY")) {
                counter = sp.getInt("KEY", 0);
                Log.d("C", String.valueOf(counter));
            }
        }

        t = new MyThread(r);
        t.start();

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isServise = false;
//        nm.cancel(1);
        Toast.makeText(this, "onDestroy()", Toast.LENGTH_SHORT).show();
    }

    public void notification(String valueCount, String valueName) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            Notification.Builder builder = new
                    Notification.Builder(getApplicationContext());
            builder.setSmallIcon(R.drawable.ice)
                    .setTicker("Text in status bar ")
                    .setContentTitle("Amount of listFiles found: " + valueCount)
                    .setContentText("Found file: " + valueName)
                    .setAutoCancel(true);
            builder.setContentIntent(pi);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                notif = builder.build();
            else
                notif = builder.getNotification();
        } else {
//            notif = new Notification(R.drawable.yue, "Text in status bar", System.currentTimeMillis());
//            notif.setLatestEventInfo(this,"Notification's title","Notification's text", pi);
        }
        nm.notify(1, notif);
    }

    public class MyThread extends Thread {
        public MyThread(Runnable runnable) {
            super(runnable);
        }
    }
}
