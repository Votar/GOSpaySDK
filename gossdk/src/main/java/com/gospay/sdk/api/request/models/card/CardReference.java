package com.gospay.sdk.api.request.models.card;

import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.util.CreditCardValidator;

/**
 * Created by bertalt on 06.09.16.
 */
public class CardReference {

    private String cardId;
    private String cvv;


    public CardReference(String cardId, String cvv) {
        this.cardId = cardId;

        if (CreditCardValidator.isCvvValid(cvv))
            this.cvv = cvv;
        else
            throw new GosSdkException(String.format("CVV %1s is not valid",cvv), null);
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }


    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getCardId() {
        return cardId;
    }

    public String getCvv() {
        return cvv;
    }


    @Override
    public String toString() {
        return "CardReference{" +
                "cardId=" + cardId +
                ", cvv='" + cvv + '\'' +
                '}';
    }
}
