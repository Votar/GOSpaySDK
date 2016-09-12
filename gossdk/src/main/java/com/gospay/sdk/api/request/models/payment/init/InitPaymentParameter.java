package com.gospay.sdk.api.request.models.payment.init;

import com.gospay.sdk.api.request.models.card.CardReference;

import java.util.Currency;


/**
 * Created by bertalt on 08.09.16.
 */
public class InitPaymentParameter {

    private CardReference card;
    private double price;
    private String currency;
    private String summary;


    public InitPaymentParameter(CardReference card, PaymentFields fields) {
        this.card = card;
        this.price = fields.getPrice();
        this.currency = fields.getCurrency().getCurrencyCode();
        this.summary = fields.getSummary();
    }

    public CardReference getCard() {
        return card;
    }

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSummary() {
        return summary;
    }
}
