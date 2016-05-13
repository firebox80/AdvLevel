package com.example.android.servicetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ReceiverProgressBar extends BroadcastReceiver {

    private static final String TYPE = "type";
    private static final int ID_ACTION_PLAY = 0;
    private static final int ID_ACTION_STOP = 1;

    public ReceiverProgressBar() { }

    @Override
    public void onReceive(Context context, Intent intent) {

        int type = intent.getIntExtra(TYPE, ID_ACTION_STOP);
        switch (type)
        {
            case ID_ACTION_PLAY:

                break;
            case ID_ACTION_STOP:

                break;
        }
    }
}
