package com.example.android.servicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button bStartService;
    private Button bStopService;
    private Switch swServiceWork;
    private Intent intentService;

    private ProgressBar mProgressBar;
    private ReceiverProgressBar myRecProgBar;
    private PlayerReceiver myPlayerRecProgBar;
    private IntentFilter filterRecProgBar;

    private Thread t;
    private boolean isCondition = true;
    private int threadSleep = 100;

    private final static String ACTION_PROGRESSBAR = "com.example.android.servicetest";
    private static final String TYPE = "type";
    private static final int ID_ACTION_PLAY = 0;
    private static final int ID_ACTION_STOP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentService = new Intent(this, MyService.class);

        swServiceWork = (Switch)findViewById(R.id.switch1);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        filterRecProgBar = new IntentFilter(ACTION_PROGRESSBAR);

        myRecProgBar = new ReceiverProgressBar();
        registerReceiver(myRecProgBar, filterRecProgBar);

        myPlayerRecProgBar = new PlayerReceiver();
        registerReceiver(myPlayerRecProgBar, filterRecProgBar);

//        IntentFilter filter = new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
//        filter.addAction("android.intent.action.SCREEN_OFF");
//        filter.addAction("android.intent.action.SCREEN_ON");
//        registerReceiver(systemRec, filter);

        if(MyService.isServiseWork){
            swServiceWork.setChecked(false);
            swServiceWork.setText("Stop");
        } else {
            swServiceWork.setChecked(true);
            swServiceWork.setText("Start");
        }

        swServiceWork.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

              if(isChecked) {
                  MyService.isStop = true;
                  stopService(intentService);
                  swServiceWork.setText("Start");
              } else {
                  MyService.isStop = false;
                  startService(intentService);
                  swServiceWork.setText("Stop");
              }
            }
        });

        bStartService = (Button)findViewById(R.id.bStartButton);
        bStopService = (Button)findViewById(R.id.bStopService);
        bStartService.setOnClickListener(this);
        bStopService.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bStartButton:
                MyService.isStop = false;
//                intentService = new Intent(this, MyService.class);
                startService(intentService);
                break;
            case R.id.bStopService:
                MyService.isStop = true;
//                if(intentService!=null)
//                    stopService(intentService);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // дерегистрируем (выключаем) BroadcastReceiver
        try {
            unregisterReceiver(myRecProgBar);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public class PlayerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int type = intent.getIntExtra(TYPE, ID_ACTION_STOP);
            switch (type)
            {
                case ID_ACTION_PLAY:
                    Runnable r1 = new Runnable() {
                        @Override
                        public void run() {
                            while (isCondition) {

                                Log.d("RUN", "HANDLER");
                                try {
                                    Thread.sleep(threadSleep);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                mProgressBar.incrementProgressBy(1);
                            }
                        }
                    };

                    if(!(t!=null && t.isAlive()) && isCondition) {
                        t = new Thread(r1);
                        t.start();
                    }
                    break;
                case ID_ACTION_STOP:
                    isCondition = false;
                    break;
            }
        }
    }
}
