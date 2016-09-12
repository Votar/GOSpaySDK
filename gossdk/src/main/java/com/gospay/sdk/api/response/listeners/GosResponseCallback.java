package com.gospay.sdk.api.response.listeners;

import com.gospay.sdk.api.response.models.GosResponse;

/**
 * Created by bertalt on 12.09.16.
 */
public interface GosResponseCallback {

    void onProcessFinished(GosResponse response);
}
