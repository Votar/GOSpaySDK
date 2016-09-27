package com.gospay.sdk.api.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.gospay.sdk.api.ServerApi;
import com.gospay.sdk.api.client.GosRequest;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.util.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by bertalt on 19.09.16.
 */
public class NetworkService extends IntentService {

    public interface NetworkContract {
        String KEY_REQUEST = "intent_key_request";
        String KEY_RESPONSE = "intent_key_response";
        String ACTION_GET_CARD_LIST = "com.gospay.sdk.service.GET_CARD_LIST";
        String ACTION_ADD_CARD = "com.gospay.sdk.service.ADD_CARD";
        String ACTION_INIT_PAYMENT = "com.gospay.sdk.service.INIT_PAYMENT";
        String ACTION_CONFIRM_PAYMENT = "com.gospay.sdk.service.CONFIRM_PAYMENT";
        String ACTION_GET_PAYMENT_STATUS = "com.gospay.sdk.service.GET_PAYMENT_STATUS";
    }

    static final String DEFAULT_NAME = "com.gospay.sdk.service.NetworkService";

    public NetworkService() {
        super(DEFAULT_NAME);
    }

    private Gson gson = new Gson();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */


    public NetworkService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GosRequest request = gson.fromJson(intent.getStringExtra(NetworkContract.KEY_REQUEST), GosRequest.class);

        Logger.LOGD("Init request by service" + request.toString());

        GosResponse response = null;

        if (request.getMethod().equalsIgnoreCase(ServerApi.GOS_METHODS.POST))
            response = callPost(request);
        else if (request.getMethod().equalsIgnoreCase(ServerApi.GOS_METHODS.GET))
            response = callGet(request);

            sendBroadcastResult(request, response);
    }

    private void sendBroadcastResult(GosRequest request, GosResponse response) {

        Intent broadcastIntent = null;



            Logger.LOGD("Prepare to send local broadcast with action: " + request.getRequestAction());
            switch (request.getRequestAction()) {

                case ServerApi.GOS_REQUESTS.GET_CARD_LIST:
                    broadcastIntent = new Intent(NetworkContract.ACTION_GET_CARD_LIST)
                            .putExtra(NetworkContract.KEY_RESPONSE, gson.toJson(response, GosResponse.class));
                    break;
                case ServerApi.GOS_REQUESTS.ADD_CARD:
                    broadcastIntent = new Intent(NetworkContract.ACTION_ADD_CARD)
                            .putExtra(NetworkContract.KEY_RESPONSE, gson.toJson(response, GosResponse.class));
                    break;
                case ServerApi.GOS_REQUESTS.INIT_PAYMENT:
                    broadcastIntent = new Intent(NetworkContract.ACTION_INIT_PAYMENT)
                            .putExtra(NetworkContract.KEY_RESPONSE, gson.toJson(response, GosResponse.class));
                    break;
                case ServerApi.GOS_REQUESTS.CONFIRM_PAYMENT:
                    broadcastIntent = new Intent(NetworkContract.ACTION_CONFIRM_PAYMENT)
                            .putExtra(NetworkContract.KEY_RESPONSE, gson.toJson(response, GosResponse.class));
                    break;
                case ServerApi.GOS_REQUESTS.GET_PAYMENT_STATUS:
                    broadcastIntent = new Intent(NetworkContract.ACTION_GET_PAYMENT_STATUS)
                            .putExtra(NetworkContract.KEY_RESPONSE, gson.toJson(response, GosResponse.class));
                    break;
            }
            if (broadcastIntent != null)
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);
            else
                Logger.LOGE("Unknown requestAction");


    }


    private GosResponse callGet(GosRequest request) {

        URL url = null;
        HttpURLConnection conn = null;
        GosResponse response = null;
        try {
            url = new URL(request.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod(request.getMethod());


            Map<String, String> headers = request.getHeaders();

            for (String key : headers.keySet())
                conn.addRequestProperty(key, headers.get(key));

            conn.setUseCaches(false);

            response = listenToServer(conn);

        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (conn != null)
                conn.disconnect();
        }

        return response;
    }


    private GosResponse callPost(GosRequest request) {

        byte[] postData = request.getBody().getBytes(Charset.forName("UTF-8"));

        URL url = null;
        HttpURLConnection conn = null;
        DataOutputStream wr = null;
        GosResponse response = null;
        try {
            url = new URL(request.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod(request.getMethod());


            Map<String, String> headers = request.getHeaders();

            for (String key : headers.keySet())
                conn.addRequestProperty(key, headers.get(key));

            conn.setUseCaches(false);

            wr = new DataOutputStream(conn.getOutputStream());

            wr.write(postData);
            wr.flush();
            response = listenToServer(conn);


        } catch (IOException e1) {
            e1.printStackTrace();
            return null;
        } finally {
            try {
                if (wr != null)
                    wr.close();

                if (conn != null)
                    conn.disconnect();

            } catch (IOException e1) {
                Logger.LOGE("Error closing stream HttpConnection");
            }

        }
        return response;
    }

    private GosResponse listenToServer(HttpURLConnection conn) {
        String line;
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, List<String>> headerFields = conn.getHeaderFields();

        String result = stringBuilder.toString();
        Logger.LOGNET("RESPONSE BODY: " + result);

        Gson gson = new Gson();
        GosResponse response = gson.fromJson(result, GosResponse.class);
        response.setHeaderFields(headerFields);

        Logger.LOGNET("RESPONSE MODEL: " + response.toString());


        return response;
    }
}
