package com.gospay.sdk.api.listeners;

import com.gospay.sdk.api.response.models.messages.card.CardView;

/**
 * Created by bertalt on 16.09.16.
 */
public interface GosSelectCardListener {

    void onSuccessSelectCard(CardView card);
    void onDismissSelectCard();
}
