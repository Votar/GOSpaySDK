package com.gospay.sdk.api.listeners;


import com.gospay.sdk.api.response.models.messages.card.CardViewModel;

/**
 * Created by bertalt on 16.09.16.
 */
public interface GosSelectCardListener {

    void onSuccessSelectCard(CardViewModel card);
    void onDismissSelectCard();
}
