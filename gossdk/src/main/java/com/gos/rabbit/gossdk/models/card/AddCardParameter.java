package com.gos.rabbit.gossdk.models.card;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.gos.rabbit.gossdk.util.DeviceInfo;

/**
 * Created by bertalt on 30.08.16.
 */
public class AddCardParameter {

    @SerializedName("card")
    private CardFields cardFields;
    private String token = "";
    private DeviceInfo deviceInfo;

    public AddCardParameter(CardFields cardFields, String token) {
        this.cardFields = cardFields;
        this.token = token;
    }
    public AddCardParameter(CardFields cardFields, DeviceInfo deviceInfo) {
        this.cardFields = cardFields;
        this.deviceInfo = deviceInfo;
    }

    public CardFields getCardFields() {
        return cardFields;
    }

    public String getToken() {
        return token;
    }
}
