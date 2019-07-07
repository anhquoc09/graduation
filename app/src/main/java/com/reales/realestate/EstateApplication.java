package com.reales.realestate;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.cloudinary.android.MediaManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class EstateApplication extends Application {

    public static final String BEARER_TOKEN = "Bearer ";

    private static List<String> mSavedList = new ArrayList<>();

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

    public static void addSavedPostId(String projectId) {
        mSavedList.add(projectId);
    }

    public static void removeSavedPostId(String projectId) {
        int index = mSavedList.indexOf(projectId);
        if (index > -1) {
            mSavedList.remove(index);
        }
    }

    public static boolean savedContain(String projectId) {
        return mSavedList.contains(projectId);
    }

    public static void clearSavedList() {
        mSavedList.clear();
    }
}