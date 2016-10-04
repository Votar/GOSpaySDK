package com.gospay.sdk.api.request.models.payment.confirm;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bertalt on 08.09.16.
 */
public class ConfirmationPaymentParameter {

    String id;
    @SerializedName("tp")
    TransientPropertyModel tp;

    public ConfirmationPaymentParameter(String id, String cvv) {
        this.id = id;
        this.tp = new TransientPropertyModel(cvv);

    }

    public String getId() {
        return id;
    }

    public String getCvv() {
        return tp.getCvvCode();
    }

    class TransientPropertyModel {

        private String cvvCode;

        public TransientPropertyModel(String cvvCode) {
            this.cvvCode = cvvCode;
        }

        public String getCvvCode() {
            return cvvCode;
        }
    }
}
