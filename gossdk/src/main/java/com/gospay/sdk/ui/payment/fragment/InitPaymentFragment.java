package com.gospay.sdk.ui.payment.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gospay.sdk.GosSdkManager;
import com.gospay.sdk.R;
import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.listeners.GosGetCardListListener;
import com.gospay.sdk.api.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.request.models.payment.init.InitPaymentParameter;
import com.gospay.sdk.api.request.models.payment.init.PaymentFields;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.api.response.models.messages.payment.Payment;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.ui.payment.PaymentProcessingActivity;
import com.gospay.sdk.ui.widget.InfinitePagerAdapter;
import com.gospay.sdk.ui.widget.InfiniteViewPager;
import com.gospay.sdk.ui.widget.PaymentCardsPagerAdapter;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bertalt on 19.09.16.
 */
public class InitPaymentFragment extends Fragment {

    public static String TAG = InitPaymentFragment.class.getSimpleName();
    private TextView tvCurrency, tvDescr, tvAmount, tvOrderId;
    private ImageView ivLeft, ivRight;
    private CardViewModel selectedCard;
    private ImageButton btnConfirm;
    private PaymentFields paymentFields;
    private Payment paymentInProgress;
    private View cardProgressBar;
    private ProgressBar requestProgress;
    private LinearLayout payBlock;
    private List<CardViewModel> listCards = new ArrayList<>();

    //private RecyclerView recyclerView;
    //private CardListAdapter cardListAdapter;

    private InfiniteViewPager mPager;
    private View cardPickerView;
    private InfinitePagerAdapter infinitePagerAdapter;
    private View myView;

    public interface InitContract {
        String KEY_PAYMENT_FIELDS = "ikpf";
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (myView == null) {
            myView = (ViewGroup) inflater.inflate(R.layout.com_gos_init_payment_fragment, container, false);
            tvDescr = (TextView) myView.findViewById(R.id.fragment_payment_description);
            tvAmount = (TextView) myView.findViewById(R.id.fragment_payment_payment_amount);
            tvCurrency = (TextView) myView.findViewById(R.id.fragment_payment_payment_currency);
            tvOrderId = (TextView) myView.findViewById(R.id.fragment_payment_order_id);
            btnConfirm = (ImageButton) myView.findViewById(R.id.fragment_payment_init_btn_next);

            ivLeft =(ImageView)myView.findViewById(R.id.button_pager_left);
            ivLeft.setOnClickListener(onClickLeft);
            ivRight = (ImageView)myView.findViewById(R.id.button_pager_right);
            ivRight.setOnClickListener(onClickRight);
            cardPickerView = myView.findViewById(R.id.init_recycler_card);
            requestProgress = (ProgressBar) myView.findViewById(R.id.init_request_progress);
            payBlock = (LinearLayout)myView.findViewById(R.id.ll_pay_block);
            mPager = (InfiniteViewPager) myView.findViewById(R.id.payment_card_recycler);
//            emptyView = (TextView) myView.findViewById(R.id.init_select_card_empty_view);
            cardProgressBar = myView.findViewById(R.id.init_select_card_progress);

            btnConfirm.setOnClickListener(onClickPay);

            bindView();
            setupView();
        }
        return myView;

    }

    @Override
    public void onResume() {
        super.onResume();
        if (paymentInProgress != null)
            showConfirmFragment();
    }

    private void bindView() {

        Bundle args = getArguments();

        if (args == null) throw new GosSdkException("No data to payment view");

        paymentFields = Parser.getsInstance().fromJson(args.getString(InitContract.KEY_PAYMENT_FIELDS), PaymentFields.class);

        if (paymentFields == null)
            throw new GosSdkException("Cannot parse bundle by key: " + InitContract.KEY_PAYMENT_FIELDS);

        tvDescr.setText(paymentFields.getDescription());
        tvOrderId.setText(paymentFields.getOrder());
        tvAmount.setText(String.valueOf(paymentFields.getPrice()));
        tvCurrency.setText(paymentFields.getCurrency().getCurrencyCode());

        if (mPager.getAdapter() == null) {
            final List<CardViewModel> list = GosSdkManager.getInstance().getCachedCardList();
            if (list.size() != 0) {
                setupRecycler(list);
            } else {
                GosSdkManager.getInstance().getCardList(getActivity(), new GosGetCardListListener() {
                    @Override
                    public void onGetCardListSuccess(ArrayList<CardViewModel> cardList) {
                        setupRecycler(cardList);
                    }

                    @Override
                    public void onGetCardListFailure(String message) {

                        listCards.clear();
                        setupRecycler(listCards);
                    }
                });
            }
        }
    }

    private void setupRecycler(List<CardViewModel> cardList) {

        cardPickerView.setVisibility(View.VISIBLE);

        listCards.clear();
        listCards.addAll(cardList);
        PaymentCardsPagerAdapter adapter = new PaymentCardsPagerAdapter(listCards);

        infinitePagerAdapter = new InfinitePagerAdapter(adapter);
        /*RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mPager.setLayoutManager(layoutManager);*/
        mPager.setOffscreenPageLimit(adapter.getCount());
        if (adapter.getCount() > 1) {
            mPager.setClipToPadding(false);
            mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.pager_margin));

        }
        mPager.setAdapter(infinitePagerAdapter);
        setupView();
    }

    private View.OnClickListener onClickPay = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (listCards != null)
                selectedCard = listCards.get(mPager.getCurrentItem());

            if (selectedCard != null) {
                InitPaymentParameter parameter = new InitPaymentParameter(selectedCard.getCardId(), paymentFields);
                GosNetworkManager.getInstance().initPayment(getContext(), parameter, gosInitPaymentListener);
                requestProgress.setVisibility(View.VISIBLE);
            } else
                Toast.makeText(getContext(), getString(R.string.hint_select_card), Toast.LENGTH_SHORT).show();

        }
    };

    private View.OnClickListener onClickLeft = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mPager != null)
                mPager.setCurrentItem(mPager.getCurrentItem()-1, true);
        }
    };
    private View.OnClickListener onClickRight= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mPager != null)
                mPager.setCurrentItem(mPager.getCurrentItem()+1, true);
        }
    };

    GosInitPaymentListener gosInitPaymentListener = new GosInitPaymentListener() {
        @Override
        public void onSuccessInitPayment(Payment paymentInProgress) {

            InitPaymentFragment.this.paymentInProgress = paymentInProgress;

            if (isResumed()) {
                showConfirmFragment();
            }
        }

        @Override
        public void onFailureInitPayment(String message) {
            Logger.LOGD("Failed " + message);
            requestProgress.setVisibility(View.GONE);
        }
    };

    private void showConfirmFragment() {
        Gson gson = Parser.getsInstance();
        Bundle args = new Bundle();
        args.putString(ConfirmPaymentFragment.KEY_PAYMENT_FIELDS, gson.toJson(paymentFields, PaymentFields.class));
        args.putString(ConfirmPaymentFragment.KEY_PAYLOAD, gson.toJson(paymentInProgress, Payment.class));
        args.putString(ConfirmPaymentFragment.KEY_CARD_VIEW, gson.toJson(selectedCard, CardViewModel.class));
        Fragment fragment = new ConfirmPaymentFragment();
        fragment.setArguments(args);
        getFragmentManager().beginTransaction()
                .replace(R.id.activity_payment_processing_fragment_container_confirm, fragment, ConfirmPaymentFragment.TAG)
                .commit();
        ((PaymentProcessingActivity) getActivity()).setTag(ConfirmPaymentFragment.TAG);

        cardPickerView.setVisibility(View.GONE);
        payBlock.setVisibility(View.GONE);

        paymentInProgress = null;

    }

    private void setupView() {

        if (infinitePagerAdapter == null) {
            if (cardProgressBar != null && mPager != null) {
                mPager.setVisibility(View.GONE);
//                emptyView.setVisibility(View.GONE);
                cardProgressBar.setVisibility(View.VISIBLE);

            }
        } else {
            if (infinitePagerAdapter.getCount() == 0) {
                if (mPager != null) {  //emptyView != null &&
                    mPager.setVisibility(View.GONE);
                    cardProgressBar.setVisibility(View.GONE);
//                    emptyView.setVisibility(View.VISIBLE);

                }
            } else {
                if (mPager != null && cardProgressBar != null) {  //emptyView != null &&
//                    emptyView.setVisibility(View.GONE);
                    cardProgressBar.setVisibility(View.GONE);
                    mPager.setVisibility(View.VISIBLE);
                }
            }
        }
    }


}
