package com.gospay.sdk;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.request.models.card.CardReference;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.PaymentFields;
import com.gospay.sdk.api.response.listeners.GosAddCardListener;
import com.gospay.sdk.api.response.listeners.GosConfirmationPaymentListener;
import com.gospay.sdk.api.response.listeners.GosGetCardListListener;
import com.gospay.sdk.api.response.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.response.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.response.listeners.GosResponseListener;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.api.response.models.messages.card.CardView;
import com.gospay.sdk.api.response.models.messages.payment.confirm.ConfirmedPayment;
import com.gospay.sdk.api.response.models.messages.payment.init.ConfirmationPayment;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.util.Logger;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by bertalt on 01.09.16.
 * Main class for sdk users.
 */
public final class GosSdkManager {

    private Context context;
    private GosStorage storage;
    private GosNetworkManager networkManager;
    private static GosSdkManager ourInstance;
    private Handler mainHandler;


    private GosSdkManager(Context context) {
        this.context = context;

        storage = GosStorage.newInstance(context);
        mainHandler = new Handler(context.getMainLooper());
        networkManager = GosNetworkManager.getInstance(context);

    }

    public static GosSdkManager create(Context context) {
        ourInstance = new GosSdkManager(context);

        return ourInstance;
    }
    public static GosSdkManager create(Context context, boolean setupDebug ) {
        ourInstance = new GosSdkManager(context);
        Logger.DEBUG = setupDebug;
        Logger.LOGD("Config from SDK "+BuildConfig.APPLICATION_ID);
        return ourInstance;
    }

    public static GosSdkManager getInstance() {

        if (ourInstance == null)
            throw new GosSdkException("SDK has not been created yet.", null);
        else {
            return ourInstance;
        }
    }

    public void destroy() {
        context = null;
        storage.clear();
        storage.destroy();
        networkManager = null;
    }


    public void addCard(long cardNumber, String expireMonth, String expireYear, String cvv, @Nullable String cardName, final GosAddCardListener listener) {


        final CardFields cardFields = CardFields.create(cardNumber, expireMonth, expireYear, cvv, cardName);

            networkManager.addCard(cardFields, listener);

    }

    public void getCardList(GosGetCardListListener listener) {

        networkManager.getCardList(listener);
    }




    public void setDebug(boolean debug) {
        Logger.DEBUG = debug;
    }


    public void initPayment(CardReference cardReference, PaymentFields paymentFields, GosInitPaymentListener initPaymentListener) {

        InitPaymentParameter parameter = new InitPaymentParameter(cardReference, paymentFields);

        networkManager.initPayment(parameter, initPaymentListener);
    }

    public void initPaymentUiThread(CardReference cardReference, PaymentFields paymentFields, GosInitPaymentListener initPaymentListener) {

        InitPaymentParameter parameter = new InitPaymentParameter(cardReference, paymentFields);

        networkManager.initPayment(parameter, initPaymentListener);
    }

    public void confirmPayment(ConfirmationPayment confirmationPayment, GosConfirmationPaymentListener listener) {



        networkManager.confirmationPayment(confirmationPayment, listener);
    }

    public void getPaymentStatus(ConfirmedPayment confirmedPayment, GosGetPaymentStatusListener getPaymentStatusListener) {

        networkManager.getPaymentStatus(confirmedPayment, getPaymentStatusListener);
    }
}
