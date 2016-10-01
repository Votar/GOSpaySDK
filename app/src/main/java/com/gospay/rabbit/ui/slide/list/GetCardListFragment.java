package com.gospay.rabbit.ui.slide.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.style.EasyEditSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gospay.rabbit.gossdkrabbit.R;
import com.gospay.sdk.GosSdkManager;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.listeners.GosRemoveCardListener;
import com.gospay.sdk.api.listeners.GosSelectCardListener;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.ui.GosEasyManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bertalt on 14.09.16.
 */
public class GetCardListFragment extends Fragment {

    private static final String TAG = GetCardListFragment.class.getSimpleName();

    @OnClick(R.id.fragment_get_card_list_btn)
    void OnClickBtn() {
        GosEasyManager.selectCardWithDialog(getActivity(), new GosSelectCardListener() {
            @Override
            public void onSuccessSelectCard(final CardViewModel card) {
                GosSdkManager.create(getActivity());
                GosSdkManager.getInstance().removeCard(getActivity(), card, new GosRemoveCardListener() {
                    @Override
                    public void onSuccessRemoveCard() {
                        Toast.makeText(getContext(), "Card removed "+card.getCardMask(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailureRemoveCard(String message) {
                        Log.e(TAG, "removing failed");
                    }
                });
            }

            @Override
            public void onDismissSelectCard() {

            }
        });
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
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_get_card_list, container, false);
        ButterKnife.bind(this,view);
        return view;
    }


}
