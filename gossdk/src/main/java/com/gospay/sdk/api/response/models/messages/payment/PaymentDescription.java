package com.gospay.sdk.api.response.models.messages.payment;

/**
 * Created by bertalt on 08.09.16.
 */
public class PaymentDescription {

    private String paymentStatus;
    int transactionId;
    private String url;

    public PaymentDescription(String paymentStatus, int transactionId, String url) {
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
        this.url = url;
    }

    public PaymentDescription(String paymentStatus, int transactionId) {
        this.paymentStatus = paymentStatus;
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getUrl() {
        return url;
    }
}
