package com.gospay.sdk.exceptions;

/**
 * Created by bertalt on 14.09.16.
 */
public final class GosInvalidCardFieldsException extends Exception {

    private GosInputField value;

    public enum GosInputField {
        CARD_NUMBER, EXPIRE_MONTH, EXPIRE_YEAR, EXPIRE_DATE, CVV, CARD_ALIAS;
    }



    public GosInvalidCardFieldsException(String message, GosInputField value) {
        super(message);
        this.value = value;
    }

    public GosInputField getInvalidField() {
        return value;
    }
}
