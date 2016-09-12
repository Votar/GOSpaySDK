package com.gospay.sdk.api.response.listeners;

import com.gospay.sdk.api.response.models.messages.payment.PaymentDescription;

/**
 * Created by bertalt on 08.09.16.
 */
public interface GosGetPaymentStatusListener {
    void onSuccessGetPaymentStatus(PaymentDescription paymentDescription);
    void onFailureGetPaymentStatus(String message);
}
