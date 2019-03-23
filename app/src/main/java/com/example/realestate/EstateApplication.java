package com.example.realestate;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.bumptech.glide.Glide;
import com.example.realestate.utils.PlatformUtils;

import java.util.Locale;

import androidx.multidex.MultiDex;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class EstateApplication extends Application {

    private static volatile EstateApplication sInstance = null;

    public static EstateApplication getInstance() {
        return sInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        Context applicationContext = getApplicationContext();

        UserManager.init(applicationContext);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Locale newLocale;
        if (PlatformUtils.hasNougat()) {
            newLocale = newConfig.getLocales().get(0);
        } else {
            newLocale = newConfig.locale;
        }

//        LocaleController.applyLanguage(this, newLocale);
    }
}
