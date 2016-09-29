package com.gospay.ui.card.add;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gospay.ui.R;
import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.exceptions.GosInvalidCardFieldsException;
import com.gospay.ui.view.GosEditNumber;

/**
 * Created by bertalt on 13.09.16.
 */
public class AddCardDialog extends DialogFragment {

    public static final String TAG = "AddCardDialog";

    private GosAddCardListener gosAddCardListener;

    private EditText etExpireYear,
            etExpireMonth,
            etCvv,
            etAlias;

    private GosEditNumber etNumber;

    private Button btnSubmit,btnCancel;


    public AddCardDialog() {

    }

    @SuppressLint("ValidFragment")
    private AddCardDialog(GosAddCardListener listener) {

        this.gosAddCardListener = listener;
    }

    public static AddCardDialog newInstance(GosAddCardListener listener) {

        return new AddCardDialog(listener);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_card, null);

        etNumber = (GosEditNumber) view.findViewById(R.id.dialog_add_card_edit_card_number);
        requestFocus(etNumber);
        etNumber.addTextChangedListener(cardWatcher);

        etExpireMonth = (EditText) view.findViewById(R.id.dialog_add_card_edit_month);
        etExpireMonth.addTextChangedListener(monthWatcher);
        etExpireYear = (EditText) view.findViewById(R.id.dialog_add_card_edit_year);
        etExpireYear.addTextChangedListener(yearWatcher);
        etCvv = (EditText) view.findViewById(R.id.dialog_add_card_edit_cvv);
        etCvv.addTextChangedListener(cvvWatcher);
        etAlias = (EditText) view.findViewById(R.id.dialog_add_card_edit_alias);

        btnSubmit = (Button) view.findViewById(R.id.dialog_add_card_btn_submit);
        btnSubmit.setOnClickListener(onClickSubmit);

        btnCancel = (Button) view.findViewById(R.id.dialog_add_card_btn_cancel);
        btnCancel.setOnClickListener(onClickCancel);

        builder.setView(view);

        return builder.create();
    }

    private View.OnClickListener onClickCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    private View.OnClickListener onClickSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String cardNumber = etNumber.getText().toString().replace(" ", "");

            try {

                CardFields cardFields = CardFields.create(Long.parseLong(cardNumber),
                        etExpireMonth.getText().toString(),
                        etExpireYear.getText().toString(),
                        etCvv.getText().toString(),
                        etAlias.getText().toString());

                    GosNetworkManager.getInstance().addCard(getContext(),cardFields, gosAddCardListener);
                dismiss();

            } catch (GosInvalidCardFieldsException ex) {

                switch (ex.getInvalidField()) {
                    case CARD_NUMBER:
                        etNumber.setError(getString(R.string.error_invalid_card_number));
                        requestFocus(etNumber);
                        break;
                    case EXPIRY_DATE:
                    case EXPIRE_MONTH:
                        etExpireMonth.setError(getString(R.string.error_invalid_expire_month));
                        requestFocus(etExpireMonth);
                        break;

                    case EXPIRY_YEAR:
                        etExpireYear.setError(getString(R.string.error_invalid_expire_year));
                        requestFocus(etExpireYear);
                        break;

                    case CVV:
                        etCvv.setError(getString(R.string.error_invalid_cvv));
                        requestFocus(etCvv);
                        break;
                    default:
                        Toast.makeText(getContext(), R.string.error_comon_validator_error, Toast.LENGTH_SHORT).show();

                }
            }

        }
    };

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    private TextWatcher cardWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == getResources().getInteger(R.integer.max_length_card_number))
                requestFocus(etExpireMonth);
        }
    };

    private TextWatcher monthWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 2) requestFocus(etExpireYear);
        }
    };
    private TextWatcher yearWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 2) requestFocus(etCvv);
        }
    };

    private TextWatcher cvvWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 3) requestFocus(etAlias);
        }
    };
}
