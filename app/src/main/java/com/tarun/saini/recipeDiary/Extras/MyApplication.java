package com.tarun.saini.recipeDiary.Extras;

import android.app.Application;

/**
 * Created by Tarun on 30-07-2017.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityCheck.ConnectivityReceiverListener listener) {
        ConnectivityCheck.connectivityReceiverListener = listener;
    }
}

