package com.gospay.sdk.util;

import android.util.Log;

import com.gospay.sdk.BuildConfig;

/**
 * Created by bertalt on 06.09.16.
 */
public class Logger {

    public static boolean DEBUG = false;

    public static void LOGD(String message){

        if(DEBUG)
            Log.d("DEBUG", message);
    }

    public static void LOGNET(String message){

        if(DEBUG)
            Log.d("NETWORK", message);
    }
}
