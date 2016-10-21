package com.gospay.ui.payment.design;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gospay.ui.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InitPaymentDesignFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;

    public InitPaymentDesignFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_init_payment_design, container, false);

        swipeRefreshLayout =(SwipeRefreshLayout)view.findViewById(R.id.init_payment_swiperefrash);

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);


        return view;
    }


    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            requestCardList();
        }
    };

    private void requestCardList() {
        
    }
}
