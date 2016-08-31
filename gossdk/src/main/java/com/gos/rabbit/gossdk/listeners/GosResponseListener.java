package com.gos.rabbit.gossdk.listeners;

/**
 * Created by bertalt on 30.08.16.
 */
public interface GosResponseListener {

    void onRequestSuccess();
    void onRequestError(String message);

}
