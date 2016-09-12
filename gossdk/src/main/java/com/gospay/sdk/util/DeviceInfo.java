package com.gospay.sdk.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.gospay.sdk.R;

import java.util.ArrayList;


public class DeviceInfo {

    private static final String TAG = "DeviceInfo";

    @SerializedName("telInfo")
    private final Telephony mTelephony;
    @SerializedName("wifiInfo")
    private final WifiConnectivity mWifiConnectivity;
    @SerializedName("androidId")
    private final String mAndroidID;
    @SerializedName("isTablet")
    private final boolean mIsTablet;


    @SerializedName("emails")
    private String[] mEmails;

    public DeviceInfo(Context context) {

        mTelephony = new Telephony(
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)
        );
        mWifiConnectivity = new WifiConnectivity(
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE)
        );
        {
            String androidID = null;
            try {
                androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Exception e) {
                Log.e(TAG, "cannot get android Id");
            }
            mAndroidID = androidID;
            mEmails = getGoogleAccounts(AccountManager.get(context));
        }

        mIsTablet = context.getResources().getBoolean(R.bool.isTablet);
    }

    public void log() {
//        Log.d("*** DeviceInfo ***");
//        Log.d(getType());
//        Log.d(getModel());
//        Log.d(getManufacturer());
//        Log.d(getOS());
//        Log.d(getPhoneNumber());
//        Log.d(getSimSerial());
//        Log.d(getSimCountry());
//        Log.d(getSimOperator());
//        Log.d(getIMSI());
//        Log.d(getIMEI());
//        Log.d(getNetworkCountry());
//        Log.d(getNetworkOperatorCode());
//        Log.d(getNetworkOperatorName());
//        Log.d(getNetworkType());
//        Log.d(getMacAddress());
//        Log.d(getDeviceCode());
//        Log.d(getAccount());
//        Log.d("*** /DeviceInfo ***");
    }



    public String[] getEmails() {
        return mEmails;
    }

    public void setEmails(String[] emails) {
        this.mEmails = emails;
    }

    public String getType() {
        return mIsTablet ? "PAD" : "PHONE";
    }

    public String getModel() {
        return Build.MODEL;
    }

    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getOS() {
        return "Android ".concat(Build.VERSION.RELEASE);
    }

    public String getPhoneNumber() {
        return mTelephony.phoneNumber; // can also try to retrieve from sms
    }

    public String getSimSerial() {
        return mTelephony.simSerial;
    }

    public String getSimCountry() {
        return mTelephony.simCountry;
    }

    public String getSimOperator() {
        return mTelephony.simOperator;
    }

    public String getIMSI() {
        return mTelephony.imsi;
    }

    public String getIMEI() {
        return mTelephony.imei;
    }

    public String getNetworkCountry() {
        return mTelephony.networkCountry;
    }

    public String getNetworkOperatorCode() {
        return mTelephony.networkOperatorCode;
    }

    public String getNetworkOperatorName() {
        return mTelephony.networkOperatorName;
    }

    public String getNetworkType() {
        return mTelephony.networkType;
    }

    public String getMacAddress() {
        return mWifiConnectivity.mac;
    }


    public String getDeviceCode() {
        return mAndroidID;
    }

    private static String getGoogleEmail(Context context) {
        try {
            AccountManager accountManager = AccountManager.get(context);
            Account account = getGoogleAccount(accountManager);
            return account == null ? null : account.name;
        } catch (Exception e) {
            Log.e(TAG, "can't get google account");
            return null;
        }
    }

    private static Account getGoogleAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        return accounts.length > 0 ? accounts[0] : null;
    }

    private static String[] getGoogleAccounts(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");

        ArrayList<String> resultList = new ArrayList<>();

        for(Account tmp: accounts)
            resultList.add(tmp.name);

        String[] returnArray = new String[resultList.size()];

        resultList.toArray(returnArray);

        return returnArray;

    }

    private static class Telephony {
        String phoneNumber;
        String simSerial;
        String simCountry;
        String simOperator;
        String imei;
        String imsi;
        String networkCountry;
        String networkOperatorName;
        String networkOperatorCode;
        String networkType;

        Telephony(TelephonyManager manager) {
            phoneNumber = manager.getLine1Number();
            simSerial = manager.getSimSerialNumber();
            simCountry = manager.getSimCountryIso();
            simOperator = manager.getSimOperator();
            imei = manager.getDeviceId();
            imsi = manager.getSubscriberId();
            networkCountry = manager.getNetworkCountryIso();
            networkOperatorName = manager.getNetworkOperatorName();
            networkOperatorCode = manager.getNetworkOperator();
            networkType = mapNetworkTypeToName(manager.getNetworkType());
        }
    }

    private static class WifiConnectivity {
        String mac;

        WifiConnectivity(WifiManager manager) {
            WifiInfo info = manager.getConnectionInfo();
            mac = info.getMacAddress();
        }
    }

    private interface NetworkType {
        String NETWORK_CDMA = "CDMA (2G)";
        String NETWORK_EDGE = "EDGE (2.75G)";
        String NETWORK_GPRS = "GPRS (2.5G)";
        String NETWORK_UMTS = "UMTS (3G)";
        String NETWORK_EVDO_0 = "EVDO revision 0 (3G)";
        String NETWORK_EVDO_A = "EVDO revision A (3G - Transitional)";
        String NETWORK_EVDO_B = "EVDO revision B (3G - Transitional)";
        String NETWORK_1X_RTT = "1xRTT  (2G - Transitional)";
        String NETWORK_HSDPA = "HSDPA (3G - Transitional)";
        String NETWORK_HSUPA = "HSUPA (3G - Transitional)";
        String NETWORK_HSPA = "HSPA (3G - Transitional)";
        String NETWORK_IDEN = "iDen (2G)";
        String NETWORK_LTE = "LTE (4G)";
        String NETWORK_EHRPD = "EHRPD (3G)";
        String NETWORK_HSPAP = "HSPAP (3G)";
        String NETWORK_UNKNOWN = "Unknown";
    }

    private static String mapNetworkTypeToName(int networkType) {
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return NetworkType.NETWORK_CDMA;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return NetworkType.NETWORK_EDGE;
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return NetworkType.NETWORK_GPRS;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return NetworkType.NETWORK_UMTS;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return NetworkType.NETWORK_EVDO_0;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return NetworkType.NETWORK_EVDO_A;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return NetworkType.NETWORK_EVDO_B;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return NetworkType.NETWORK_1X_RTT;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return NetworkType.NETWORK_HSDPA;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return NetworkType.NETWORK_HSPA;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return NetworkType.NETWORK_HSUPA;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NetworkType.NETWORK_IDEN;
            case TelephonyManager.NETWORK_TYPE_LTE:
                return NetworkType.NETWORK_LTE;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return NetworkType.NETWORK_EHRPD;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NetworkType.NETWORK_HSPAP;
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
            default:
                //return NetworkType.NETWORK_UNKNOWN;
                return null;
        }
    }

}
