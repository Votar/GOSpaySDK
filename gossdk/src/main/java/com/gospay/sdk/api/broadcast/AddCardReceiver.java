package com.gospay.sdk.api.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.gospay.sdk.R;
import com.gospay.sdk.api.listeners.GosAddCardListener;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.api.service.GosNetworkService;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;


/**
 * Created by bertalt on 19.09.16.
 */
public class AddCardReceiver extends BroadcastReceiver {

    private String LOG_TAG = this.getClass().getName();
    private GosAddCardListener listener;

    public AddCardReceiver(GosAddCardListener listener) {
        super();
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Gson gson = Parser.getsInstance();

        String json = intent.getStringExtra(GosNetworkService.NetworkContract.KEY_RESPONSE);

        Logger.LOGD(LOG_TAG, "Receiver intent by LocalBroadcast");

        GosResponse response = gson.fromJson(json, GosResponse.class);

        if (response == null) {
            listener.onFailureAddCard(context.getString(R.string.error_network_connection));
        } else {

            int resultCode = response.getResult().getCode();

            switch (resultCode) {
                case 0:
                    listener.onSuccessAddCard(gson.fromJson(response.getPayload(), CardViewModel.class));
                    break;
                default:
                    listener.onFailureAddCard(response.getResult().getMessage());
            }
        }

        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }
}
