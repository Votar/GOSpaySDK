package com.gospay.sdk.api.listeners;


import com.gospay.sdk.api.response.models.messages.payment.Payment;

/**
 * Created by bertalt on 08.09.16.
 */
public interface GosInitPaymentListener {
    void onSuccessInitPayment(Payment paymentInProgress);
    void onFailureInitPayment(String message);
}
