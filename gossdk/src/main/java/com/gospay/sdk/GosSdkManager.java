package com.gospay.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;

import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosSelectCardListener;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.request.models.payment.confirm.ConfirmationPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.PaymentFields;
import com.gospay.sdk.api.request.models.payment.status.GetPaymentStatusParameter;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.listeners.GosConfirmationPaymentListener;
import com.gospay.sdk.api.listeners.GosGetCardListListener;
import com.gospay.sdk.api.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.response.models.messages.card.CardView;
import com.gospay.sdk.api.response.models.messages.payment.Payment;
import com.gospay.sdk.exceptions.GosInvalidInputException;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.ui.dialog.card.add.AddCardDialog;
import com.gospay.sdk.ui.dialog.card.select.SelectCardDialog;
import com.gospay.sdk.ui.dialog.payment.PaymentDialog;
import com.gospay.sdk.ui.dialog.payment.PaymentStep;
import com.gospay.sdk.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bertalt on 01.09.16.
 * Main class for sdk users.
 */
public final class GosSdkManager {

    private FragmentActivity context;
    private GosStorage storage;
    private GosNetworkManager networkManager;
    private static GosSdkManager ourInstance;
    private List<CardView> cardList = new ArrayList<>();


    public static GosSdkManager create(FragmentActivity context) {

        ourInstance = new GosSdkManager(context);
        Logger.DEBUG = true;
        return ourInstance;
    }

    public static GosSdkManager create(FragmentActivity context, boolean cacheCards) {

        if (ourInstance == null) {
            ourInstance = new GosSdkManager(context);


            Logger.DEBUG = true;
            if (cacheCards)
                ourInstance.getCardList(new GosGetCardListListener() {
                    @Override
                    public void onGetCardListSuccess(ArrayList<CardView> cardList) {
                        ourInstance.cardList.addAll(cardList);
                    }

                    @Override
                    public void onGetCardListFailure(String message) {
                        Logger.LOGD(message);
                    }
                });
        }

        return ourInstance;
    }


    public static GosSdkManager getInstance() {

        if (ourInstance == null)
            throw new GosSdkException("SDK has not been created yet.");
        else {
            return ourInstance;
        }
    }


    private GosSdkManager(FragmentActivity context) {

        this.context = context;
        storage = GosStorage.newInstance(context);
        networkManager = GosNetworkManager.newInstance(context);
    }


    public void addCard(String cardNumber, String expireMonth, String expireYear, String cvv, @Nullable String cardName, final GosAddCardListener listener) throws GosInvalidInputException {


        final CardFields cardFields = CardFields.create(cardNumber, expireMonth, expireYear, cvv, cardName);

        networkManager.addCard(cardFields, listener, false);

    }


    public void addCardWithDialog(FragmentActivity context, final GosAddCardListener listener, boolean showProgress) {

        DialogFragment fragment = AddCardDialog.newInstance(listener, showProgress);

        fragment.setCancelable(false);

        fragment.show(context.getSupportFragmentManager(), AddCardDialog.TAG);

    }

    public void selectCardWithDialog(FragmentActivity context, GosSelectCardListener listener) {

        DialogFragment fragment;
        if (cardList.size() == 0) {
            fragment = SelectCardDialog.newInstance(listener);
        } else {
            fragment = SelectCardDialog.newInstance(listener, cardList);
        }
        fragment.show(context.getSupportFragmentManager(), SelectCardDialog.TAG);
    }

    public void getCardList(GosGetCardListListener listener) {

        networkManager.getCardList(listener);
    }

    public List<CardView> getCachedCardList() {

        if (cardList != null)
            return cardList;
        else
            return new ArrayList<>();
    }


    public void setDebug(boolean debug) {
        Logger.DEBUG = debug;
    }


    public void initPayment(CardView cardView, PaymentFields paymentFields, GosInitPaymentListener initPaymentListener) {

        InitPaymentParameter parameter = new InitPaymentParameter(cardView.getCardId(), paymentFields);

        networkManager.initPayment(parameter, initPaymentListener);
    }

    public void processPaymentWithDialog(FragmentActivity activity, PaymentFields paymentFields) {

        DialogFragment fragment = PaymentDialog.newInstance();

        Bundle args = new Bundle();

        args.putDouble(PaymentDialog.KEY_AMOUNT, paymentFields.getPrice());
        args.putString(PaymentDialog.KEY_CURRENCY, paymentFields.getCurrency().getCurrencyCode());
        args.putString(PaymentDialog.KEY_ORDER_ID, paymentFields.getOrder());
        args.putString(PaymentDialog.KEY_DESCRIPTION, paymentFields.getDescription());

        fragment.setArguments(args);

        fragment.show(activity.getSupportFragmentManager(), PaymentDialog.TAG);

    }

    public void confirmPayment(Payment confirmationPayment, String cvv, GosConfirmationPaymentListener listener) {

        ConfirmationPaymentParameter param = new ConfirmationPaymentParameter(confirmationPayment.getId(), cvv);

        networkManager.confirmationPayment(param, listener);

    }

    public void getPaymentStatus(Payment payment, GosGetPaymentStatusListener getPaymentStatusListener) {

        GetPaymentStatusParameter parameter = new GetPaymentStatusParameter(payment.getId());

        networkManager.getPaymentStatus(parameter, getPaymentStatusListener);
    }
}
