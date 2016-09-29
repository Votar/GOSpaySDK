package com.gospay.sdk.api.response.models.messages.payment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bertalt on 08.09.16.
 */
public class GosPayment {

    private GosPayment(){}

    @SerializedName("id")
    private String id;
    private GosSummary amount;
    private GosSummary total;
    private String status;
    @SerializedName("d3SecureUrl")
    private String d3SecureUrl ="http:\\www.google.com";



    public String getId() {
        return id;
    }

    public GosSummary getAmount() {
        return amount;
    }

    public GosSummary getTotal() {
        return total;
    }

    public String getStatus() {
        return status;
    }

    public String getD3SecureUrl() {
        return d3SecureUrl;
    }

    @Override
    public String toString() {
        return "GosPayment{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", total=" + total +
                ", status='" + status + '\'' +
                '}';
    }
}
