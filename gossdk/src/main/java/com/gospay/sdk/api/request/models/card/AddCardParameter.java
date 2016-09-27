package com.gospay.sdk.api.request.models.card;

import com.google.gson.annotations.SerializedName;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.util.DeviceInfo;

/**
 * Created by bertalt on 30.08.16.
 */
public class AddCardParameter {

    @SerializedName("card")
    private CardFields cardFields;
    private DeviceInfo deviceInfo;

    public AddCardParameter(CardFields cardFields) {
        this.cardFields = cardFields;

    }

    public AddCardParameter(CardFields cardFields, DeviceInfo deviceInfo) {
        this.cardFields = cardFields;
        this.deviceInfo = deviceInfo;
    }

    public CardFields getCardFields() {
        return cardFields;
    }

}
