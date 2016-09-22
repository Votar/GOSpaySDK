package com.gospay.sdk.ui.payment.fragment;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gospay.sdk.GosSdkManager;
import com.gospay.sdk.R;
import com.gospay.sdk.api.PaymentStatus;
import com.gospay.sdk.api.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.response.models.messages.payment.Payment;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.util.Parser;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bertalt on 19.09.16.
 */
public class TrackPaymentFragment extends Fragment {

    public static final String TAG = TrackPaymentFragment.class.getSimpleName();
    public static final String KEY_PAYMENT = "bundle_key_payment";
    private Gson gson = new Gson();
    private TextView tvCurrentStatus;
    private Timer checkingStatusTimer;
    private Payment currentPayment;
    private ProgressBar progressBar;
    private ImageView ivResult;
    private int UPDATE_STATUS_MILLIS = 3000;
    private ViewGroup view;
    private AlertDialog.Builder alert;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null)
            view = (ViewGroup) inflater.inflate(R.layout.com_gos_track_payment_fragment, container, false);

        tvCurrentStatus = (TextView) view.findViewById(R.id.track_payment_current_status);

        progressBar = (ProgressBar)view.findViewById(R.id.track_payment_progress);
        ivResult = (ImageView)view.findViewById(R.id.track_payment_ic_result);

        bindView();
        startCheckingPaymentStatusTimer(UPDATE_STATUS_MILLIS);
        return view;

    }


    private void bindView() {

        Bundle args = getArguments();
        Gson gson = Parser.getsInstance();
        if (args == null)
            throw new GosSdkException("No data to payment view");

        currentPayment = gson.fromJson(args.getString(KEY_PAYMENT), Payment.class);

    }

    private void switchPaymentStatus(Payment payment){

        String status = payment.getStatus();

        tvCurrentStatus.setText(status);
        switch (status){
            case PaymentStatus.CANCELED:
            case PaymentStatus.DECLINED:
            case PaymentStatus.ERROR:
                setupFailResult();
                break;

            case PaymentStatus.AUTHORIZATION:
            case PaymentStatus.PENDING:
            case PaymentStatus.PROCESSING:
            case PaymentStatus.QUEUED:

                break;
            case PaymentStatus.APPROVED:
                setupOkResult();
                break;

            case PaymentStatus.VERIFICATION_3D_SECURE_REQUIRED:
                show3dWebView(payment);
                break;

        }
    }

    private void show3dWebView(Payment payment){

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager.findFragmentByTag(DescriptionBrowserFragment.TAG) == null) {

                DescriptionBrowserFragment fragment = DescriptionBrowserFragment.newInstance(payment.getUrl());

                fragment.show(getFragmentManager(), DescriptionBrowserFragment.TAG);
            }
        }else
            showWebView(payment);
    }

    private void showWebView(Payment payment) {

        if(alert == null) {
            alert = new AlertDialog.Builder(getContext());
            alert.setTitle(getString(R.string.text_progress_dialog));

            WebView wv = new WebView(getContext());
            wv.loadUrl(payment.getUrl());
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);

                    return true;
                }
            });

            alert.setView(wv);
            alert.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
    }

    private void setupOkResult(){
        stopTimer();
        progressBar.setVisibility(View.GONE);
        ivResult.setImageResource(R.drawable.ic_payment_ok);
    }
    private void setupFailResult(){
        stopTimer();
        progressBar.setVisibility(View.GONE);
        ivResult.setImageResource(R.drawable.ic_payment_declined);
    }


    GosGetPaymentStatusListener gosGetPaymentStatusListener = new GosGetPaymentStatusListener() {

        @Override
        public void onSuccessGetPaymentStatus(Payment paymentDescription) {

            switchPaymentStatus(paymentDescription);
        }

        @Override
        public void onFailureGetPaymentStatus(String message) {
            Log.d(TAG, message);
        }
    };

    private void startCheckingPaymentStatusTimer(int UPDATE_TIME) {
        if (checkingStatusTimer != null)
            stopTimer();

//        final GosApi gosApi = new GosApiImpl(getActivity(), null);
        checkingStatusTimer = new Timer(false);
        checkingStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                GosSdkManager.getInstance().getPaymentStatus(getContext(), currentPayment, gosGetPaymentStatusListener);
            }
        }, 0, UPDATE_TIME);
    }

    private void stopTimer() {
        if (checkingStatusTimer != null) {
            checkingStatusTimer.cancel();
            checkingStatusTimer.purge();
            checkingStatusTimer = null;
        }
    }

}
