package com.gospay.sdk.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gospay.sdk.R;
import com.gospay.sdk.api.request.models.payment.init.PaymentFields;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.ui.payment.fragment.InitPaymentFragment;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;
import com.gospay.sdk.util.TextUtils;

public class PaymentProcessingActivity extends AppCompatActivity {

    private static final String TAG = PaymentProcessingActivity.class.getName();

    private String mTagCurrentFragment;
    private static String KEY_CURRENT_TAG = "instance_key_current_tag";
    private PaymentFields paymentFields;


    public interface PaymentContract {


        String KEY_PAYMENT_FIELDS = "ikpf";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);

        TextView tvDescr = (TextView) findViewById(R.id.fragment_payment_description);
        TextView tvAmount = (TextView) findViewById(R.id.fragment_payment_payment_amount);
        TextView tvCurrency = (TextView) findViewById(R.id.fragment_payment_payment_currency);
        TextView tvOrderId = (TextView) findViewById(R.id.fragment_payment_order_id);

        Intent args = getIntent();
        if (args == null) throw new GosSdkException("Intent with fields is null");

        paymentFields = Parser.getsInstance().fromJson(args.getStringExtra(PaymentContract.KEY_PAYMENT_FIELDS), PaymentFields.class);

        if (paymentFields == null) throw new GosSdkException("Payment cannot be parsed");

        tvDescr.setText(paymentFields.getDescription());
        tvOrderId.setText(paymentFields.getOrder());
        tvAmount.setText(String.valueOf(paymentFields.getPrice()));
        tvCurrency.setText(paymentFields.getCurrency().getCurrencyCode());
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mTagCurrentFragment != null)
            outState.putString(KEY_CURRENT_TAG, mTagCurrentFragment);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TextUtils.isEmpty(mTagCurrentFragment))
            showFirstFragment(getSupportFragmentManager());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null)
            mTagCurrentFragment = savedInstanceState.getString(KEY_CURRENT_TAG);
    }

    private void showFirstFragment(FragmentManager fm) {

        Logger.LOGD("Show first fragment");


        Fragment fragment = new InitPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InitPaymentFragment.InitContract.KEY_PAYMENT_FIELDS, Parser.getsInstance().toJson(paymentFields, PaymentFields.class));

        fragment.setArguments(bundle);

        fm.beginTransaction().
                replace(R.id.activity_payment_processing_fragment_container, fragment, InitPaymentFragment.TAG)
                .commit();

        setTag(InitPaymentFragment.TAG);
    }

    public void setTag(String tag) {
        mTagCurrentFragment = tag;
    }
}
