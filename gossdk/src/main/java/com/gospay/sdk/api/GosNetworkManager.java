package com.gospay.sdk.api;


import android.app.Activity;
import android.app.Application;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.gospay.sdk.api.broadcast.AddCardReceiver;
import com.gospay.sdk.api.broadcast.ConfirmPaymentReceiver;
import com.gospay.sdk.api.broadcast.GetCardListReceiver;
import com.gospay.sdk.api.broadcast.GetStatusPaymentReceiver;
import com.gospay.sdk.api.broadcast.InitPaymentReceiver;
import com.gospay.sdk.api.client.GosRequest;
import com.gospay.sdk.api.client.cookie.MyCookieStore;
import com.gospay.sdk.api.request.models.payment.confirm.ConfirmationPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentParameter;
import com.gospay.sdk.api.request.models.payment.status.GetPaymentStatusParameter;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.listeners.GosConfirmationPaymentListener;
import com.gospay.sdk.api.listeners.GosGetCardListListener;
import com.gospay.sdk.api.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.util.NetworkUtils;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.api.service.NetworkService;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.util.Logger;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;


/**
 * Created by bertalt on 29.08.16.
 */
public final class GosNetworkManager {

    private static GosNetworkManager sInstance;
    private boolean isDebug = false;
    private String TAG = "GosNetworkManager";
    //    private static ServerApi serverApi;
    private Context context;
    private GosStorage storage;
    //    private static String API_KEY;
    //    private final String DEBUG_TOKEN = "GOS.TRACK=YXAVPMYXS5PMHDT65442VHQHYI7JUYFBHA5AMBC66RD2NUAWJVDCBR2RY4Z4PC7HDNDA6RWPW6GWOUP6CKDK2GG6OMDPDS4P7BPVMY7CYM4QRJWSZV4FNHFDCOR2ZKA4OXPS7KG7A3GJUV4UIZ3EJK4";
    private final String JSON_TYPE = "application/json";
    private CookieManager cookieManager;
    private Gson gson;
    //Test


    //Production
    //public static final String BACKEND_URL = "http://gateway.gospay.net:80/ws/";

    public static GosNetworkManager newInstance(Context context) {

        sInstance = new GosNetworkManager(context);

        return sInstance;
    }

    public static GosNetworkManager getInstance() {

        if (sInstance == null)
            throw new GosSdkException("NetworkManager has not been created yet");

        return sInstance;
    }


    public static GosNetworkManager getInstance(Application context, boolean isDebug) {

        if (sInstance == null)
            sInstance = new GosNetworkManager(context, isDebug);


        return sInstance;
    }

    private GosNetworkManager(Context context) {


        this.context = context;
        this.storage = GosStorage.getInstance();
        this.gson = new Gson();
        NetworkUtils.disableSSLCertificateChecking();

        cookieManager = new CookieManager(new MyCookieStore(context), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
//        cookieManager.getCookieStore().removeAll();
    }

    private GosNetworkManager(Context context, boolean isDebug) {

        this(context);
        this.isDebug = isDebug;

    }

    public boolean isDebug() {
        return Logger.DEBUG;
    }

    public void getCardList(Activity context, GosGetCardListListener listListener) {

        final GosRequest request = new GosRequest(ServerApi.GOS_REQUESTS.GET_CARD_LIST,
                null,
                ServerApi.GOS_METHODS.GET);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(NetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        GetCardListReceiver receiver = new GetCardListReceiver(listListener);
        IntentFilter intentFilter = new IntentFilter(NetworkService.NetworkContract.ACTION_GET_CARD_LIST);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);
    }

    public void addCard(CardFields fields, final GosAddCardListener listener, boolean showProgress) {

        String json = gson.toJson(fields, CardFields.class);

        Logger.LOGNET("toJson = \n" + json);

        final GosRequest request = new GosRequest(ServerApi.GOS_REQUESTS.ADD_CARD,
                json,
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(NetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        AddCardReceiver receiver = new AddCardReceiver(listener);
        IntentFilter intentFilter = new IntentFilter(NetworkService.NetworkContract.ACTION_ADD_CARD);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);

    }

    public void initPayment(InitPaymentParameter parameter, final GosInitPaymentListener initPaymentListener) {

        String json = gson.toJson(parameter, InitPaymentParameter.class);

        final GosRequest request = new GosRequest(ServerApi.GOS_REQUESTS.INIT_PAYMENT,
                json,
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(NetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        InitPaymentReceiver receiver = new InitPaymentReceiver(initPaymentListener);
        IntentFilter intentFilter = new IntentFilter(NetworkService.NetworkContract.ACTION_INIT_PAYMENT);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);

    }

    public void confirmationPayment(ConfirmationPaymentParameter parameter, final GosConfirmationPaymentListener listener) {

        String json = gson.toJson(parameter, ConfirmationPaymentParameter.class);

        Logger.LOGNET("toJson = \n" + parameter.toString());

        final GosRequest request = new GosRequest(ServerApi.GOS_REQUESTS.CONFIRM_PAYMENT,
                json,
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(NetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        ConfirmPaymentReceiver receiver = new ConfirmPaymentReceiver(listener);
        IntentFilter intentFilter = new IntentFilter(NetworkService.NetworkContract.ACTION_CONFIRM_PAYMENT);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);



    }

    public void getPaymentStatus(GetPaymentStatusParameter parameter, final GosGetPaymentStatusListener listener) {

        String json = gson.toJson(parameter, GetPaymentStatusParameter.class);

        final GosRequest request = new GosRequest(ServerApi.GOS_REQUESTS.GET_PAYMENT_STATUS,
                json,
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, NetworkService.class);
        intent.putExtra(NetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        GetStatusPaymentReceiver receiver = new GetStatusPaymentReceiver(listener);
        IntentFilter intentFilter = new IntentFilter(NetworkService.NetworkContract.ACTION_GET_PAYMENT_STATUS);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);

    }





    private void setupDefaultHeaders(GosRequest request) {

        request.addHeader(ServerApi.GOS_HEADERS.API_KEY, storage.getApiKey());
        request.addHeader(ServerApi.GOS_HEADERS.LOCALE, storage.getLanguage());
        request.addHeader(ServerApi.GOS_HEADERS.CONTENT_TYPE, JSON_TYPE);
        //TMP
        request.addHeader(ServerApi.GOS_HEADERS.ORIGIN, "http://www.x-obmen.com/");
        logCookie();
    }

    private void logCookie() {

        Logger.LOGNET("STORED COOKIE: ");
        for (HttpCookie tmp : cookieManager.getCookieStore().getCookies()) {
            Logger.LOGNET(tmp.toString());
        }
    }
}

