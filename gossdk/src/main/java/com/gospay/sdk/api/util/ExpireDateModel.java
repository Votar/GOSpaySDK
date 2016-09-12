package com.gospay.sdk.api.util;

import android.text.TextUtils;

import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.util.CreditCardValidator;

/**
 * Created by bertalt on 06.09.16.
 */
public class ExpireDateModel {

    private String expireMonth;
    private String expireYear;

    public static ExpireDateModel create(String monoString) {

        if (com.gospay.sdk.util.TextUtils.isEmpty(monoString))
            throw new GosSdkException("String for expire date couldn't be empty", null);

        if (monoString.length() != 4) {
            throw new GosSdkException(String.format("Invalid expire date %1s", monoString), null);
        }
        String expireMonth = monoString.substring(0, 2);
        String expireYear = monoString.substring(2, 4);

        if (CreditCardValidator.isExpireDateValid(expireMonth, expireYear))
            return new ExpireDateModel(expireMonth, expireYear);
        else
            throw new GosSdkException(String.format("Invalid expire date %1s / %2s", expireMonth, expireYear), null);
    }

    public ExpireDateModel(String expireMonth, String expireYear) {
        this.expireMonth = expireMonth;
        this.expireYear = expireYear;
    }

    public String getExpireMonth() {
        return expireMonth;
    }

    public String getExpireYear() {
        return expireYear;
    }

    @Override
    public String toString() {
        return "ExpireDateModel{" +
                "expireMonth='" + expireMonth + '\'' +
                ", expireYear='" + expireYear + '\'' +
                '}';
    }

    public String toMonoString() {
        return expireMonth + expireYear;
    }
}
