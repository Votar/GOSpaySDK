package com.gospay.sdk;

import android.content.Intent;
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
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.api.response.models.messages.payment.Payment;
import com.gospay.sdk.exceptions.GosInvalidInputException;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.ui.payment.PaymentProcessingActivity;
import com.gospay.sdk.ui.dialog.card.add.AddCardDialog;
import com.gospay.sdk.ui.dialog.card.select.SelectCardDialog;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;

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
    private List<CardViewModel> cardList = new ArrayList<>();
    private boolean cacheCards;


    public static GosSdkManager create(FragmentActivity context) {

        ourInstance = new GosSdkManager(context);
        Logger.DEBUG = true;
        return ourInstance;
    }

    public static GosSdkManager create(FragmentActivity context, boolean cacheCards) {


        if (ourInstance == null) {
            ourInstance = new GosSdkManager(context, cacheCards);
            Logger.DEBUG = true;
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

    private GosSdkManager(FragmentActivity context, boolean cacheCards) {
        this(context);
        this.cacheCards = cacheCards;

        if (cacheCards)
            getCardList(context, new GosGetCardListListener() {
                @Override
                public void onGetCardListSuccess(ArrayList<CardViewModel> cardList) {
                    ourInstance.cardList.addAll(cardList);
                }

                @Override
                public void onGetCardListFailure(String message) {
                    Logger.LOGD(message);
                }
            });

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

    public void getCardList(FragmentActivity activity, GosGetCardListListener listener) {

        networkManager.getCardList(activity, listener);

    }

    public List<CardViewModel> getCachedCardList() {

        if (cardList != null)
            return cardList;
        else
            return new ArrayList<>();
    }


    public void setDebug(boolean debug) {
        Logger.DEBUG = debug;
    }


    public void initPayment(CardViewModel cardViewModel, PaymentFields paymentFields, GosInitPaymentListener initPaymentListener) {

        InitPaymentParameter parameter = new InitPaymentParameter(cardViewModel.getCardId(), paymentFields);

        networkManager.initPayment(parameter, initPaymentListener);
    }

    public void processPaymentOneClick(FragmentActivity activity, PaymentFields paymentFields) {

        Intent in = new Intent(activity, PaymentProcessingActivity.class);

        in.putExtra(PaymentProcessingActivity.PaymentContract.KEY_PAYMENT_FIELDS,
                Parser.getsInstance().toJson(paymentFields, PaymentFields.class));
        activity.startActivity(in);

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
