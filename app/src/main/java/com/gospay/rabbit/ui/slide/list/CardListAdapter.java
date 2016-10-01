package com.gospay.rabbit.ui.slide.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gospay.rabbit.gossdkrabbit.R;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bertalt on 14.09.16.
 */
public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {
    private List<CardViewModel> mDataset = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View rootView;
        public TextView cardMask;
        public TextView cardAlias;

        public ViewHolder(View v) {
            super(v);
            rootView = v;
            cardMask = (TextView)rootView.findViewById(R.id.recycler_card_mask);
            cardAlias = (TextView)rootView.findViewById(R.id.recycler_card_alias);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CardListAdapter( List<CardViewModel> myDataset) {

        mDataset.addAll(myDataset);

    }

    public void swap(ArrayList<CardViewModel> data){
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
                .inflate(R.layout.item_credit_card_app, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.cardMask.setText(mDataset.get(position).getCardMask());
        holder.cardAlias.setText(mDataset.get(position).getCardAlias());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}