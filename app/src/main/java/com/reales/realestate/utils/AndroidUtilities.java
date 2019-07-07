package com.reales.realestate.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.reales.realestate.EstateApplication;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public final class AndroidUtilities {

    private static final Handler sUIHandler = new Handler(Looper.getMainLooper());

    private AndroidUtilities() {
        throw new UnsupportedOperationException("Not allow instantiating object.");
    }

    public static void showToast(final String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (isOnMainThread()) {
            Toast.makeText(EstateApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
        } else {
            sUIHandler.post(() -> Toast.makeText(EstateApplication.getInstance(), message, Toast.LENGTH_SHORT).show());
        }
    }

    public static String getString(int stringRes) {
        try {
            return EstateApplication.getInstance().getString(stringRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
