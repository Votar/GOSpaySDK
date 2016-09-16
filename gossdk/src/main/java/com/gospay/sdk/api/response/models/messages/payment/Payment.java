package com.gospay.sdk.api.response.models.messages.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bertalt on 08.09.16.
 */
public class Payment {

    @SerializedName("id")
    private String id;
    private Summary amount;
    private Summary total;

    public Payment(String id, Summary amount, Summary total) {
        this.id = id;
        this.amount = amount;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public Summary getAmount() {
        return amount;
    }

    public Summary getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", total=" + total +
                '}';
    }
}
