package com.example.realestate.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.realestate.EstateApplication;

import androidx.annotation.StringRes;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public final class AndroidUtilities {

    private static String sDeviceId = null;

    public static float sDensity = 1;

    private static final Point sDisplaySize = new Point(0, 0);

    private static final String TAG = "Utils";

    private static final String sPackageName;

    private static final Handler sUIHandler = new Handler(Looper.getMainLooper());

    private AndroidUtilities() {
        throw new UnsupportedOperationException("Not allow instantiating object.");
    }

    static {
        sDensity = EstateApplication.getInstance().getResources().getDisplayMetrics().density;
        sPackageName = EstateApplication.getInstance().getPackageName();
    }

    public static String getDeviceId() {
        Context context = EstateApplication.getInstance();
        if (TextUtils.isEmpty(sDeviceId)) {
            try {
                sDeviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage() + "unique device id");
            }
        }
        return sDeviceId;
    }

    public static String currentOsVersion() {
        return Build.VERSION.RELEASE;
    }

    public static int currentOsVersionCode() {
        return Build.VERSION.SDK_INT;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }
//
//    public static Point getDisplaySize(Context context) {
//        if (sDisplaySize.x == 0 && sDisplaySize.y == 0) {
//            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//            wm.getDefaultDisplay().getSize(sDisplaySize);
//        }
//        return new Point(sDisplaySize);
//    }

    public static void showToast(final String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (isOnMainThread()) {
            Toast.makeText(EstateApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
        } else {
            sUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EstateApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public static void showToast(@StringRes int stringRes, Object... arguments) {
        final Context context = EstateApplication.getInstance().getApplicationContext();
        try {
            final String message = context.getString(stringRes, arguments);
            if (isOnMainThread()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                sUIHandler.post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
            }
        } catch (Resources.NotFoundException ex) {
            Log.e(TAG, "showToast Resource Id " + stringRes + " not found.", ex);
        }
    }

    public static void showToast(@StringRes int stringRes) {
        final Context context = EstateApplication.getInstance().getApplicationContext();
        try {
            final String message = context.getString(stringRes);
            if (isOnMainThread()) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            } else {
                sUIHandler.post(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
            }
        } catch (Resources.NotFoundException ex) {
            Log.e(TAG, "showToast Resource Id " + stringRes + " not found.", ex);
        }
    }

//    public static Snackbar showSnackbar(View coordinator, String msgId, String action, final View.OnClickListener listener) {
//        Snackbar snackbar = Snackbar.make(coordinator,
//                msgId, Snackbar.LENGTH_INDEFINITE);
//        if (listener != null) {
//            snackbar.setAction(action, listener);
//        }
//
//        snackbar.show();
//        return snackbar;
//    }
//
//    public static Snackbar showSnackbar(View coordinator, int msgId, int action, final View.OnClickListener listener) {
//        Snackbar snackbar = Snackbar.make(coordinator,
//                msgId, Snackbar.LENGTH_INDEFINITE);
//        if (listener != null) {
//            snackbar.setAction(action, listener);
//        }
//
//        snackbar.show();
//        return snackbar;
//    }
//
//    public static void hideSnackbar(Snackbar snackbar) {
//        if (isSnackbarShowing(snackbar)) {
//            snackbar.dismiss();
//        }
//    }
//
//    public static boolean isSnackbarShowing(Snackbar snackbar) {
//        return snackbar != null && snackbar.isShown();
//    }
//
//    public static boolean isTablet(Context context) {
//        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
//                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
//    }
//
//    public static boolean hasLollipop() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
//    }
//
//    public static String getPackageName() {
//        return sPackageName;
//    }

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

//    public static Uri resourceToUri(Context context, int resID) {
//        if (context == null) {
//            return Uri.parse("");
//        }
//        try {
//            return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                    context.getResources().getResourcePackageName(resID) + '/' +
//                    context.getResources().getResourceTypeName(resID) + '/' +
//                    context.getResources().getResourceEntryName(resID));
//        } catch (Exception ignored) {
//            return Uri.parse("");
//        }
//    }
//
//    public static void openAppSettings(Activity activity, int requestCode) {
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
//        intent.setData(uri);
//        activity.startActivityForResult(intent, requestCode);
//    }
}
