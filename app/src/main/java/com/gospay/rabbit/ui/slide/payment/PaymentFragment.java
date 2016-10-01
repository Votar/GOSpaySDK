package com.gospay.rabbit.ui.slide.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gospay.rabbit.gossdkrabbit.R;
import com.gospay.sdk.api.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.response.models.messages.payment.GosPayment;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bertalt on 14.09.16.
 */
public class PaymentFragment extends Fragment {

    @OnClick(R.id.fragment_add_card_btn)
    void OnClickBtn() {


    }

    GosInitPaymentListener gosInitPaymentListener = new GosInitPaymentListener() {


        @Override
        public void onSuccessInitPayment(GosPayment createdPayment) {

        }

        @Override
        public void onFailureInitPayment(String message) {

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
