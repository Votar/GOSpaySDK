package com.gospay.sdk.api.client;

/**
 * Created by bertalt on 11.09.16.
 */

import android.support.annotation.Nullable;

import com.gospay.sdk.api.ServerApi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class GosRequest {

    private StringBuilder url = new StringBuilder().append(ServerApi.BACKEND_URL);
    private String requestAction;
    private String body;
    private String method;
    private Map<String, String> headers;

    public GosRequest(String url, @Nullable String body, String method) {

        this(url, body);
        this.method = method;
    }

    public GosRequest(String url, String body) {
        this(url);
        this.body = body;
    }

    public GosRequest(String url) {
        headers = new HashMap<>();
        requestAction = url;
        this.url.append(requestAction);

    }

    public String getUrl() {
        return url.toString();
    }

    public String getBody() {
        return body;
    }

    public String getMethod() {
        return method;
    }

    public String getRequestAction() {
        return requestAction;
    }

    public void addHeader(String key, String value){
        headers.put(key, value);
    }
    public Map<String, String> getHeaders(){
        return headers;
    }

    private String toStringHeaders(){

        if(headers.keySet().size() != 0 &&headers != null){
            StringBuilder stringBuilder = new StringBuilder();
            for(String tmpKey : headers.keySet()){
                stringBuilder.append(tmpKey).append(':').append(headers.get(tmpKey)).append('\n');
            }
            return stringBuilder.toString();
        }
        else
            return "";

    }

    @Override
    public String toString() {
        return "GosRequest{" +
                "url='" + url + '\''+'\n'+
                ", body='" + body + '\''+'\n' +
                ", method='" + method + '\''+'\n' +
                ", headers = "+ toStringHeaders()+'\n'+
                '}';
    }


}
