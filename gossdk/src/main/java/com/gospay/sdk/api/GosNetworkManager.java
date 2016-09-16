package com.gospay.sdk.api;


import android.app.Application;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.gospay.sdk.R;
import com.gospay.sdk.api.client.AsyncHttpClient;
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
import com.gospay.sdk.api.listeners.GosResponseCallback;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.api.response.models.messages.card.CardView;
import com.gospay.sdk.api.response.models.messages.payment.Payment;
import com.gospay.sdk.api.util.NetworkUtils;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.util.Logger;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.ArrayList;


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
    private static String API_KEY;
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
                            ArrayList<CardView> resultArray = new ArrayList<CardView>();

                            JsonArray array = response.getPayload().getAsJsonArray();

                            if (array.size() == 0)
                                listener.onGetCardListSuccess(resultArray);
                            else {
                                for (JsonElement nextValue : array)
                                    resultArray.add(gson.fromJson(nextValue, CardView.class));

                                listener.onGetCardListSuccess(resultArray);
                            }
                            break;
                        default:
                            listener.onGetCardListFailure(response.getResult().getMessage());
                    }
                }
            }
        })
                .execute(request);

    }

    public void initPayment(InitPaymentParameter parameter, final GosInitPaymentListener initPaymentListener) {

        String json = gson.toJson(parameter, InitPaymentParameter.class);

        final GosRequest request = new GosRequest(ServerApi.BACKEND_URL + ServerApi.GOS_REQUESTS.INIT_PAYMENT,
                json,
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        new AsyncHttpClient(new GosResponseCallback() {
            @Override
            public void onProcessFinished(GosResponse response) {

                if (response == null) {
                    initPaymentListener.onFailureInitPayment(context.getString(R.string.error_network_connection));
                } else {

                    int resultCode = response.getResult().getCode();

                    switch (resultCode) {
                        case 0:
                            Logger.LOGNET(response.getPayload().toString());
                            initPaymentListener.onSuccessInitPayment(gson.fromJson(response.getPayload(), Payment.class));
                            break;
                        default:
                            initPaymentListener.onFailureInitPayment(response.getResult().getMessage());
                    }
                }
            }
        })
                .execute(request);


    }

    public void confirmationPayment(ConfirmationPaymentParameter parameter, final GosConfirmationPaymentListener listener) {

        String json = gson.toJson(parameter, ConfirmationPaymentParameter.class);

        Logger.LOGNET("toJson = \n" + parameter.toString());

        final GosRequest request = new GosRequest(ServerApi.BACKEND_URL + ServerApi.GOS_REQUESTS.CONFIRM_PAYMENT,
                json,
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        new AsyncHttpClient(new GosResponseCallback() {
            @Override
            public void onProcessFinished(GosResponse response) {

                if (response == null) {
                    listener.onFailureConfirmationPayment(context.getString(R.string.error_network_connection));
                } else {

                    int resultCode = response.getResult().getCode();

                    switch (resultCode) {
                        case 0:
                            listener.onSuccessConfirmationPayment(response.getResult().getMessage());
                            break;
                        default:
                            listener.onFailureConfirmationPayment(response.getResult().getMessage());
                    }
                }
            }
        })
                .execute(request);


    }

    public void getPaymentStatus(GetPaymentStatusParameter parameter, final GosGetPaymentStatusListener getPaymentStatusListener) {

        String json = gson.toJson(parameter, GetPaymentStatusParameter.class);

        final GosRequest request = new GosRequest(ServerApi.BACKEND_URL + ServerApi.GOS_REQUESTS.GET_PAYMENT_STATUS,
                json,
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        new AsyncHttpClient(new GosResponseCallback() {
            @Override
            public void onProcessFinished(GosResponse response) {

                if (response == null) {
                    getPaymentStatusListener.onFailureGetPaymentStatus(context.getString(R.string.error_network_connection));
                } else {

                    int resultCode = response.getResult().getCode();

                    switch (resultCode) {
                        case 0:
                            Logger.LOGNET(response.getPayload().toString());
                            getPaymentStatusListener.onSuccessGetPaymentStatus(gson.fromJson(response.getPayload(), Payment.class));
                            break;
                        default:
                            getPaymentStatusListener.onFailureGetPaymentStatus(response.getResult().getMessage());
                    }
                }
            }
        })
                .execute(request);

    }


    public void addCard(CardFields fields, final GosAddCardListener listener, boolean showProgress) {

        String json = gson.toJson(fields, CardFields.class);

        Logger.LOGNET("toJson = \n" + json);

        final GosRequest request = new GosRequest(ServerApi.BACKEND_URL + ServerApi.GOS_REQUESTS.ADD_CARD,
                json,
                ServerApi.GOS_METHODS.POST);

        setupDefaultHeaders(request);

        GosResponseCallback gosCallback = new GosResponseCallback() {
            @Override
            public void onProcessFinished(GosResponse response) {

                if (response == null) {
                    listener.onFailureAddCard(context.getString(R.string.error_network_connection));
                } else {

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
        };

        AsyncHttpClient client;

        if (showProgress)
            client = new AsyncHttpClient(context, gosCallback);
        else
            client = new AsyncHttpClient(gosCallback);

        client.execute(request);

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

