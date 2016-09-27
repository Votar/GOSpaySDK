package com.gospay.sdk.api.listeners;


import com.gospay.sdk.api.response.models.messages.card.CardViewModel;

import java.util.ArrayList;

/**
 * Created by bertalt on 07.09.16.
 */
public interface GosGetCardListListener {

    void onGetCardListSuccess(ArrayList<CardViewModel> cardList);
    void onGetCardListFailure(String message);
}
