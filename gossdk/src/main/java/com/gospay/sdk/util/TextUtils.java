package com.gospay.sdk.util;

import android.support.annotation.Nullable;

/**
 * Created by bertalt on 01.09.16.
 */
public class TextUtils {
    public static boolean isEmpty(@Nullable CharSequence str) {
        if (str == null || str.length() == 0)
            return true;
        else
            return false;
    }
}
