package com.gospay.sdk.api.response.models.messages.payment.confirm;

/**
 * Created by bertalt on 08.09.16.
 */
public class ConfirmedPayment {

    private int transactionId;

    public ConfirmedPayment(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
