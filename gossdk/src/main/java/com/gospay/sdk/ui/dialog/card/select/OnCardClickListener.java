package com.gospay.sdk.ui.dialog.card.select;

import com.gospay.sdk.api.response.models.messages.card.CardViewModel;

/**
 * Created by bertalt on 16.09.16.
 */
public interface OnCardClickListener {

    void OnItemClicked(CardViewModel cardViewModel);
}
