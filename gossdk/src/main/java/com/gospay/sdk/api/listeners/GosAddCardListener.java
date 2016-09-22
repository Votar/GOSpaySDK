package com.gospay.sdk.api.listeners;

import com.gospay.sdk.api.response.models.messages.card.CardViewModel;

/**
 * Created by bertalt on 12.09.16.
 */
public interface GosAddCardListener {
    void onSuccessAddCard(CardViewModel card);
    void onFailureAddCard(String message);
}
