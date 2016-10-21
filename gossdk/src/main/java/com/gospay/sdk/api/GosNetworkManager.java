package com.gospay.sdk.api;


import android.app.Activity;
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
import com.gospay.sdk.api.broadcast.RemoveCardReceiver;
import com.gospay.sdk.api.client.GosRequest;
import com.gospay.sdk.api.client.cookie.GosCookieStore;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.listeners.GosConfirmationPaymentListener;
import com.gospay.sdk.api.listeners.GosGetCardListListener;
import com.gospay.sdk.api.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.listeners.GosRemoveCardListener;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.request.models.card.RemoveCardParameter;
import com.gospay.sdk.api.request.models.payment.confirm.ConfirmationPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentWithCardParameter;
import com.gospay.sdk.api.request.models.payment.status.GetPaymentStatusParameter;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.api.service.GosNetworkService;
import com.gospay.sdk.api.util.NetworkUtils;
import com.gospay.sdk.exceptions.GosSdkException;
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
    private GosStorage storage;
    private CookieManager cookieManager;
    private Gson gson;
    private final String ORIGIN;

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


    private GosNetworkManager(Context context) {
        this.storage = GosStorage.getInstance();
        this.gson = new Gson();
        NetworkUtils.disableSSLCertificateChecking();

        ORIGIN = "http://" + context.getPackageName();

        cookieManager = new CookieManager(new GosCookieStore(context), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);

        clearToken();


    }

    private void clearToken() {
        if (cookieManager != null)
            cookieManager.getCookieStore().removeAll();
    }


    public void getCardList(Activity context, GosGetCardListListener listListener) {

        final GosRequest request = new GosRequest(GosServerApi.GOS_REQUESTS.GET_CARD_LIST,
                null,
                GosServerApi.GOS_METHODS.GET);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, GosNetworkService.class);
        intent.putExtra(GosNetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        GetCardListReceiver receiver = new GetCardListReceiver(listListener);
        IntentFilter intentFilter = new IntentFilter(GosNetworkService.NetworkContract.ACTION_GET_CARD_LIST);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);
    }

    public void addCard(Context context, CardFields fields, final GosAddCardListener listener) {

        String json = gson.toJson(fields, CardFields.class);

        Logger.LOGNET("toJson = \n" + json);

        final GosRequest request = new GosRequest(GosServerApi.GOS_REQUESTS.ADD_CARD,
                json,
                GosServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, GosNetworkService.class);
        intent.putExtra(GosNetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        AddCardReceiver receiver = new AddCardReceiver(listener);
        IntentFilter intentFilter = new IntentFilter(GosNetworkService.NetworkContract.ACTION_ADD_CARD);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);

    }

    public void removeCard(Context context, RemoveCardParameter parameter, final GosRemoveCardListener listener) {

        String json = gson.toJson(parameter, RemoveCardParameter.class);

        Logger.LOGNET("toJson = \n" + json);

        final GosRequest request = new GosRequest(GosServerApi.GOS_REQUESTS.REMOVE_CARD,
                json,
                GosServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, GosNetworkService.class);
        intent.putExtra(GosNetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        RemoveCardReceiver receiver = new RemoveCardReceiver(listener);
        IntentFilter intentFilter = new IntentFilter(GosNetworkService.NetworkContract.ACTION_REMOVE_CARD);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);

    }


    public void initPayment(Context context, InitPaymentParameter parameter, final GosInitPaymentListener initPaymentListener) {

        String json = gson.toJson(parameter, InitPaymentParameter.class);

        final GosRequest request = new GosRequest(GosServerApi.GOS_REQUESTS.INIT_PAYMENT,
                json,
                GosServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, GosNetworkService.class);
        intent.putExtra(GosNetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        InitPaymentReceiver receiver = new InitPaymentReceiver(initPaymentListener);
        IntentFilter intentFilter = new IntentFilter(GosNetworkService.NetworkContract.ACTION_INIT_PAYMENT);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);

    }

    public void confirmationPayment(Context context, ConfirmationPaymentParameter parameter, final GosConfirmationPaymentListener listener) {

        String json = gson.toJson(parameter, ConfirmationPaymentParameter.class);

        Logger.LOGNET("toJson = \n" + parameter.toString());

        final GosRequest request = new GosRequest(GosServerApi.GOS_REQUESTS.CONFIRM_PAYMENT,
                json,
                GosServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, GosNetworkService.class);
        intent.putExtra(GosNetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        ConfirmPaymentReceiver receiver = new ConfirmPaymentReceiver(listener);
        IntentFilter intentFilter = new IntentFilter(GosNetworkService.NetworkContract.ACTION_CONFIRM_PAYMENT);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);


    }

    public void getPaymentStatus(Context context, GetPaymentStatusParameter parameter, final GosGetPaymentStatusListener listener) {

        String json = gson.toJson(parameter, GetPaymentStatusParameter.class);

        final GosRequest request = new GosRequest(GosServerApi.GOS_REQUESTS.GET_PAYMENT_STATUS,
                json,
                GosServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, GosNetworkService.class);
        intent.putExtra(GosNetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        GetStatusPaymentReceiver receiver = new GetStatusPaymentReceiver(listener);
        IntentFilter intentFilter = new IntentFilter(GosNetworkService.NetworkContract.ACTION_GET_PAYMENT_STATUS);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);

    }

    public void initPaymentWithCard(Context context, InitPaymentWithCardParameter parameter, GosInitPaymentListener listener) {

        String json = gson.toJson(parameter, InitPaymentWithCardParameter.class);

        final GosRequest request = new GosRequest(GosServerApi.GOS_REQUESTS.INIT_PAYMENT_WITH_CARD,
                json,
                GosServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        Intent intent = new Intent(context, GosNetworkService.class);
        intent.putExtra(GosNetworkService.NetworkContract.KEY_REQUEST, gson.toJson(request, GosRequest.class));

        InitPaymentReceiver receiver = new InitPaymentReceiver(listener);
        IntentFilter intentFilter = new IntentFilter(GosNetworkService.NetworkContract.ACTION_INIT_PAYMENT);

        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);

        context.startService(intent);

    }


    private void setupDefaultHeaders(GosRequest request) {

        request.addHeader(GosServerApi.GOS_HEADERS.API_KEY, storage.getApiKey());
        request.addHeader(GosServerApi.GOS_HEADERS.LOCALE, storage.getLanguage());
        String JSON_TYPE = "application/json";
        request.addHeader(GosServerApi.GOS_HEADERS.CONTENT_TYPE, JSON_TYPE);
        //TMP
        request.addHeader(GosServerApi.GOS_HEADERS.ORIGIN, ORIGIN);
        logCookie();
    }

    private void logCookie() {

        Logger.LOGNET("STORED COOKIE: ");
        for (HttpCookie tmp : cookieManager.getCookieStore().getCookies()) {
            Logger.LOGNET(tmp.toString());
        }
    }


}

