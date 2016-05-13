package com.example.maxim.internet;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Android on 31.03.2016.
 */
public class Library {

    public static boolean isNetworkExists(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean result= false;

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!=null){
            result = result || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
        }
        else{   // аппарат не имеет мобильного подключения - планшет без 3G
            result = result || false;
        }

        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!=null){
            result = result || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        }
        else{   // аппарат не имеет Wifi подключения
            result = result || false;
        }

        return result;
    }

}
