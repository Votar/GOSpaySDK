package com.gospay.sdk.api.response.models.messages.card;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bertalt on 01.09.16.
 */
public class CardViewModel {

    private CardViewModel(){}

    @SerializedName("cardEntityId")
    private String cardId;
    @SerializedName("info")
    private String cardMask;
    @SerializedName("cardAlias")
    private String cardAlias;

    public String getCardId() {
        return cardId;
    }

    public String getCardMask() {
        return cardMask;
    }

    public String getCardAlias() {
        return cardAlias;
    }


    @Override
    public String toString() {
        return "CardViewModel{" +
                "cardId='" + cardId + '\'' +
                ", cardMask='" + cardMask + '\'' +
                ", cardAlias='" + cardAlias + '\'' +
                '}';
    }
}
