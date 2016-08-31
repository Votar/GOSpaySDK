package com.gos.rabbit.gossdk.models.card;

import com.gos.rabbit.gossdk.exceptions.GosSdkException;
import com.gos.rabbit.gossdk.util.CreditCardValidator;

/**
 * Created by bertalt on 30.08.16.
 */
public final class CardFields {

    private long cardNumber;
    private String expireDate;
    private String cvv;
    private String cardName= "";

    public String getCardName() {
        return cardName;
    }

    private CardFields() {
    }

    private CardFields(long cardNumber, String expireDate, String cvv, String cardName) {
        this.cardNumber = cardNumber;
        this.expireDate = expireDate;
        this.cvv = cvv;
        this.cardName = cardName;
    }

    public static CardFields create(long cardNumber, String expireMonth, String expireYear, String cvv, String cardName){


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

    public long getCardNumber() {
        return cardNumber;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getCvv() {
        return cvv;
    }
}
