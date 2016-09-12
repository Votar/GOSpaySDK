package com.gospay.sdk.api.response.listeners;

import com.gospay.sdk.api.response.models.messages.payment.confirm.ConfirmedPayment;

/**
 * Created by bertalt on 08.09.16.
 */
public interface GosConfirmationPaymentListener {
    void onSuccessConfirmationPayment(ConfirmedPayment confirmedPayment);
    void onFailureConfirmationPayment(String message);
}
