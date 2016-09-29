package com.gospay.sdk.api.response.models.messages.payment;

/**
 * Created by bertalt on 13.09.16.
 */
public class GosSummary {

    double amount;
    String currency;

    private GosSummary(){}

    public GosSummary(double amount, String currency) {
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
        return "GosSummary{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
    }
}
