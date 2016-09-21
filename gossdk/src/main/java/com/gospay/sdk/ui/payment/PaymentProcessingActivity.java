package com.gospay.sdk.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gospay.sdk.R;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.ui.payment.fragment.InitPaymentFragment;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.TextUtils;

public class PaymentProcessingActivity extends AppCompatActivity {

    private static final String TAG = PaymentProcessingActivity.class.getName();

    private Fragment currentFragment;
    private String mTagCurrentFragment;
    private static String KEY_CURRENT_TAG = "instance_key_current_tag";


    public interface PaymentContract {


        String KEY_PAYMENT_FIELDS = "ikpf";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);
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

    private void showCurrentFragment(String tag) {

        FragmentManager fm = getSupportFragmentManager();
        currentFragment = fm.findFragmentByTag(tag);
        if (currentFragment == null) {
            Log.e(TAG, "Cannot find fragment by restore tag");
            showFirstFragment(fm);
        }
    }

    private void showFirstFragment(FragmentManager fm) {

        Logger.LOGD("Show first fragment");

        Intent args = getIntent();
        if (args == null) throw new GosSdkException("Intent with fields is null");
        Fragment fragment = new InitPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(InitPaymentFragment.InitContract.KEY_PAYMENT_FIELDS,
                args.getStringExtra(PaymentContract.KEY_PAYMENT_FIELDS));
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
