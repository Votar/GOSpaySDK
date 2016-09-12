package com.gospay.sdk.api.response.models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.gospay.sdk.util.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by bertalt on 01.09.16.
 */
public class GosResponse {


    @SerializedName("result")
    private ResultModel result;
    @SerializedName("payload")
    private JsonElement payload;
    private Map<String, List<String>> headerFields;

    public GosResponse(ResultModel result, JsonElement payload) {
        this.result = result;
        this.payload = payload;
    }

    public GosResponse(ResultModel result, JsonElement payload, Map<String, List<String>> headerFields) {
        this(result, payload);
        this.headerFields.putAll(headerFields);
    }

    public Map<String, List<String>> getHeaderFields() {
        return headerFields;
    }

    public void setHeaderFields(Map<String, List<String>> headerFields) {
        this.headerFields = headerFields;
    }

    public ResultModel getResult() {
        return result;
    }

    public void setResult(ResultModel result) {
        this.result = result;
    }

    public JsonElement getPayload() {
        return payload;
    }
    public void setPayload(JsonElement payload) {
        this.payload = payload;
    }

    private String headersToString(){
        StringBuilder stringBuilder = new StringBuilder();
        for(String tmpKey : headerFields.keySet())
          stringBuilder.append(tmpKey).append(" :").append(Arrays.toString(headerFields.get(tmpKey).toArray())).append('\n');
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "GosResponse{" +'\n'+
                "result=" + result +'\n'+
                ", payload='" + payload + '\'' +'\n'+
                ", headers= "+headersToString()+
                '}';
    }
}
