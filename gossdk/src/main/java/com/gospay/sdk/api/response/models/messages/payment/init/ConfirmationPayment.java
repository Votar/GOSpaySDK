package com.gospay.sdk.api.response.models.messages.payment.init;

/**
 * Created by bertalt on 08.09.16.
 */
public class ConfirmationPayment {

    private int transactionId;

    public ConfirmationPayment(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }
}
