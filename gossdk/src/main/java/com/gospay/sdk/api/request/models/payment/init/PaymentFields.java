package com.gospay.sdk.api.request.models.payment.init;

import java.util.Currency;

/**
 * Created by bertalt on 08.09.16.
 */
public class PaymentFields {

    private double price;
    private Currency currency;
    private String description;
    private String order;

    public PaymentFields(double amount, String currency, String description, String order) {
        this.price = amount;
        this.currency = Currency.getInstance(currency);
        this.description = description;
        this.order = order;
    }

    public double getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getOrder() {
        return order;
    }
}
