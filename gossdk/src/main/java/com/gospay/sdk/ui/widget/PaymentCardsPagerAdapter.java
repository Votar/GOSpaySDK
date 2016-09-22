package com.gospay.sdk.ui.widget;

import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gospay.sdk.R;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;

import java.util.ArrayList;
import java.util.List;


public class PaymentCardsPagerAdapter extends PagerAdapter {

    private List<CardViewModel> mDataset = new ArrayList<>();
    public PaymentCardsPagerAdapter(List<CardViewModel> dataset) {
        super();
        this.mDataset.clear();
        mDataset.addAll(dataset);
    }




    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View v = LayoutInflater.from(collection.getContext()).inflate(R.layout.item_recycler_credit_card, collection, false);

        TextView cardMask = (TextView) v.findViewById(R.id.recycler_card_mask);
        TextView cardAlias = (TextView) v.findViewById(R.id.recycler_card_alias);

        cardMask.setText(mDataset.get(position).getCardMask());
        cardAlias.setText(mDataset.get(position).getAlias());
        /*boolean isConfirmed = Boolean.parseBoolean(cursor.getString(AccountsQuery.IS_CONFIRMED));
        if (isConfirmed) {
            lytCard.setBackgroundDrawable(collection.getContext().getResources().getDrawable(R.drawable.card_front));
            imageAttention.setVisibility(View.GONE);
        }*/

        collection.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


}
