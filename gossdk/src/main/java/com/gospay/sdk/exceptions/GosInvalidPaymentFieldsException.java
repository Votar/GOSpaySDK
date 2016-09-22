package com.gospay.sdk.exceptions;

/**
 * Created by bertalt on 14.09.16.
 */
public final class GosInvalidPaymentFieldsException extends Exception {

    private GosPaymentField value;

    public enum GosPaymentField {
        PRICE, CURRENCY, DESCRIPTION, ORDER_ID;
    }



    public GosInvalidPaymentFieldsException(String message, GosPaymentField value) {
        super(message);
        this.value = value;
    }

    public GosPaymentField getInvalidField() {
        return value;
    }
}
