package com.gospay.sdk.ui.payment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gospay.sdk.GosSdkManager;
import com.gospay.sdk.R;
import com.gospay.sdk.api.listeners.GosConfirmationPaymentListener;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.api.response.models.messages.payment.Payment;
import com.gospay.sdk.exceptions.GosInvalidCardFieldsException;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.ui.payment.PaymentProcessingActivity;
import com.gospay.sdk.util.CreditCardValidator;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;

/**
 * Created by bertalt on 19.09.16.
 */
public class ConfirmPaymentFragment extends Fragment {

    public static final String KEY_PAYLOAD = "bundle_key_payload";
    public static final String KEY_CARD_VIEW = "bundle_key_card_view";
    public static final String KEY_PAYMENT_FIELDS = "bundle_key_payment_fields";
    public static final String TAG = ConfirmPaymentFragment.class.getSimpleName();
    private TextView tvCardMask, tvCardAlias;
    private EditText etCvv;
    private ProgressBar requestProgress;
    private CardViewModel selectedCard;
    private ViewGroup view;
    private Button btnConfirm;
    private Payment payment;
    private CardViewModel cardViewModel;
    private CardView cardLayout;
    private boolean shouldShowNext;
    private Gson gson = new Gson();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null)
            view = (ViewGroup) inflater.inflate(R.layout.com_gos_confirm_payment_fragment, container, false);

        tvCardAlias = (TextView) view.findViewById(R.id.card_view_card_alias);
        tvCardMask = (TextView) view.findViewById(R.id.card_view_card_mask);
        requestProgress = (ProgressBar) view.findViewById(R.id.confirm_request_progress);
        btnConfirm = (Button) view.findViewById(R.id.fragment_payment_confirm_btn_next);
        btnConfirm.setOnClickListener(onClickConfirm);
        cardLayout = (android.support.v7.widget.CardView) view.findViewById(R.id.layout_card_view);

        etCvv = (EditText) view.findViewById(R.id.confirm_payment_edit_cvv);
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
            payment = gson.fromJson(getArguments().getString(KEY_PAYLOAD), Payment.class);
            Logger.LOGD("Parsed payment " + payment.toString());
        }

        if (cardViewModel == null) {
            cardViewModel = gson.fromJson(getArguments().getString(KEY_CARD_VIEW), CardViewModel.class);
            Logger.LOGD("Parse card_view " + cardViewModel.toString());
        }

        if (cardViewModel != null) {
            tvCardAlias.setText(cardViewModel.getAlias());
            tvCardMask.setText(cardViewModel.getCardMask());
        }
    }

    private View.OnClickListener onClickConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                GosSdkManager.getInstance().confirmPayment(getContext(),payment, etCvv.getText().toString(), gosConfirmListener);
                requestProgress.setVisibility(View.VISIBLE);

            } catch (GosInvalidCardFieldsException e) {
                Toast.makeText(getContext(), getString(R.string.error_invalid_cvv), Toast.LENGTH_SHORT).show();
            }


        }

    };

    GosConfirmationPaymentListener gosConfirmListener = new GosConfirmationPaymentListener() {

        @Override
        public void onSuccessConfirmationPayment(String message) {

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
        getFragmentManager().beginTransaction()
                .replace(R.id.activity_payment_processing_fragment_container_confirm, fragment, ConfirmPaymentFragment.TAG)
                .commit();
        ((PaymentProcessingActivity) getActivity()).setTag(TrackPaymentFragment.TAG);

        btnConfirm.setVisibility(View.GONE);
        cardLayout.setVisibility(View.GONE);

        shouldShowNext = false;
    }

}

