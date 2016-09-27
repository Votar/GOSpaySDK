package com.gospay.sdk.api.listeners;


import com.gospay.sdk.api.response.models.GosResponse;

/**
 * Created by bertalt on 30.08.16.
 */
public interface GosResponseListener {

    void onRequestSuccess(GosResponse response);
    void onRequestError(String message);

}
