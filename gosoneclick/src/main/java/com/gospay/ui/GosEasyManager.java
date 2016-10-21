package com.gospay.ui;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.listeners.GosSelectCardListener;
import com.gospay.sdk.api.request.models.payment.init.PaymentFields;
import com.gospay.sdk.exceptions.GosInvalidPaymentFieldsException;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.util.Parser;
import com.gospay.ui.card.add.AddCardDialog;
import com.gospay.ui.card.add.AddCardFragment;
import com.gospay.ui.card.select.SelectCardDialog;
import com.gospay.ui.payment.PaymentProcessingActivity;

/**
 * Created by bertalt on 01.09.16.
 * Main class for sdk users.
 */
public final class GosEasyManager {

    private GosNetworkManager networkManager;
    private static GosEasyManager ourInstance;

    private PaymentFields currentPayment;

    public static GosEasyManager newInstance(FragmentActivity context) {

        if (ourInstance == null)
            ourInstance = new GosEasyManager(context);

        return ourInstance;

    }

    public static GosEasyManager getInstance(){
        if(ourInstance == null)
            throw new GosSdkException("Try to get instance before newInstance()");

        return ourInstance;
    }

    private GosEasyManager(FragmentActivity context) {

        GosStorage.newInstance(context);
        networkManager = GosNetworkManager.newInstance(context);

    }

    public PaymentFields getCurrentPayment(){
        if(currentPayment == null)
            throw new GosSdkException("Try to get current payment but it is null");

        return currentPayment;
    }

    /**
     * Shows dialog fragment with input fields to add card
     *
     * @param activity
     * @param listener
     */
    public static void addCardWithDialog(FragmentActivity activity, final GosAddCardListener listener) {

        /*if(ourInstance == null)
            ourInstance = new GosEasyManager(activity);*/

        DialogFragment fragment = AddCardDialog.newInstance(listener);

        fragment.setCancelable(false);

        fragment.show(activity.getSupportFragmentManager(), AddCardDialog.TAG);


    }

    /**
     * @param activity
     * @param amount
     * @param currency    - in order ISO 4217
     * @param description
     * @param orderId
     * @throws GosInvalidPaymentFieldsException
     */
    public static void processPayment(FragmentActivity activity, double amount, String currency, String description, String orderId) throws GosInvalidPaymentFieldsException {

        if (ourInstance == null)
            ourInstance = new GosEasyManager(activity);


        Intent in = new Intent(activity, PaymentProcessingActivity.class);

        ourInstance.currentPayment = PaymentFields.create(amount, currency, description, orderId);;


       /* in.putExtra(PaymentProcessingActivity.PaymentContract.KEY_PAYMENT_FIELDS,
                Parser.getsInstance().toJson(paymentFields, PaymentFields.class));*/
        activity.startActivity(in);
    }


    public static void selectCardWithDialog(FragmentActivity activity, GosSelectCardListener listener) {

        if (ourInstance == null)
            ourInstance = new GosEasyManager(activity);

        DialogFragment fragment;

        fragment = SelectCardDialog.newInstance(listener);

        fragment.show(activity.getSupportFragmentManager(), SelectCardDialog.TAG);
    }


}
