package com.gospay.sdk.exceptions;

/**
 * Created by bertalt on 14.09.16.
 */
public class GosInvalidInputException extends Exception {

    private GosInputField value;

    public enum GosInputField {
        CARD_NUMBER, EXPIRE_MONTH, EXPIRE_YEAR, EXPIRE_DATE, CVV, CARD_ALIAS;
    }



    public GosInvalidInputException(String message, GosInputField value) {
        super(message);
        this.value = value;
    }

    public GosInputField getInvalidField() {
        return value;
    }
}
