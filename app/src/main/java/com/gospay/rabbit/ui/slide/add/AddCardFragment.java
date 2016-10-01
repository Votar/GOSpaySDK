package com.gospay.rabbit.ui.slide.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gospay.rabbit.gossdkrabbit.R;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.ui.GosEasyManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bertalt on 14.09.16.
 */
public class AddCardFragment extends Fragment {

    private static final String TAG = AddCardFragment.class.getSimpleName();

    @OnClick(R.id.fragment_add_card_btn)
    void OnClickBtn() {
       GosEasyManager.addCardWithDialog(getActivity(), gosAddCardListener);
    }

    GosAddCardListener gosAddCardListener = new GosAddCardListener() {
        @Override
        public void onSuccessAddCard(CardViewModel card) {

            Toast.makeText(getContext(), String.format("Card 1%s has been added", card.getCardMask()), Toast.LENGTH_SHORT).show();
            Log.d(TAG, Thread.currentThread().getName());
        }

        @Override
        public void onFailureAddCard(String message) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_add_card, container, false);
        ButterKnife.bind(this,view);
        return view;
    }


}
