package com.gospay.ui;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.request.models.payment.init.PaymentFields;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.exceptions.GosInvalidPaymentFieldsException;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.util.Parser;
import com.gospay.ui.card.add.AddCardDialog;
import com.gospay.ui.payment.PaymentProcessingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bertalt on 01.09.16.
 * Main class for sdk users.
 */
public final class GosEasyManager {

    private GosNetworkManager networkManager;
    private static GosEasyManager ourInstance;
    private List<CardViewModel> cardList = new ArrayList<>();

    private GosEasyManager(android.support.v4.app.FragmentActivity context) {

        GosStorage storage = GosStorage.newInstance(context);
        networkManager = GosNetworkManager.newInstance(context);

    }

    /**
     * Shows dialog fragment with input fields to add card
     *
     * @param activity
     * @param listener
     */
    public void addCardWithDialog(FragmentActivity activity, final GosAddCardListener listener) {

        DialogFragment fragment = AddCardDialog.newInstance(listener);

        fragment.setCancelable(false);

        fragment.show(activity.getSupportFragmentManager(), AddCardDialog.TAG);

    }
    /**
     *
     * @param activity
     * @param amount
     * @param currency - in order ISO 4217
     * @param description
     * @param orderId
     * @throws GosInvalidPaymentFieldsException
     */
    public static void processPayment(FragmentActivity activity, double amount, String currency, String description, String orderId) throws GosInvalidPaymentFieldsException {

        if(ourInstance == null)
            ourInstance = new GosEasyManager(activity);

        PaymentFields paymentFields = PaymentFields.create(amount, currency, description, orderId);

        Intent in = new Intent(activity, PaymentProcessingActivity.class);

        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        in.putExtra(PaymentProcessingActivity.PaymentContract.KEY_PAYMENT_FIELDS,
                Parser.getsInstance().toJson(paymentFields, PaymentFields.class));
        activity.startActivity(in);
    }


}
