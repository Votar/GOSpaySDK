package com.gos.rabbit.gossdk.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.gos.rabbit.gossdk.R;

import java.util.Locale;

/**
 * Created by bertalt on 31.08.16.
 */
public class SdkStorage {
    private static SdkStorage ourInstance;
    private Context context;
    private SharedPreferences storage;

    public static SdkStorage getInstance(Context context) {

        if(ourInstance == null) ourInstance = new SdkStorage(context);

        return ourInstance;
    }

    private SdkStorage(Context context) {
        this.context = context;
        storage = context.getSharedPreferences(context.getString(R.string.prefName), Context.MODE_PRIVATE);
    }

    /**
     *
     * @return language code  of user locale, for example: en - english or ru - russian
     */
    public String getLocale(){

        return storage.getString(context.getString(R.string.prefLocale), Locale.getDefault().getLanguage());
    }

    public void setLocale(Locale userLocale){

        storage.edit().putString(context.getString(R.string.prefLocale), userLocale.getLanguage()).apply();

    }


}
