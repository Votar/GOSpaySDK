package com.gospay.sdk.api.request.models.card;

import android.support.annotation.Nullable;

import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.util.CreditCardValidator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bertalt on 30.08.16.
 */
public final class CardFields {

    private interface JSONKeys{
        public static String NUMBER = "number";
        public static String EXPIRE = "expire";
        public static String CVV = "cvvCode";
        public static String ALIAS = "alias";
    }

    private long number;
    private String expire;
    private String cvvCode;
    private String alias;

    public String getAlias() {
        return alias;
    }

    private CardFields() {
    }

    public CardFields(long number, String expire) {
        this.number = number;
        this.expire = expire;
    }

    private CardFields(long number, String expire, String cvv, String alias) {
        this.number = number;
        this.expire = expire;
        this.cvvCode = cvv;
        this.alias = alias;
    }

    public static CardFields create(long cardNumber, String expireMonth, String expireYear, String cvv, @Nullable String cardName){

        String cardNum = String.valueOf(cardNumber);
        String message;

        if(!CreditCardValidator.isCardValid(cardNum)) {

            throw new GosSdkException(String.format("Credit card %1$16s is not valid", cardNum), null);
        }

        if(!CreditCardValidator.isExpireDateValid(expireMonth, expireYear)){

            throw  new GosSdkException(String.format("Expire date %1$4s is not valid", expireMonth+expireYear), null);
        }

        if(!CreditCardValidator.isCvvValid(cvv)){

            throw new GosSdkException(String.format("CVV %1$3s is not valid", cvv), null);
        }

        return new CardFields(cardNumber, expireMonth+expireYear, cvv, cardName);
    }


    public JSONObject toJson(){

        JSONObject object = new JSONObject();

        try {
            object.put(JSONKeys.ALIAS, getAlias());
            object.put(JSONKeys.CVV, getCvvCode());
            object.put(JSONKeys.EXPIRE, getExpire());
            object.put(JSONKeys.NUMBER, getNumber());
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            throw new GosSdkException("Trouble in JSON serialization", null);
        }

    }

    public long getNumber() {
        return number;
    }
    public String getExpire() {
        return expire;
    }
    public String getCvvCode() {
        return cvvCode;
    }
}
