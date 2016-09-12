package com.gospay.sdk.util;

import java.util.regex.Pattern;

/**
 * Luhn Class is an implementation of the Luhn algorithm that checks validity of a credit card number.
 * <p>
 * <p> Checks whether a string of digits is a valid credit card number according to the Luhn
 * algorithm.
 * <p>1. Starting with the second to last digit and moving left, double the value of all the
 * alternating digits. For any digits that thus become 10 or more, add their digits together.
 * For example, 1111 becomes 2121, while 8763 becomes 7733 (from (1+6)7(1+2)3).
 * <p>2. Add all these digits together. For example, 1111 becomes 2121, then 2+1+2+1 is 6;
 * while 8763 becomes 7733, then 7+7+3+3 is 20.
 * <p>
 * <p>3. If the total ends in 0 (put another way, if the total modulus 10 is 0), then the number
 * is valid according to the Luhn formula, else it is not valid. So, 1111 is not valid (as shown
 * above, it comes out to 6), while 8763 is valid (as shown above, it comes out to 20).
 *
 * @author <a href="http://www.chriswareham.demon.co.uk/software/Luhn.java">Chris Wareham</a>
 * @return <b>true</b> if the number is valid, <b>false</b> otherwise.
 */
public class Luhn {

    private static final Pattern PATTERN = Pattern.compile("^\\d{13,19}$");
    private static final String TAG = "Luhn";

    public static boolean isCardValid(String ccNumber) {
        if (ccNumber == null || ccNumber.length() != 16) return false;

        if (PATTERN.matcher(ccNumber).matches()) {
            int sum = 0;
            boolean alternate = false;
            for (int i = ccNumber.length() - 1; i >= 0; i--) {
                int n = Integer.parseInt(ccNumber.substring(i, i + 1));
                if (alternate) {
                    n *= 2;
                    if (n > 9) {
                        n = (n % 10) + 1;
                    }
                }
                sum += n;
                alternate = !alternate;
            }

            return (sum % 10 == 0);
        }

        return false;
    }

    public static String getCreditCardType(String creditCardNumber) {
//        Pattern regVisa = Pattern.compile("^4[0-9]{12}(?:[0-9]{3})?$");
//        Pattern regMaster = Pattern.compile("^5[1-5][0-9]{14}$");
//        Pattern regExpress = Pattern.compile("^3[47][0-9]{13}$");
//        Pattern regDiners = Pattern.compile("^3(?:0[0-5]|[68][0-9])[0-9]{11}$");
//        Pattern regDiscover = Pattern.compile("^6(?:011|5[0-9]{2})[0-9]{12}$");
//        Pattern regJCB = Pattern.compile("^(?:2131|1800|35\\d{3})\\d{11}$");
//
//        if (regVisa.matcher(creditCardNumber).matches())
//            return "VISA";
//        else if (regVisa.matcher(creditCardNumber).matches())
//            return "MASTER";
//        else if (regVisa.matcher(creditCardNumber).matches())
//            return "AEXPRESS";
//        else if (regVisa.matcher(creditCardNumber).matches())
//            return "DINERS";
//        else if (regVisa.matcher(creditCardNumber).matches())
//            return "DISCOVERS";
//        else if (regVisa.matcher(creditCardNumber).matches())
//            return "JCB";
//        else
//            return "invalid";

        if (creditCardNumber.startsWith("4")) {
            return "VISA";
        } else if (creditCardNumber.startsWith("5")) {
            return "MASTERCARD";
        } else if (creditCardNumber.startsWith("6")) {
            return "MAESTRO";
        } else {
            return "invalid";
        }
    }

    public static boolean isVisa(String creditCardNumber) {
        return (getCreditCardType(creditCardNumber).equals("VISA"));
    }


}
