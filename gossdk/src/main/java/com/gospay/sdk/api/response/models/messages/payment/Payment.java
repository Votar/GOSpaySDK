package com.gospay.sdk.api.response.models.messages.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bertalt on 08.09.16.
 */
public class Payment {

    private Payment(){}

    @SerializedName("id")
    private String id;
    private Summary amount;
    private Summary total;
    private String status;
    private String url ="http:\\www.google.com";



    public String getId() {
        return id;
    }

    public Summary getAmount() {
        return amount;
    }

    public Summary getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", total=" + total +
                ", status='" + status + '\'' +
                '}';
    }
}
