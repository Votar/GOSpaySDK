package com.gospay.sdk.api.request.models.payment;

/**
 * Created by bertalt on 16.09.16.
 */
public class OrderModel {

    private double amount;
    private String currency;
    private String description;
    private String order;

    public OrderModel(double amount, String currency, String description, String order) {
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.order = order;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getOrder() {
        return order;
    }
}
