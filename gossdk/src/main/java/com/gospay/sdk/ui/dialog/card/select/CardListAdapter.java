package com.gospay.sdk.ui.dialog.card.select;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gospay.sdk.R;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bertalt on 14.09.16.
 */
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private List<CardViewModel> mDataset = new ArrayList<>();
    private final OnCardClickListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View rootView;
        public TextView cardMask;
        public TextView cardAlias;
        public int position;

        public ViewHolder(View v) {
            super(v);
            rootView = v;
            cardMask = (TextView) rootView.findViewById(R.id.recycler_card_mask);
            cardAlias = (TextView) rootView.findViewById(R.id.recycler_card_alias);
        }

        public void bind(final CardViewModel item, final OnCardClickListener listener) {
            cardMask.setText(item.getCardMask());
            cardAlias.setText(item.getAlias());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClicked(item);
                }
            });
        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardListAdapter(List<CardViewModel> myDataset, OnCardClickListener listener) {

        mDataset.addAll(myDataset);
        this.listener = listener;
    }

    public void swap(ArrayList<CardViewModel> data) {
        mDataset.clear();
        mDataset.addAll(data);
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recycler_credit_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.bind(mDataset.get(position), listener);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}