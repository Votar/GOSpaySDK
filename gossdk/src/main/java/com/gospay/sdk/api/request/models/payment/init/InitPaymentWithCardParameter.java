package com.gospay.sdk.api.request.models.payment.init;


import com.google.gson.annotations.SerializedName;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.request.models.payment.GosDeviceInfo;
import com.gospay.sdk.api.request.models.payment.OrderModel;

/**
 * Created by bertalt on 08.09.16.
 */
public class InitPaymentWithCardParameter {

    @SerializedName("card")
    private CardFields card;

    private OrderModel order;

    @SerializedName("device")
    private GosDeviceInfo deviceInfo;

    public InitPaymentWithCardParameter(CardFields card, PaymentFields fields, GosDeviceInfo deviceInfo) {

        this.card = card;

        order = new OrderModel(fields.getPrice(),
                fields.getCurrency().getCurrencyCode(),
                fields.getDescription(),
                fields.getOrder());

        this.deviceInfo = deviceInfo;
    }


}
