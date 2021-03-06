package com.gospay.sdk.api.request.models.card;

import android.support.annotation.Nullable;

import com.gospay.sdk.exceptions.GosInvalidCardFieldsException;
import com.gospay.sdk.util.CreditCardValidator;

import java.util.Locale;


/**
 * Created by bertalt on 30.08.16.
 * Model to serialization card data to GSON
 */
public final class  CardFields {


    private long number;
    private String expire;
    private String cvvCode;
    private String alias;

    private CardFields(long number, String expire, String cvv, String alias) {

        this.number = number;
        this.expire = expire;
        this.cvvCode = cvv;
        this.alias = alias;
    }

    /**
     *
     * @param cardNumber sixteen digits. We supports VISA, MasterCard and Maestro
     * @param expiryMonth number of month [01-12]
     * @param expiryYear two last digits of expiry year
     * @param cvv three digits of security code
     * @param cardAlias card alias
     * @return
     * @throws GosInvalidCardFieldsException
     */
    public static CardFields create(long cardNumber, String expiryMonth, String expiryYear, String cvv, @Nullable String cardAlias) throws GosInvalidCardFieldsException {

//        String message;

        if(!CreditCardValidator.isCardValid(String.valueOf(cardNumber))) {

            throw new GosInvalidCardFieldsException(String.format("Credit card %1$16s is not valid", cardNumber), GosInvalidCardFieldsException.GosInputField.CARD_NUMBER);
        }

        if(!CreditCardValidator.isExpireMonthValid(expiryMonth)){

            throw new GosInvalidCardFieldsException(String.format("Expire month %1$2s is not valid", expiryMonth), GosInvalidCardFieldsException.GosInputField.EXPIRE_MONTH);
        }

        if(!CreditCardValidator.isExpireYearValid(expiryYear)){

            throw new GosInvalidCardFieldsException(String.format("Expire year %1$2s is not valid", expiryYear), GosInvalidCardFieldsException.GosInputField.EXPIRY_YEAR);
        }
        if(!CreditCardValidator.isExpireDateValid(expiryMonth, expiryYear)){

            throw  new GosInvalidCardFieldsException(String.format("Expire date %1$4s is not valid", expiryMonth+expiryYear), GosInvalidCardFieldsException.GosInputField.EXPIRY_DATE);
        }

        if(!CreditCardValidator.isCvvValid(cvv)){

            throw new GosInvalidCardFieldsException(String.format("CVV %1$3s is not valid", cvv), GosInvalidCardFieldsException.GosInputField.CVV);
        }

        String expireDate = String.format(Locale.ENGLISH,
                "%1$02d%2$02d",Integer.valueOf(expiryMonth),Integer.valueOf(expiryYear));

        return new CardFields(cardNumber,expireDate , cvv, cardAlias);
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
