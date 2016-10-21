package com.gospay.ui.card.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.exceptions.GosInvalidCardFieldsException;
import com.gospay.ui.R;
import com.gospay.ui.view.GosEditNumber;

/**
 * Created by bertalt on 20.10.16.
 */

public class AddCardFragment extends Fragment {


    private GosAddCardListener gosAddCardListener;

    private EditText etExpireYear,
            etExpireMonth,
            etCvv,
            etAlias;

    private GosEditNumber etNumber;
    private Button btnAdd;
    private CheckBox checkSaveCvv;


    public static AddCardFragment newInstance(GosAddCardListener listener) {
        AddCardFragment fragment =  new AddCardFragment();
        fragment.gosAddCardListener = listener;

        return fragment;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


         View view = (ViewGroup) inflater.inflate(R.layout.com_gos_add_card_fragment, container, false);

        etNumber = (GosEditNumber) view.findViewById(R.id.add_card_edit_number);
        etNumber.addTextChangedListener(cardWatcher);

        etExpireMonth = (EditText) view.findViewById(R.id.add_card_edit_expiry_month);
        etExpireMonth.addTextChangedListener(monthWatcher);
        etExpireYear = (EditText) view.findViewById(R.id.add_card_edit_expiry_year);
        etExpireYear.addTextChangedListener(yearWatcher);
        etCvv = (EditText) view.findViewById(R.id.add_card_edit_cvv);
        etCvv.addTextChangedListener(cvvWatcher);
        etAlias = (EditText) view.findViewById(R.id.add_card_edit_alias);

        btnAdd = (Button) view.findViewById(R.id.add_card_btn_add);
        btnAdd.setOnClickListener(onClickAdd);

        requestFocus(etAlias);

        return view;
    }


    private View.OnClickListener onClickAdd = new View.OnClickListener() {
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
            if (s.length() == 3) requestFocus(btnAdd);
        }
    };




}
