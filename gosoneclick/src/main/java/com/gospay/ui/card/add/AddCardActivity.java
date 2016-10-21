package com.gospay.ui.card.add;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.util.Parser;
import com.gospay.ui.R;

public class AddCardActivity extends AppCompatActivity {

    private String KEY_N_START = "bdl_n_start";
    private boolean isFirstStart = true;

    public interface AddCardContract {
        String ARG_CARD_VIEW_MODEL = "cardViewModel";
        String ARG_ERROR_MESSAGE = "errorMessage";
        String ARG_PAYMENT_FIELDS = "paymentFields";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.add_card_title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (savedInstanceState == null) {
            replaceFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putBoolean(KEY_N_START, isFirstStart);
        super.onSaveInstanceState(outState);
    }


    private void replaceFragment() {
        Fragment fragment = AddCardFragment.newInstance(listener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.add_card_fragment_container, fragment)
                .commit();
    }

    GosAddCardListener listener = new GosAddCardListener() {
        @Override
        public void onSuccessAddCard(CardViewModel card) {

            Intent in = new Intent();
            String agrCardViewModel = Parser.getsInstance().toJson(card, CardViewModel.class);
            in.putExtra(AddCardContract.ARG_CARD_VIEW_MODEL, agrCardViewModel);

            setResult(RESULT_OK, in);
            finish();
        }

        @Override
        public void onFailureAddCard(String message) {
            Intent in = new Intent();

            in.putExtra(AddCardContract.ARG_CARD_VIEW_MODEL, message);

            setResult(RESULT_CANCELED, in);
            finish();
        }
    };


}
