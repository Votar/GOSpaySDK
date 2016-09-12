package com.gospay.sdk.api.response.listeners;

import com.gospay.sdk.api.response.models.messages.card.CardView;
import com.gospay.sdk.api.response.models.messages.error.ErrorJsonModel;

import java.util.ArrayList;

/**
 * Created by bertalt on 07.09.16.
 */
public interface GosGetCardListListener {

    void onGetCardListSuccess(ArrayList<CardView> cardList);
    void onGetCardListFailure(String message);
}
