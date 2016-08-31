package com.gos.rabbit.gossdk.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bertalt on 28.04.16.
 */
public class LoggingInterceptor implements Interceptor {
    private static final String TAG = "stetho";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        for(String tmp_name : request.headers().names()){

            Log.d(TAG,request.headers().get(tmp_name));

        }
        long t1 = System.nanoTime();


        Log.i(TAG, String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
//        Log.i(TAG, request.body().toString());
        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i(TAG, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}
