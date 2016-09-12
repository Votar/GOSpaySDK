package com.gospay.sdk.api.client;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.gospay.sdk.api.response.listeners.GosResponseCallback;
import com.gospay.sdk.api.response.models.GosResponse;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.util.Logger;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by bertalt on 11.09.16.
 */
public class AsyncHttpClient extends AsyncTask<GosRequest, Void, GosResponse> {

    private GosResponseCallback listener;
    private String API_KEY;
    private  String language;
    public AsyncHttpClient(GosResponseCallback listener) {
        this.listener = listener;
    }

    @Override
    protected GosResponse doInBackground(GosRequest... params) {

        for(GosRequest request : params){

            Logger.LOGNET("init request: " + request.toString());
        }

        return call(params[0]);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();


    }

    @Override
    protected void onPostExecute(GosResponse gosResponse) {
        super.onPostExecute(gosResponse);
        listener.onProcessFinished(gosResponse);
    }
    private GosResponse call(GosRequest request) {

        byte[] postData = new byte[0];
        postData = request.getBody().getBytes(Charset.forName("UTF-8"));

        int postDataLength = postData.length;
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

            for(String key : headers.keySet())
                conn.addRequestProperty(key, headers.get(key));


            conn.setUseCaches(false);

            wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            wr.flush();
            response = listenToServer(conn);



        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (wr != null)
                    wr.close();

                if (conn != null)
                    conn.disconnect();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return response;
        }
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
        Logger.LOGNET("RESPONSE BODY: "+result);

        Gson gson = new Gson();
        GosResponse response = gson.fromJson(result, GosResponse.class);
        response.setHeaderFields(headerFields);

        Logger.LOGNET("RESPONSE MODEL: " + response.toString());


        return response;
    }

    private static void logCookies(Map<String, List<String>> headerFields){
        Logger.LOGNET("Cookies:");
        for(String tmpKey : headerFields.keySet())
            Logger.LOGNET(tmpKey+ " :"+Arrays.toString(headerFields.get(tmpKey).toArray()));

    }
}
