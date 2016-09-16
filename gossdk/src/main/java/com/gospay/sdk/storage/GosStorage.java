package com.gospay.sdk.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.gospay.sdk.R;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.util.TextUtils;

/**
 * Created by bertalt on 31.08.16.
 */
public final class GosStorage {
    private static GosStorage ourInstance;
    private Context context;
    private SharedPreferences prefStorage;
    private static final String DEFAULT_ID = "-1";
    private String mApiKey;

    public static GosStorage newInstance(Context context){
        ourInstance = new GosStorage(context);
        return ourInstance;
    }


    public static GosStorage getInstance() {

        if(ourInstance == null)
            throw new GosSdkException("You try to get instance of SDK storage before create()");

        return ourInstance;
    }

    private GosStorage(Context context) {
        this.context = context;
        prefStorage = context.getSharedPreferences(context.getString(R.string.prefStorageName), Context.MODE_PRIVATE);
    }

    /**
     *
     * @return language code  of user locale, for example: en - english or ru - russian
     */
    public String getLanguage(){
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    public String getApiKey(){

        if(TextUtils.isEmpty(mApiKey)) {

            ApplicationInfo ai = null;
            try {
                ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            } catch (PackageManager.NameNotFoundException | NullPointerException e) {
                e.printStackTrace();
            }
            Bundle bundle = ai.metaData;
            mApiKey = bundle.getString("com.gospay.sdk.V1");

        }
        if(TextUtils.isEmpty(mApiKey))
            throw new GosSdkException("Cannot find GOS API_KEY ");

        return mApiKey;
    }


    /**
     *
     * @param cardId
     * @param cvv
     * @return true if result success
     */
    public boolean addCardWithCvv(int cardId, String cvv){

        if(prefStorage == null) throw new GosSdkException("Storage is null");

        return prefStorage.edit().putString(String.valueOf(cardId), cvv).commit();

    }

    public String getCvvByCardId(int cardId){

        if(prefStorage == null) throw new GosSdkException("Storage is null");

        String cvv = prefStorage.getString(String.valueOf(cardId), DEFAULT_ID);

        if(cvv.equalsIgnoreCase(DEFAULT_ID)){
            return null;
        }
        else{
            return cvv;
        }

    }

    /**
     * Clear preference storage
     */
    public void clear() {

        if(prefStorage == null) throw new GosSdkException("Storage is null");

        prefStorage.edit().clear().apply();

    }


    public void destroy() {

        ourInstance = null;
        context = null;
    }
}
