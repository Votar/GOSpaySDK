package com.gospay.sdk.api.request.models.card;

import com.google.gson.annotations.SerializedName;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;

/**
 * Created by bertalt on 30.08.16.
 */
public class RemoveCardParameter {

    @SerializedName("cardEntityId")
    private String cardUid;

    public RemoveCardParameter(CardViewModel card) {
                cardUid = card.getCardId();
    }

}
