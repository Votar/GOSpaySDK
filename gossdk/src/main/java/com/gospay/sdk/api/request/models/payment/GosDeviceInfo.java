package com.gospay.sdk.api.request.models.payment;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import com.gospay.sdk.R;
import java.util.ArrayList;

/**
 * Created by bertalt on 30.09.16.
 */

public class GosDeviceInfo {

    private String type;
    private String model;
    private String manufacturer;
    private String os;
    private String code;
    private String pushId;
    private String[] accountId;
    private String mac;
    private String phoneNumber;
    private String simSerial;
    private String simCountry;
    private String simOperator;
    private String imsi;
    private String imei;
    private String networkCountry;
    private String networkOperatorCode;
    private String networkOperatorName;
    private String networkType;

    public GosDeviceInfo(Context context) {

       /*
        mWifiConnectivity = new GosDeviceInfo.WifiConnectivity(
                (WifiManager) context.getSystemService(Context.WIFI_SERVICE)
        );*/
        {
            String androidID = "unknown";
            try {
                androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Exception e) {
                e.printStackTrace();
            }

            code = androidID;


            accountId = getGoogleAccounts(context);
        }

        type = context.getResources().getBoolean(R.bool.isTablet) ? "PAD" : "PHONE";
        setupTelephonyManager(context);
        setupNetworkInfo(context);

        model = Build.DEVICE;
        manufacturer = Build.MANUFACTURER;
        os = Build.VERSION.RELEASE;
    }

    private static String[] getGoogleAccounts(Context context) {

        AccountManager accountManager = AccountManager.get(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        Account[] accounts = accountManager.getAccountsByType("com.google");

        ArrayList<String> resultList = new ArrayList<>();

        for(Account tmp: accounts)
            resultList.add(tmp.name);

        String[] returnArray = new String[resultList.size()];

        resultList.toArray(returnArray);

        return returnArray;

    }
    
    private void setupTelephonyManager(Context context) {

        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
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

    private void setupNetworkInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        mac = info.getMacAddress();
    }
    
}
