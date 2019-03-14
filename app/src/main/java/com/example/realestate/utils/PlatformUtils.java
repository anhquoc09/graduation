package com.example.realestate.utils;

import android.os.Build;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public final class PlatformUtils {

    public static final int ANDROID_PLATFORM_ID = 1;

    public static final int IOS_PLATFORM_ID = 2;

    public static final int WINDOW_PHONE_PLATFORM_ID = 3;

    private PlatformUtils() {
        throw new UnsupportedOperationException("Not allow instantiating object.");
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasKitKatWatch() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasLollipopMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean isAndroidO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

}
