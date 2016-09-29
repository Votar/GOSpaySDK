package com.gospay.sdk.api.listeners;


import com.gospay.sdk.api.response.models.messages.payment.GosPayment;

/**
 * Created by bertalt on 08.09.16.
 */
public interface GosInitPaymentListener {
    void onSuccessInitPayment(GosPayment createdPayment);
    void onFailureInitPayment(String message);
}
