package com.gospay.sdk;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.listeners.GosConfirmationPaymentListener;
import com.gospay.sdk.api.listeners.GosGetCardListListener;
import com.gospay.sdk.api.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.listeners.GosRemoveCardListener;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.request.models.card.RemoveCardParameter;
import com.gospay.sdk.api.request.models.payment.GosDeviceInfo;
import com.gospay.sdk.api.request.models.payment.confirm.ConfirmationPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentWithCardParameter;
import com.gospay.sdk.api.request.models.payment.init.PaymentFields;
import com.gospay.sdk.api.request.models.payment.status.GetPaymentStatusParameter;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.api.response.models.messages.payment.GosPayment;
import com.gospay.sdk.exceptions.GosInvalidCardFieldsException;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.storage.GosStorage;
import com.gospay.sdk.util.CreditCardValidator;
import com.gospay.sdk.util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bertalt on 01.09.16.
 * Main class for sdk users.
 */
public final class GosSdkManager {

    private GosNetworkManager networkManager;
    private static GosSdkManager ourInstance;
    private List<CardViewModel> cardList = new ArrayList<>();

    /**
     * Static creator for SDK manager
     *
     * @param context
     * @return {@link GosSdkManager}
     */
    public static GosSdkManager create(Context context) {

        ourInstance = new GosSdkManager(context);
        Logger.DEBUG = true;
        return ourInstance;
    }

    /**
     * Static getter for SDK manager. Attention! You should always call create() method before this.
     *
     * @return {@link GosSdkManager}
     */
    public static GosSdkManager getInstance() {

        if (ourInstance == null)
            throw new GosSdkException("SDK has not been created yet.");
        else {
            return ourInstance;
        }
    }

    /**
     * The {@link GosSdkManager} is used to add, list cards and execute payments
     */
    private GosSdkManager(Context context) {

        GosStorage storage = GosStorage.newInstance(context);
        networkManager = GosNetworkManager.newInstance(context);

    }

    private GosSdkManager(FragmentActivity context, boolean cacheCards) {
        this(context);

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

    /**
     * Execute request to add card. Result of operation will return in {@link GosAddCardListener} callback
     *
     * @param cardNumber
     * @param expireMonth
     * @param expiryYear
     * @param cvv
     * @param cardAlias
     * @param listener
     * @throws GosInvalidCardFieldsException
     */
    public void addCard(Context context, long cardNumber, String expireMonth, String expiryYear, String cvv, String cardAlias, final GosAddCardListener listener) throws GosInvalidCardFieldsException {

        final CardFields cardFields = CardFields.create(cardNumber, expireMonth, expiryYear, cvv, cardAlias);

        networkManager.addCard(context, cardFields, listener);

    }

    public void removeCard(Context context, CardViewModel card, GosRemoveCardListener listener){

        RemoveCardParameter parameter = new RemoveCardParameter(card);

        networkManager.removeCard(context,parameter, listener);
    }

    /**
     *
     * @param activity
     * @param listener
     */
    public void getCardList(FragmentActivity activity, GosGetCardListListener listener) {

        networkManager.getCardList(activity, listener);

    }

    /**
     * Return a  {@link List}  of GOSPAY {@link CardViewModel} objects.
     *
     * @return
     */
    public List<CardViewModel> getCachedCardList() {

        if (cardList != null)
            return cardList;
        else
            return new ArrayList<>();
    }

    /**
     * Executes initialization of payment. This action used to begin of payment processing.
     *
     * @param card                {@link CardViewModel} with UID of card
     * @param paymentFields       {@link PaymentFields} with payment details
     * @param listener {@link GosInitPaymentListener} listener to return result by callback
     */
    public void initPayment(Context context, CardViewModel card, PaymentFields paymentFields, GosInitPaymentListener listener) {

        InitPaymentParameter parameter = new InitPaymentParameter(card.getCardId(), paymentFields);

        networkManager.initPayment(context, parameter, listener);
    }



    /**
     * Executes confirmation of payment. This method usually call after {@link #initPayment(Context, CardViewModel, PaymentFields, GosInitPaymentListener) initPayment}
     * to continue of payment processing
     *
     * @param createdPayment with Id of GOSPAY {@link GosPayment}
     * @param cvv                 CVV security code of card
     * @param listener            {@link GosConfirmationPaymentListener} listener to return result by callback
     */
    public void confirmPayment(Context context, GosPayment createdPayment, String cvv, GosConfirmationPaymentListener listener) throws GosInvalidCardFieldsException {

        if (!CreditCardValidator.isCvvValid(cvv))
            throw new GosInvalidCardFieldsException(String.format("Cvv is not valid: %1s", cvv), GosInvalidCardFieldsException.GosInputField.CVV);

        ConfirmationPaymentParameter param = new ConfirmationPaymentParameter(createdPayment.getId(), cvv);

        networkManager.confirmationPayment(context, param, listener);

    }

    /**
     * Used to track payment status by GOSPAY {@link GosPayment} object with payment description
     *
     * @param payment
     * @param listener {@link GosGetPaymentStatusListener}  listener to return result by callback
     */
    public void getPaymentStatus(Context context, GosPayment payment, GosGetPaymentStatusListener listener) {

        GetPaymentStatusParameter parameter = new GetPaymentStatusParameter(payment.getId());

        networkManager.getPaymentStatus(context, parameter, listener);
    }

    public void payWithCard(Context context, CardFields card, PaymentFields paymentFields, GosInitPaymentListener listener){

        GosDeviceInfo deviceInfo = new GosDeviceInfo(context);

        InitPaymentWithCardParameter parameter = new InitPaymentWithCardParameter(card, paymentFields, deviceInfo);

        networkManager.initPaymentWithCard(context, parameter, listener);
    }


}
