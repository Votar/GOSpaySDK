package com.gospay.sdk.api.request.models.card;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bertalt on 30.08.16.
 */
public class AddCardParameter {

    @SerializedName("card")
    private CardFields cardFields;


    public AddCardParameter(CardFields cardFields) {
        this.cardFields = cardFields;

    }

    public CardFields getCardFields() {
        return cardFields;
    }

}
