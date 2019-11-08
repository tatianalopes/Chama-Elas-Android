package com.tcal.chamaelas.util;

import android.app.Application;
import android.content.Context;

/**
 * This class makes the access to the application context easier
 */
public class AppContextUtil extends Application {

    // Obs: there is no memory leak as context is part of the application which is a process which
    // will only get killed when the application closes
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * Returns the application context
     *
     * @return the application context
     */
    public static Context getContext() {
        return mContext;
    }
}
