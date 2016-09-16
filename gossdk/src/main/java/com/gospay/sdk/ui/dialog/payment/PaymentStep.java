package com.gospay.sdk.ui.dialog.payment;

import com.gospay.sdk.exceptions.GosSdkException;

/**
 * Created by bertalt on 16.09.16.
 */
public enum PaymentStep {


    INIT_PAYMENT(1), CONFIRM_PAYMENT(2), LOADING_PAYMENT(0), D3_SECURE(3);

    private int value = -1;
    PaymentStep(int i) {
        value = i;
    }

        public static PaymentStep valueOf(int i){
            switch (i) {
                case 1:
                    return INIT_PAYMENT;
                case 2:
                    return CONFIRM_PAYMENT;
                case 3:
                    return D3_SECURE;
                case 0:
                    return LOADING_PAYMENT;
                default:
                    throw new GosSdkException("Unknown payment step");
            }
        }

    public int toInt(){
        return value;
    }

}