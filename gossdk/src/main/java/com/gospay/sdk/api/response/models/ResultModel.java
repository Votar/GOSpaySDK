package com.gospay.sdk.api.response.models;

/**
 * Created by bertalt on 12.09.16.
 */
public class ResultModel {

    private ResultModel(){}

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ResultModel(int code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
