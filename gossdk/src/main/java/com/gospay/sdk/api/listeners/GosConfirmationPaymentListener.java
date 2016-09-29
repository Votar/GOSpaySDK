package com.gospay.sdk.api.listeners;


/**
 * Created by bertalt on 08.09.16.
 */
public interface GosConfirmationPaymentListener {
        void onSuccessConfirmationPayment();
        void onFailureConfirmationPayment(String message);
}
