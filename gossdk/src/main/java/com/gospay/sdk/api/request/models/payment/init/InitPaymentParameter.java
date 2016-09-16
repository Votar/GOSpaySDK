package com.gospay.sdk.api.request.models.payment.init;


import com.google.gson.annotations.SerializedName;
import com.gospay.sdk.api.request.models.payment.OrderModel;

/**
 * Created by bertalt on 08.09.16.
 */
public class InitPaymentParameter {

    @SerializedName("cardEntityId")
    private String card;

    private OrderModel order;

    public InitPaymentParameter(String card, PaymentFields fields) {
        this.card = card;

        order = new OrderModel(fields.getPrice(),
                fields.getCurrency().getCurrencyCode(),
                fields.getDescription(),
                fields.getOrder());
    }

    public String getCard() {
        return card;
    }


}
