package com.gospay.sdk.api.request.models.card;

import android.support.annotation.Nullable;

import com.gospay.sdk.exceptions.GosInvalidInputException;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.util.CreditCardValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bertalt on 30.08.16.
 */
public final class CardFields {


    private long number;
    private String expire;
    private String cvvCode;
    private String alias;

    private CardFields() {
    }

    private CardFields(String number, String expire, String cvv, String alias) {

        this.number = Long.parseLong(number);
        this.expire = expire;
        this.cvvCode = cvv;
        this.alias = alias;
    }

    public static CardFields create(String cardNumber, String expireMonth, String expireYear, String cvv, @Nullable String cardName) throws GosInvalidInputException{

        String message;

        if(!CreditCardValidator.isCardValid(cardNumber)) {

            throw new GosInvalidInputException(String.format("Credit card %1$16s is not valid", cardNumber), GosInvalidInputException.GosInputField.CARD_NUMBER);
        }

        if(!CreditCardValidator.isExpireMonthValid(expireMonth)){

            throw new GosInvalidInputException(String.format("Expire month %1$2s is not valid", expireMonth), GosInvalidInputException.GosInputField.EXPIRE_MONTH);
        }

        if(!CreditCardValidator.isExpireYearValid(expireYear)){

            throw new GosInvalidInputException(String.format("Expire year %1$2s is not valid", expireYear), GosInvalidInputException.GosInputField.EXPIRE_YEAR);
        }
        if(!CreditCardValidator.isExpireDateValid(expireMonth, expireYear)){

            throw  new GosInvalidInputException(String.format("Expire date %1$4s is not valid", expireMonth+expireYear), GosInvalidInputException.GosInputField.EXPIRE_DATE);
        }

        if(!CreditCardValidator.isCvvValid(cvv)){

            throw new GosInvalidInputException(String.format("CVV %1$3s is not valid", cvv), GosInvalidInputException.GosInputField.CVV);
        }

        return new CardFields(cardNumber, expireMonth+expireYear, cvv, cardName);
    }

    public String getAlias() {
        return alias;
    }
    public String getNumber() {
        return String.valueOf(number);
    }
    public String getExpire() {
        return expire;
    }
    public String getCvvCode() {
        return cvvCode;
    }
}
