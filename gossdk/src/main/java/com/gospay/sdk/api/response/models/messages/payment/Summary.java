package com.gospay.sdk.api.response.models.messages.payment;

/**
 * Created by bertalt on 13.09.16.
 */
public class Summary {

    double amount;
    String currency;

    private Summary(){}

    public Summary(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
