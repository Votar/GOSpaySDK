package com.gospay.sdk.util;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

import com.gospay.sdk.R;


/**
 * Created by bertalt on 15.09.16.
 */
public class UiUtil {

    private static ProgressDialog progressDialog;

    public static void showDefaultProgressDialog(FragmentActivity c) {

        progressDialog = new ProgressDialog(c);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(c.getString(R.string.text_progress_dialog));

        progressDialog.show();
    }

    public static void dismissProgressDialog() {

        if (progressDialog != null)
            progressDialog.dismiss();

        progressDialog = null;
    }


}
