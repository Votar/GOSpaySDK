package com.gospay.sdk.api.response.listeners;

import com.gospay.sdk.api.response.models.messages.payment.init.ConfirmationPayment;

/**
 * Created by bertalt on 08.09.16.
 */
public interface GosInitPaymentListener {
    void onSuccessInitPayment(ConfirmationPayment paymentInProgress);
    void onFailureInitPayment(String message);
}
