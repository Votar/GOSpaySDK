package com.gospay.sdk.api.request.models.payment.confirm;

/**
 * Created by bertalt on 13.09.16.
 */
public class TransientPropertyModel {

    private String cvvCode;

    public TransientPropertyModel(String cvvCode) {
        this.cvvCode = cvvCode;
    }

    public String getCvvCode() {
        return cvvCode;
    }
}
