package com.gospay.sdk.api.listeners;


/**
 * Created by bertalt on 08.09.16.
 */
public interface GosConfirmationPaymentListener {
    void onSuccessConfirmationPayment(String message);
    void onFailureConfirmationPayment(String message);
}
