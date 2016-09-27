package com.gospay.sdk.api.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.gospay.sdk.R;
import com.gospay.sdk.api.listeners.GosGetPaymentStatusListener;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.api.response.models.messages.payment.Payment;
import com.gospay.sdk.api.service.NetworkService;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;

/**
 * Created by bertalt on 19.09.16.
 */
public class GetStatusPaymentReceiver extends BroadcastReceiver {

    private GosGetPaymentStatusListener listener;

    public GetStatusPaymentReceiver(GosGetPaymentStatusListener listener) {
        super();
        this.listener = listener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String json = intent.getStringExtra(NetworkService.NetworkContract.KEY_RESPONSE);
        Logger.LOGD("Receiver intent");

        Gson gson = Parser.getsInstance();

        GosResponse response = gson.fromJson(json, GosResponse.class);

        if (response == null) {
            listener.onFailureGetPaymentStatus(context.getString(R.string.error_network_connection));
        }
        else {
            int resultCode = response.getResult().getCode();

            switch (resultCode) {
                case 0:
                    Logger.LOGNET(response.getPayload().toString());
                    listener.onSuccessGetPaymentStatus(gson.fromJson(response.getPayload(), Payment.class));
                    break;
                default:
                    listener.onFailureGetPaymentStatus(response.getResult().getMessage());
            }
        }

        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

}
