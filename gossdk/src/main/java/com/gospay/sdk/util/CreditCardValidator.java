package com.gospay.sdk.util;



import android.support.annotation.NonNull;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by bertalt on 30.08.16.
 */
public class CreditCardValidator {

    public static boolean isCardValid(String ccNumber) {
        return Luhn.isCardValid(ccNumber);
    }


    public static boolean isExpireDateValid(String expireMonth, String expireYear) {


        if (TextUtils.isEmpty(expireMonth) || TextUtils.isEmpty(expireYear)){
            return false;
        }

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        int currentMonth = calendar.get(Calendar.MONTH) + 1;
        int currentYear = calendar.get(Calendar.YEAR) - 2000;

        int iExpireMonth = Integer.valueOf(expireMonth);
        int iExpireYear = Integer.valueOf(expireYear);

        /*Check valid month*/
        if(iExpireMonth <1 || iExpireMonth >12)
            return false;


        /*Check if expire date in future*/
        if (currentYear > iExpireYear)
            return false;
        else if (currentYear == iExpireYear)
            if (currentMonth > iExpireMonth)
                return false;



        return true;
    }

    public static boolean isExpireYearValid(String expireYear){

        if (TextUtils.isEmpty(expireYear)){
            return false;
        }
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        int currentYear = calendar.get(Calendar.YEAR) - 2000;
        int iExpireYear = Integer.valueOf(expireYear);

        if (currentYear > iExpireYear)
            return false;

        return true;
    }

    public static boolean isExpireMonthValid(String expireMonth){

        if (TextUtils.isEmpty(expireMonth)){
            return false;
        }

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);

        int currentMonth = calendar.get(Calendar.MONTH) + 1;

        int iExpireMonth = Integer.valueOf(expireMonth);

        if(iExpireMonth <1 || iExpireMonth >12)
            return false;

        return true;
    }

    public static boolean isCvvValid(@NonNull String cvv){

        if(TextUtils.isEmpty(cvv)) return false;
        /*Cvv code in string format always contains 3 character*/
        if(cvv.length()!=3) return false;

        try {
            int iCvv = Integer.valueOf(cvv);
            if(iCvv <=0 || iCvv >999) return false;

        }catch (NumberFormatException ex){
            return false;
        }

        return true;

    }
}
