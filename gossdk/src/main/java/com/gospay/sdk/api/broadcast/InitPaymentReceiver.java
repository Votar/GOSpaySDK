package com.gospay.sdk.api.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.gospay.sdk.R;
import com.gospay.sdk.api.listeners.GosInitPaymentListener;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.api.response.models.messages.payment.GosPayment;
import com.gospay.sdk.api.service.GosNetworkService;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;


/**
 * Created by bertalt on 19.09.16.
 */
public class InitPaymentReceiver extends BroadcastReceiver {

    private GosInitPaymentListener listener;

    public InitPaymentReceiver(GosInitPaymentListener listener) {
        super();
        this.listener = listener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String json = intent.getStringExtra(GosNetworkService.NetworkContract.KEY_RESPONSE);
        Logger.LOGD("Receiver intent");

        Gson gson = Parser.getsInstance();

        GosResponse response = gson.fromJson(json, GosResponse.class);

        if (response == null) {
            listener.onFailureInitPayment(context.getString(R.string.error_network_connection));
        }
        else {
            int resultCode = response.getResult().getCode();

            switch (resultCode) {
                case 0:
                    Logger.LOGNET(response.getPayload().toString());
                    listener.onSuccessInitPayment(gson.fromJson(response.getPayload(), GosPayment.class));
                    break;
                default:
                    listener.onFailureInitPayment(response.getResult().getMessage());
            }
        }

        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

}
