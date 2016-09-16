package com.gospay.sdk.ui.dialog.card.select;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gospay.sdk.R;
import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosGetCardListListener;
import com.gospay.sdk.api.listeners.GosSelectCardListener;
import com.gospay.sdk.api.response.models.messages.card.CardView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bertalt on 13.09.16.
 */
public class SelectCardDialog extends DialogFragment {

    public static final String TAG = "SelectCardDialog";

    private GosSelectCardListener selectCardListener;

    private View progressBar;
    private RecyclerView recyclerView;
    private CardListAdapter cardListAdapter;
    private TextView emptyView;
    private List<CardView> cardList = new ArrayList<>();


    private ProgressDialog progressDialog;

    public SelectCardDialog() {

    }

    @SuppressLint("ValidFragment")
    private SelectCardDialog(GosSelectCardListener listener) {

        this.selectCardListener = listener;
    }

    @SuppressLint("ValidFragment")
    private SelectCardDialog(GosSelectCardListener listener, List<CardView> cardList) {

        this.selectCardListener = listener;
        this.cardList.addAll(cardList);
    }

    public static SelectCardDialog newInstance(GosSelectCardListener listener) {

        return new SelectCardDialog(listener);
    }
    public static SelectCardDialog newInstance(GosSelectCardListener listener, List<CardView> cardList) {

        return new SelectCardDialog(listener, cardList);
    }

    @Override
    public void onStart() {
        super.onStart();

        setRetainInstance(true);

        if (cardList.size() == 0)
            getDataSet();
        else{
            setupRecycler(cardList);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_select_card, null);

        progressBar = view.findViewById(R.id.dialog_select_card_progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.dialog_select_card_recycler);

        emptyView = (TextView) view.findViewById(R.id.dialog_select_card_empty_view);

//        builder.setNegativeButton(getString(R.string.ui_btn_cancel), onClickCancel );

        setupView();

        builder.setView(view);

        return builder.create();
    }


    private DialogInterface.OnClickListener onClickCancel = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dismiss();
        }
    };


    private void setupView() {

        if (cardListAdapter == null) {
            if (progressBar != null && recyclerView != null) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

            }
        } else {
            if (cardListAdapter.getItemCount() == 0) {
                if (emptyView != null && recyclerView != null) {
                    recyclerView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);

                }
            } else {
                if (emptyView != null && recyclerView != null && progressBar != null) {
                    emptyView.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setupRecycler(List<CardView> cardList){

        cardListAdapter = new CardListAdapter(cardList, onCardClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(cardListAdapter);
        setupView();
    }

    private void getDataSet() {

        GosNetworkManager.getInstance().getCardList(new GosGetCardListListener() {
            @Override
            public void onGetCardListSuccess(ArrayList<CardView> cardList) {

                setupRecycler(cardList);
            }

            @Override
            public void onGetCardListFailure(String message) {

                cardListAdapter = new CardListAdapter(new ArrayList<CardView>(), onCardClickListener);
                emptyView.setText(message);
                setupView();
            }
        });

    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    OnCardClickListener onCardClickListener = new OnCardClickListener() {
        @Override
        public void OnItemClicked(CardView cardView) {
            dismiss();
            selectCardListener.onSuccessSelectCard(cardView);
        }
    };
}
