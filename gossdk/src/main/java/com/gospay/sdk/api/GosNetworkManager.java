package com.gospay.sdk.api;


import android.app.Application;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gospay.sdk.R;
import com.gospay.sdk.api.client.AsyncHttpClient;
import com.gospay.sdk.api.client.GosRequest;
import com.gospay.sdk.api.client.cookie.MyCookieStore;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentParameter;
import com.gospay.sdk.api.response.listeners.GosAddCardListener;
import com.gospay.sdk.api.response.listeners.GosConfirmationPaymentListener;
import com.gospay.sdk.api.response.listeners.GosGetCardListListener;
import com.gospay.sdk.api.response.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.response.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.response.listeners.GosResponseCallback;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.request.models.card.GetCardListParameter;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.api.response.models.messages.card.CardView;
import com.gospay.sdk.api.response.models.messages.payment.confirm.ConfirmedPayment;
import com.gospay.sdk.api.response.models.messages.payment.init.ConfirmationPayment;
import com.gospay.sdk.api.util.NetworkUtils;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.TextUtils;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


/**
 * Created by bertalt on 29.08.16.
 */
public final class GosNetworkManager {

    static GosNetworkManager sInstance;
    private boolean isDebug = false;
    private String TAG = "GosNetworkManager";
    private static ServerApi serverApi;
    private Context context;
    private GosStorage storage;
    private static String API_KEY;
    private final String DEBUG_TOKEN = "GOS.TRACK=YXAVPMYXS5PMHDT65442VHQHYI7JUYFBHA5AMBC66RD2NUAWJVDCBR2RY4Z4PC7HDNDA6RWPW6GWOUP6CKDK2GG6OMDPDS4P7BPVMY7CYM4QRJWSZV4FNHFDCOR2ZKA4OXPS7KG7A3GJUV4UIZ3EJK4";
    private final String JSON_TYPE = "application/json";
    private CookieManager cookieManager;
    //Test


    //Production
    //public static final String BACKEND_URL = "http://gateway.gospay.net:80/ws/";

    public static GosNetworkManager getInstance(Context context) {

        if (sInstance == null)
            sInstance = new GosNetworkManager(context);

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
        NetworkUtils.disableSSLCertificateChecking();

        cookieManager = new CookieManager(new MyCookieStore(context), CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }

    private GosNetworkManager(Context context, boolean isDebug) {

        this(context);
        this.isDebug = isDebug;

    }
    public boolean isDebug() {
        return Logger.DEBUG;
    }


    public String getToken() {

        return "asd";
      /*  return context.getSharedPreferences(context.getString(R.string.prefStorageName),
                Context.MODE_PRIVATE).getString(context.getString(R.string.prefKeyToken), "");*/
    }

    public void getCardList(final GosGetCardListListener listener) {

        final GosRequest request = new GosRequest(ServerApi.BACKEND_URL + ServerApi.GOS_REQUESTS.GET_CARD_LIST,
                null,
                ServerApi.GOS_METHODS.GET);

        setupDefaultHeaders(request);

        new AsyncHttpClient(new GosResponseCallback() {
            @Override
            public void onProcessFinished(GosResponse response) {

                if (response == null) {
                    listener.onGetCardListFailure(context.getString(R.string.error_network_connection));
                } else {
                    Gson gson = new Gson();
                    int resultCode = response.getResult().getCode();

                    switch (resultCode) {
                        case 0:
                            Logger.LOGNET(response.getPayload().toString());
                            //listener.onGetCardListSuccess();
                            break;
                        default:
                            listener.onGetCardListFailure(response.getResult().getMessage());
                    }
                }
            }
        })
                .execute(request);

    }

    public void initPayment(InitPaymentParameter parameter, GosInitPaymentListener initPaymentListener) {


    }

    public void confirmationPayment(ConfirmationPayment confirmationPayment, GosConfirmationPaymentListener listener) {



    }

    public void getPaymentStatus(ConfirmedPayment confirmedPayment, GosGetPaymentStatusListener getPaymentStatusListener) {


    }


    public void addCard(CardFields fields, final GosAddCardListener listener) {

        Logger.LOGNET("toJson = \n" + fields.toJson().toString());

        final GosRequest request = new GosRequest(ServerApi.BACKEND_URL + ServerApi.GOS_REQUESTS.ADD_CARD,
                fields.toJson().toString(),
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        new AsyncHttpClient(new GosResponseCallback() {
            @Override
            public void onProcessFinished(GosResponse response) {

                if (response == null) {
                    listener.onFailureAddCard(context.getString(R.string.error_network_connection));
                } else {
                    Gson gson = new Gson();
                    int resultCode = response.getResult().getCode();

                    switch (resultCode) {
                        case 0:
                            listener.onSuccessAddCard(gson.fromJson(response.getPayload(), CardView.class));
                            break;
                        default:
                            listener.onFailureAddCard(response.getResult().getMessage());
                    }
                }
            }
        })
                .execute(request);
    }


    private void setupDefaultHeaders(GosRequest request) {

        request.addHeader(ServerApi.GOS_HEADERS.API_KEY, storage.getApiKey());
        request.addHeader(ServerApi.GOS_HEADERS.LOCALE, storage.getLanguage());
        request.addHeader(ServerApi.GOS_HEADERS.CONTENT_TYPE, JSON_TYPE);
        logCookie();
    }

    private void logCookie() {

        Logger.LOGNET("STORED COOKIE: ");
        for (HttpCookie tmp : cookieManager.getCookieStore().getCookies()) {
            Logger.LOGNET(tmp.toString());
        }
    }
}

