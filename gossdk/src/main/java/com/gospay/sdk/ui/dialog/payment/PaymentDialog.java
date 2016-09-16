package com.gospay.sdk.ui.dialog.payment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gospay.sdk.GosSdkManager;
import com.gospay.sdk.R;
import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.PaymentFields;
import com.gospay.sdk.api.response.models.messages.card.CardView;
import com.gospay.sdk.api.response.models.messages.payment.Payment;
import com.gospay.sdk.ui.UiUtil;
import com.gospay.sdk.ui.dialog.card.select.CardListAdapter;
import com.gospay.sdk.ui.dialog.card.select.OnCardClickListener;
import com.gospay.sdk.util.Logger;

import java.util.List;

/**
 * Created by bertalt on 16.09.16.
 */
public final class PaymentDialog extends DialogFragment {

    public static final String TAG = "PaymentDialog";
    private TextView tvCurrency, tvDescr, tvAmount, tvOrderId;

    private RecyclerView recyclerView;
    private CardListAdapter cardListAdapter;
    private CardView selectedCard;
    private Button btnPay;
    private PaymentFields paymentFields;


    private PaymentDialog(){

    }

    public static final String KEY_CURRENCY="bundle_currency";
    public static final String KEY_DESCRIPTION="bundle_descr";
    public static final String KEY_AMOUNT="bundle_amount";
    public static final String KEY_ORDER_ID="bundle_order";

    public static PaymentDialog newInstance(){
        return new PaymentDialog();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        return buildDialog();

    }



    private Dialog buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_payment, null);

        recyclerView = (RecyclerView) view.findViewById(R.id.dialog_payment_recycler);

        btnPay = (Button)view.findViewById(R.id.dialog_payment_btn_pay);
        btnPay.setOnClickListener(onClickPay);

        builder.setView(view);

        tvAmount = (TextView)view.findViewById(R.id.dialog_payment_amount);
        tvCurrency = (TextView)view.findViewById(R.id.dialog_payment_currency);
        tvDescr = (TextView)view.findViewById(R.id.dialog_payment_description);
        tvOrderId = (TextView)view.findViewById(R.id.dialog_payment_order_id);

        bindView();

        return builder.create();
    }

    private View.OnClickListener onClickPay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(selectedCard != null)
            {
                InitPaymentParameter parameter = new InitPaymentParameter(selectedCard.getCardId(), paymentFields);
                GosNetworkManager.getInstance().initPayment(parameter, gosInitPaymentListener);
                UiUtil.getDefaultProgressDialog(getActivity()).show();
                dismiss();
            }else
                Toast.makeText(getContext(), getString(R.string.hint_select_card), Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    private void bindView(){

        Bundle args = getArguments();

        if(args != null){

            paymentFields = new PaymentFields(args.getDouble(KEY_AMOUNT), args.getString(KEY_CURRENCY), args.getString(KEY_DESCRIPTION), args.getString(KEY_ORDER_ID));

            tvDescr.setText(paymentFields.getDescription());
            tvOrderId.setText(paymentFields.getOrder());
            tvAmount.setText(String.valueOf(paymentFields.getPrice()));
            tvCurrency.setText(paymentFields.getCurrency().getCurrencyCode());
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        List<CardView> list = GosSdkManager.getInstance().getCachedCardList();
        if(list.size() != 0) {
            cardListAdapter = new CardListAdapter(list, onCardClickListener);
            recyclerView.setAdapter(cardListAdapter);
        }else{
            Toast.makeText(getContext(), "Empty list", Toast.LENGTH_SHORT).show();
        }

        }

    OnCardClickListener onCardClickListener = new OnCardClickListener() {
        @Override
        public void OnItemClicked(CardView cardView) {
            selectedCard = cardView;
        }
    };

    GosInitPaymentListener gosInitPaymentListener = new GosInitPaymentListener() {
        @Override
        public void onSuccessInitPayment(Payment paymentInProgress) {
            Logger.LOGD("Success ##############");

        }

        @Override
        public void onFailureInitPayment(String message) {
            Logger.LOGD("Failed ##############");
        }
    };

}
