package com.gospay.sdk.api.request.models.payment.init;

import java.util.Currency;

/**
 * Created by bertalt on 08.09.16.
 */
public class PaymentFields {
    private double price;
    private Currency currency;
    private String summary;

    public PaymentFields(double price, Currency currency, String summary) {
        this.price = price;
        this.currency = currency;
        this.summary = summary;
    }

    public double getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getSummary() {
        return summary;
    }
}
