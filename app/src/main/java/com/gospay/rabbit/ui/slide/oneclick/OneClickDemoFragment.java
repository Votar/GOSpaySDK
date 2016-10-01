package com.gospay.rabbit.ui.slide.oneclick;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gospay.rabbit.gossdkrabbit.R;
import com.gospay.sdk.exceptions.GosInvalidPaymentFieldsException;
import com.gospay.ui.GosEasyManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bertalt on 14.09.16.
 */
public class OneClickDemoFragment extends Fragment {


    private static final String TAG = "OneClickDemoFragment";

    @OnClick(R.id.fragment_one_click_slide_btn)
    void OnClickBtn() {
        try {

            GosEasyManager.processPayment(getActivity(), 1200, "USD", "iPhone 7 32GB", "4546/re");

        } catch (GosInvalidPaymentFieldsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_one_click_slide, container, false);
        ButterKnife.bind(this, view);
        return view;

    }


}
