package com.gospay.sdk.api.request.models.payment.status;

/**
 * Created by bertalt on 13.09.16.
 */
public class GetPaymentStatusParameter {

    private String payment;

    public GetPaymentStatusParameter(String payment) {
        this.payment = payment;
    }

    public String getPayment() {
        return payment;
    }
}
