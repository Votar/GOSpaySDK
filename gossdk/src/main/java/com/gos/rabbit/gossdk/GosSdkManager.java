package com.gos.rabbit.gossdk;


import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gos.rabbit.gossdk.listeners.GosResponseListener;
import com.gos.rabbit.gossdk.models.card.AddCardParameter;
import com.gos.rabbit.gossdk.models.card.CardFields;
import com.gos.rabbit.gossdk.network.LoggingInterceptor;
import com.gos.rabbit.gossdk.network.api.ServerApi;
import com.gos.rabbit.gossdk.util.DeviceInfo;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bertalt on 29.08.16.
 */
public class GosSdkManager {

    static GosSdkManager sInstance;
    private boolean isDebug = false;
    private String TAG = "GosSdkManager";
    private static ServerApi serverApi;
    private Application application;

    //Test
    public static final String BACKEND_URL = "http://gos.dev.perfsys.com:8081/ws/";

    //Production
    //public static final String BACKEND_URL = "http://gateway.gospay.net:80/ws/";

    public static GosSdkManager getInstance(Application application) {

        if (sInstance == null)
            sInstance = new GosSdkManager(application);

        return sInstance;
    }

    public static GosSdkManager getInstance(Application application, boolean isDebug) {

        if (sInstance == null)
            sInstance = new GosSdkManager(application, isDebug);

        return sInstance;
    }

    private GosSdkManager(Application application) {

        this.application = application;
        serverApi = retrofitBuilder.build().create(ServerApi.class);
    }
    private GosSdkManager(Application application, boolean isDubug){

        this.application = application;
        this.isDebug = isDubug;
        serverApi = retrofitBuilder.build().create(ServerApi.class);
    }

    private static Gson gson = new GsonBuilder().create();

    private static OkHttpClient.Builder okClientBuilder = new OkHttpClient.Builder();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(BACKEND_URL)
                    .client(okClientBuilder
                            .addInterceptor(new LoggingInterceptor())
                            .build())
                    .addConverterFactory(GsonConverterFactory.create(gson));



    public boolean isDebug() {
        return isDebug;
    }


    public void addCard(CardFields parameter, GosResponseListener listener){

        AddCardParameter param;
        String token = getToken();
        if(TextUtils.isEmpty(token)){
            param = new AddCardParameter(parameter, new DeviceInfo(application));
        }
        else{
            param= new AddCardParameter(parameter, token);
        }


        Log.d(TAG, new Gson().toJson(param, AddCardParameter.class));



    }

    public String getToken() {
        return "";
    }
}
