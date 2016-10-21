package com.gospay.ui.payment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gospay.sdk.api.response.models.messages.payment.GosPayment;
import com.gospay.ui.R;
import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosConfirmationPaymentListener;
import com.gospay.sdk.api.request.models.payment.confirm.ConfirmationPaymentParameter;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.exceptions.GosInvalidCardFieldsException;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.ui.payment.PaymentProcessingActivity;
import com.gospay.sdk.util.CreditCardValidator;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bertalt on 19.09.16.
 */
public class ConfirmPaymentFragment extends Fragment {

    public static final String KEY_PAYLOAD = "bundle_key_payload";
    public static final String KEY_CARD_VIEW = "bundle_key_card_view";
//    public static final String KEY_PAYMENT_FIELDS = "bundle_key_payment_fields";
    public static final String TAG = ConfirmPaymentFragment.class.getSimpleName();
    private TextView tvCardMask, tvCardAlias, tvPaymentAmount, tvTotalAmount, tvPaymentCurrency, tvTotalCurrency;
    private EditText etCvv;
    private ProgressBar requestProgress;
    private ViewGroup view;
    private ImageView btnConfirm;
    private GosPayment payment;
    private CardViewModel cardViewModel;
    private FrameLayout cardLayout;
    private boolean shouldShowNext;
    private Gson gson = new Gson();
    private GosNetworkManager networkManager = GosNetworkManager.getInstance();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = (ViewGroup) inflater.inflate(R.layout.com_gos_confirm_payment_fragment, container, false);

            tvCardAlias = (TextView) view.findViewById(R.id.card_view_card_alias);
            tvCardMask = (TextView) view.findViewById(R.id.card_view_card_mask);
            requestProgress = (ProgressBar) view.findViewById(R.id.confirm_request_progress);
            btnConfirm = (ImageView) view.findViewById(R.id.fragment_payment_confirm_btn_next);
            btnConfirm.setOnClickListener(onClickConfirm);
            cardLayout = (FrameLayout) view.findViewById(R.id.layout_card_view);

            tvTotalAmount = (TextView) view.findViewById(R.id.fragment_payment_confirm_total_amount);
            tvTotalCurrency = (TextView) view.findViewById(R.id.fragment_payment_confirm_total_currency);

            tvPaymentAmount = (TextView) view.findViewById(R.id.fragment_payment_confirm_payment_amount);
            tvPaymentCurrency = (TextView) view.findViewById(R.id.fragment_payment_confirm_payment_currency);

            etCvv = (EditText) view.findViewById(R.id.confirm_payment_edit_cvv);
        }

        List<Object> sd = new ArrayList<>();

        bindView();
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (shouldShowNext)
            showTrackStatusFragment();
    }


    private void bindView() {

        Bundle args = getArguments();

        if (args == null)
            throw new GosSdkException("No data to payment view");


        if (payment == null) {
            payment = gson.fromJson(getArguments().getString(KEY_PAYLOAD), GosPayment.class);
            Logger.LOGD("Parsed payment " + payment.toString());
        }
        if(payment!=null){

            tvPaymentAmount.setText(String.valueOf(payment.getAmount().getAmount()));
            tvPaymentCurrency.setText(payment.getAmount().getCurrency());
            tvTotalAmount.setText(String.valueOf(payment.getTotal().getAmount()));
            tvTotalCurrency.setText(String.valueOf(payment.getTotal().getCurrency()));

        }

        if (cardViewModel == null) {
            cardViewModel = gson.fromJson(getArguments().getString(KEY_CARD_VIEW), CardViewModel.class);
            Logger.LOGD("Parse card_view " + cardViewModel.toString());
        }

        if (cardViewModel != null) {
            tvCardAlias.setText(cardViewModel.getCardAlias());
            tvCardMask.setText(cardViewModel.getCardMask());
        }


    }

    private View.OnClickListener onClickConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                String cvv = etCvv.getText().toString();
                if (!CreditCardValidator.isCvvValid(cvv))
                    throw new GosInvalidCardFieldsException(String.format("Cvv is not valid: %1s", cvv), GosInvalidCardFieldsException.GosInputField.CVV);

                ConfirmationPaymentParameter param = new ConfirmationPaymentParameter(payment.getId(), cvv);

                networkManager.confirmationPayment(getContext(), param, gosConfirmListener);

                requestProgress.setVisibility(View.VISIBLE);

            } catch (GosInvalidCardFieldsException e) {
                Toast.makeText(getContext(), getString(R.string.error_invalid_cvv), Toast.LENGTH_SHORT).show();
            }


        }

    };

    GosConfirmationPaymentListener gosConfirmListener = new GosConfirmationPaymentListener() {

        @Override
        public void onSuccessConfirmationPayment() {

            shouldShowNext = true;

            if (isResumed()) {
                showTrackStatusFragment();
            }

            requestProgress.setVisibility(View.GONE);
        }

        @Override
        public void onFailureConfirmationPayment(String message) {
            Toast.makeText(getContext(), getString(R.string.error_from_server, message), Toast.LENGTH_SHORT).show();
            requestProgress.setVisibility(View.GONE);
        }
    };

    private void showTrackStatusFragment() {

        Gson gson = Parser.getsInstance();
        Bundle args = new Bundle();
        args.putString(TrackPaymentFragment.KEY_PAYMENT, getArguments().getString(KEY_PAYLOAD));
        Fragment fragment = new TrackPaymentFragment();

        fragment.setArguments(args);

        clearBackStack();

        getFragmentManager().beginTransaction()
                .replace(R.id.activity_payment_processing_fragment_container, fragment, TrackPaymentFragment.TAG)
                .commit();
        ((PaymentProcessingActivity) getActivity()).setTag(TrackPaymentFragment.TAG);

        btnConfirm.setVisibility(View.GONE);
        cardLayout.setVisibility(View.GONE);


        shouldShowNext = false;
    }

    private void clearBackStack() {

        for (int i = 0; i < getFragmentManager().getBackStackEntryCount(); i++) {
            getFragmentManager().popBackStack();
        }
    }

}

