package com.gospay.sdk.util;

import com.google.gson.Gson;

/**
 * Created by bertalt on 20.09.16.
 */
public class Parser {
    private static Gson sInstance = new Gson();

    private Parser(){}

    public static Gson getsInstance(){
        if(sInstance == null)
            new Parser();

        return sInstance;
    }
}
