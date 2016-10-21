package com.gospay.ui.payment.stuff;

import android.content.DialogInterface;
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
import com.gospay.sdk.api.response.models.messages.payment.GosPayment;
import com.gospay.ui.R;
import com.gospay.sdk.api.GosNetworkManager;
import com.gospay.sdk.api.response.models.messages.payment.GosPaymentStatus;
import com.gospay.sdk.api.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.request.models.payment.status.GetPaymentStatusParameter;
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
    private TextView tvCurrentStatus;
    private Timer checkingStatusTimer;
    private GosPayment currentPayment;
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

        if (view == null) {
            view = (ViewGroup) inflater.inflate(R.layout.com_gos_track_payment_fragment, container, false);

            tvCurrentStatus = (TextView) view.findViewById(R.id.track_payment_current_status);

            progressBar = (ProgressBar) view.findViewById(R.id.track_payment_progress);
            ivResult = (ImageView) view.findViewById(R.id.track_payment_ic_result);

            bindView();

        }
        return view;

    }


    private void bindView() {

        Bundle args = getArguments();
        Gson gson = Parser.getsInstance();
        if (args == null)
            throw new GosSdkException("No data to payment view");

        currentPayment = gson.fromJson(args.getString(KEY_PAYMENT), GosPayment.class);

    }

    @Override
    public void onStart() {
        super.onStart();
        startCheckingPaymentStatusTimer(UPDATE_STATUS_MILLIS);
    }

    @Override
    public void onStop() {
        super.onStop();
        stopTimer();
    }

    private void switchPaymentStatus(GosPayment payment) {

        if (!isResumed()) return;

        String status = payment.getStatus();

        tvCurrentStatus.setText(status);
        switch (status) {
            case GosPaymentStatus.CANCELED:
            case GosPaymentStatus.DECLINED:
            case GosPaymentStatus.ERROR:
                show3dSecureVerification(payment);
                setupFailResult();
                break;

            case GosPaymentStatus.AUTHORIZATION:
            case GosPaymentStatus.PENDING:
            case GosPaymentStatus.PROCESSING:
            case GosPaymentStatus.QUEUED:

                break;
            case GosPaymentStatus.APPROVED:
                setupOkResult();
                break;

            case GosPaymentStatus.VERIFICATION_3D_SECURE_REQUIRED:
                show3dSecureVerification(payment);
                break;

        }
    }

    private void show3dSecureVerification(GosPayment payment) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager.findFragmentByTag(DescriptionBrowserFragment.TAG) == null) {

                DescriptionBrowserFragment fragment = DescriptionBrowserFragment.newInstance(payment.getD3SecureUrl());

                fragment.show(getFragmentManager(), DescriptionBrowserFragment.TAG);
            }
        } else
            showWebView(payment);
    }

    private void showWebView(GosPayment payment) {

        if (alert == null) {
            alert = new AlertDialog.Builder(getContext());
            alert.setTitle(getString(R.string.text_progress_dialog));

            WebView wv = new WebView(getContext());
            wv.loadUrl(payment.getD3SecureUrl());
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

    private void setupOkResult() {
        stopTimer();
        progressBar.setVisibility(View.GONE);
        ivResult.setImageResource(R.drawable.ic_payment_ok);
    }

    private void setupFailResult() {
        stopTimer();
        progressBar.setVisibility(View.GONE);
        ivResult.setImageResource(R.drawable.ic_payment_declined);
    }


    GosGetPaymentStatusListener gosGetPaymentStatusListener = new GosGetPaymentStatusListener() {

        @Override
        public void onSuccessGetPaymentStatus(GosPayment actualPayment) {

            switchPaymentStatus(actualPayment);
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
        final GetPaymentStatusParameter parameter = new GetPaymentStatusParameter(currentPayment.getId());
        checkingStatusTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                GosNetworkManager.getInstance().getPaymentStatus(getContext(), parameter, gosGetPaymentStatusListener);
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
