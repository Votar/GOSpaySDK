package com.gospay.sdk.api.response.listeners;

import com.gospay.sdk.api.response.models.messages.card.CardView;

/**
 * Created by bertalt on 12.09.16.
 */
public interface GosAddCardListener {
    void onSuccessAddCard(CardView card);
    void onFailureAddCard(String message);
}
