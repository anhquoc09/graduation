package com.example.realestate;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.cloudinary.android.MediaManager;
import com.example.realestate.utils.PlatformUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.multidex.MultiDex;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class EstateApplication extends Application {

    public static final String BEARER_TOKEN = "Bearer ";

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

        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dne3aha8f");
        MediaManager.init(this, config);
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
