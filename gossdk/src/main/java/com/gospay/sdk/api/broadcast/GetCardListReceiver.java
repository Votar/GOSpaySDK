package com.gospay.sdk.api.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.gospay.sdk.R;
import com.gospay.sdk.api.listeners.GosGetCardListListener;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.api.response.models.messages.card.CardViewModel;
import com.gospay.sdk.api.service.GosNetworkService;
import com.gospay.sdk.util.Logger;
import com.gospay.sdk.util.Parser;


import java.util.ArrayList;

/**
 * Created by bertalt on 19.09.16.
 */
public class GetCardListReceiver extends BroadcastReceiver {

    private GosGetCardListListener listener;

    public GetCardListReceiver(GosGetCardListListener listListener) {
        this.listener = listListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Gson gson = Parser.getsInstance();

        String json = intent.getStringExtra(GosNetworkService.NetworkContract.KEY_RESPONSE);

        Logger.LOGD("Receiver intent");

        GosResponse response = gson.fromJson(json, GosResponse.class);

        if (response == null) {
            listener.onGetCardListFailure(context.getString(R.string.error_network_connection));
        } else {

            int resultCode = response.getResult().getCode();

            switch (resultCode) {
                case 0:
                    // Logger.LOGNET(response.getPayload().toString());

                    ArrayList<CardViewModel> resultArray = new ArrayList<CardViewModel>();
                    if (response.getPayload() == null)
                        listener.onGetCardListSuccess(resultArray);
                    else {
                        JsonArray array = response.getPayload().getAsJsonArray();

                        if (array.size() == 0)
                            listener.onGetCardListSuccess(resultArray);
                        else {
                            for (JsonElement nextValue : array)
                                resultArray.add(gson.fromJson(nextValue, CardViewModel.class));

                            listener.onGetCardListSuccess(resultArray);
                        }
                    }
                    break;
                default:
                    listener.onGetCardListFailure(response.getResult().getMessage());
            }
        }
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }
}
