package com.gos.rabbit.gossdk.util;

import android.text.TextUtils;

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

    public static boolean isCvvValid(String cvv){

        if(TextUtils.isEmpty(cvv)) return false;

        try {
            int iCvv = Integer.valueOf(cvv);
            if(iCvv <1 && iCvv >999) return false;
        }catch (NumberFormatException ex){
            return false;
        }


        return true;

    }
}