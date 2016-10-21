package com.gospay.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.ui.R;
import com.gospay.ui.card.add.AddCardActivity;
import com.gospay.ui.payment.stuff.InitPaymentFragment;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;


public class PaymentProcessingActivity extends AppCompatActivity {

    private static final String TAG = PaymentProcessingActivity.class.getName();
    private int REQ_ADD_CARD =0x23;
    private String mTagCurrentFragment;
    private static String KEY_CURRENT_TAG = "instance_key_current_tag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_processing);


        if (savedInstanceState == null)
            showFirstFragment();

    }

    private void showFirstFragment() {
        Fragment fragment = new InitPaymentFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_payment_processing_fragment_container, fragment)
                .commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mTagCurrentFragment != null)
            outState.putString(KEY_CURRENT_TAG, "changed");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    private GosAddCardListener addCardListener;
    public void startAddCardActivity(GosAddCardListener listener) {

        Logger.LOGD("Show first fragment");

        addCardListener = listener;
        Intent intent = new Intent(this, AddCardActivity.class);
        startActivityForResult(intent, REQ_ADD_CARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_ADD_CARD){
            if(resultCode == RESULT_OK){
                //Card has been added
                //Received CardViewModel in ARG_CARD_VIEW_MODEL and saved PaymentFields in ARG_PAYMENT_FIELDS

                String cardViewJson = data.getStringExtra(AddCardActivity.AddCardContract.ARG_CARD_VIEW_MODEL);
                CardViewModel card = Parser.getsInstance().fromJson(cardViewJson, CardViewModel.class);

                //Send result to listener if need
                if(addCardListener != null)
                    addCardListener.onSuccessAddCard(card);

                Logger.LOGD("Card has been added "+card.getCardId());

            }else{
                //Error from server with message
                Toast.makeText(this, getString(R.string.card_not_added), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setTag(String tag) {
        mTagCurrentFragment = tag;
    }
}
