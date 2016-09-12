package com.gospay.sdk.api.response.models.messages.card;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bertalt on 01.09.16.
 */
public class CardView {

    @SerializedName("cardEntityId")
    private String cardId;
    @SerializedName("info")
    private String cardMask;
    @SerializedName("alias")
    private String alias;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardMask() {
        return cardMask;
    }

    public void setCardMask(String cardMask) {
        this.cardMask = cardMask;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    @Override
    public String toString() {
        return "CardView{" +
                "cardId='" + cardId + '\'' +
                ", cardMask='" + cardMask + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}
