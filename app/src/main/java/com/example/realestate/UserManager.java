package com.example.realestate;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.realestate.data.model.Profile;

import java.util.Calendar;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public final class UserManager {

    private static User sCurrentUser;

    private static UserLocalStorage mUserLocalStorage;

    public static synchronized void init(Context context) {
        if (mUserLocalStorage == null) {
            mUserLocalStorage = new UserLocalStorage(context);
        }

        if (sCurrentUser == null && mUserLocalStorage.isLoggedIn()) {
            sCurrentUser = mUserLocalStorage.loadUser();
        }
    }

    public static synchronized User getCurrentUser() {
        if (sCurrentUser == null) {
            sCurrentUser = mUserLocalStorage.loadUser();
        }
        return sCurrentUser;
    }

    public static synchronized void setCurrentUser(User currentUser) {
        sCurrentUser = currentUser;
        mUserLocalStorage.saveUser(currentUser);
    }

    public static synchronized boolean isUserLoggedIn() {
        final User user = getCurrentUser();
        return user != null && Calendar.getInstance().getTimeInMillis() < user.getTokenExpiredTime() && !TextUtils.isEmpty(user.getAccessToken());
    }

    public static synchronized void setAccessToken(String token) {
        if (sCurrentUser != null) {
            sCurrentUser.setAccessToken(token);
        }
        mUserLocalStorage.saveAccessToken(token);
    }

    public static synchronized String getAccessToken() {
        if (sCurrentUser != null) {
            return sCurrentUser.getAccessToken();
        }
        return mUserLocalStorage.getAccessToken();
    }

    public synchronized static void clearSession() {
        mUserLocalStorage.clear();
        sCurrentUser = null;
    }

    /**
     * {@link UserLocalStorage}
     */
    private static final class UserLocalStorage {

        private static final String NAME = "user_prefs";

        private static final String PREF_USER_TOKEN_EXPIRED_TIME = "key_user_session_expired_time";
        private static final String PREF_USER_SESSION = "key_user_session";

        private static final String PREF_USER_IDENTIFY = "key_user_identify";
        private static final String PREF_USER_ID = "key_user_id";
        private static final String PREF_USER_NAME = "key_user_full_name";
        private static final String PREF_USER_ADDRESS = "key_user_address";
        private static final String PREF_USER_PHONE = "key_user_phone";
        private static final String PREF_USER_DESCRIPTION = "key_user_description";
        private static final String PREF_USER_EMAIL = "key_user_email";
        private static final String PREF_USER_TOTAL_PROJECT = "key_user_total_project";
        private static final String PREF_USER_STATUS = "key_user_status";
        private static final String PREF_USER_AVATAR = "key_user_avatar";
        private static final String PREF_USER_COMPANY = "key_user_company";
        private static final String PREF_USER_VERIFY = "key_user_verify";
        private static final String PREF_USER_LOCK = "key_user_lock";
        private static final String PREF_USER_HASH = "key_user_hash";
        private static final String PREF_USER_V = "key_user_v";

        private SharedPreferences sSettings;

        private UserLocalStorage(Context context) {
            sSettings = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }

        public synchronized String getUserSession() {
            return sSettings.getString(PREF_USER_SESSION, "");
        }

        public synchronized boolean isLoggedIn() {
            return !TextUtils.isEmpty(getUserSession());
        }

        public synchronized void saveUser(User user) {
            if (user == null) {
                return;
            }
            sSettings.edit().putLong(PREF_USER_TOKEN_EXPIRED_TIME, user.getTokenExpiredTime()).apply();
            sSettings.edit().putString(PREF_USER_SESSION, user.getAccessToken()).apply();

            saveProfile(user.getProfile());
        }

        public synchronized User loadUser() {
            User user = new User();
            user.setTokenExpiredTime(sSettings.getLong(PREF_USER_TOKEN_EXPIRED_TIME, 0));
            user.setAccessToken(sSettings.getString(PREF_USER_SESSION, ""));
            user.setProfile(loadProfile());
            return user;
        }

        public synchronized void saveProfile(Profile profile) {
            if (profile != null) {
                SharedPreferences.Editor editor = sSettings.edit();
                editor.putString(PREF_USER_IDENTIFY, profile.getIdentify());
                editor.putString(PREF_USER_ID, profile.getId());
                editor.putString(PREF_USER_NAME, profile.getFullname());
                editor.putString(PREF_USER_ADDRESS, profile.getAddress());
                editor.putString(PREF_USER_PHONE, profile.getPhone());
                editor.putString(PREF_USER_DESCRIPTION, profile.getDescription());
                editor.putString(PREF_USER_EMAIL, profile.getEmail());
                editor.putInt(PREF_USER_TOTAL_PROJECT, profile.getTotalPost());
                editor.putInt(PREF_USER_STATUS, profile.getStatusAccount());
                editor.putString(PREF_USER_AVATAR, profile.getAvatar());
                editor.putString(PREF_USER_COMPANY, profile.getCompany());
                editor.putBoolean(PREF_USER_VERIFY, profile.getVerify());
                editor.putBoolean(PREF_USER_LOCK, profile.getLock());
                editor.putInt(PREF_USER_HASH, profile.getHash());
                editor.putInt(PREF_USER_V, profile.getV());

                editor.apply();
            }
        }

        private synchronized Profile loadProfile() {
            final Profile profile = new Profile();
            profile.setIdentify(sSettings.getString(PREF_USER_IDENTIFY, ""));
            profile.setId(sSettings.getString(PREF_USER_ID, ""));
            profile.setFullname(sSettings.getString(PREF_USER_NAME, ""));
            profile.setAddress(sSettings.getString(PREF_USER_ADDRESS, ""));
            profile.setPhone(sSettings.getString(PREF_USER_PHONE, ""));
            profile.setDescription(sSettings.getString(PREF_USER_DESCRIPTION, ""));
            profile.setEmail(sSettings.getString(PREF_USER_EMAIL, ""));
            profile.setTotalPost(sSettings.getInt(PREF_USER_TOTAL_PROJECT, 0));
            profile.setStatusAccount(sSettings.getInt(PREF_USER_STATUS, 0));
            profile.setAvatar(sSettings.getString(PREF_USER_AVATAR, ""));
            profile.setCompany(sSettings.getString(PREF_USER_COMPANY, ""));
            profile.setVerify(sSettings.getBoolean(PREF_USER_VERIFY, false));
            profile.setLock(sSettings.getBoolean(PREF_USER_LOCK, false));
            profile.setHash(sSettings.getInt(PREF_USER_HASH, 0));
            profile.setV(sSettings.getInt(PREF_USER_V, 0));

            return profile;
        }

        public synchronized void clear() {
            sSettings.edit().clear().apply();
        }

        public synchronized void saveTokenExpiredTime(Long time) {
            sSettings.edit().putLong(PREF_USER_SESSION, time).apply();
        }

        public synchronized Long getTokenExpiredTime() {
            return sSettings.getLong(PREF_USER_SESSION, 0);
        }

        public synchronized void saveAccessToken(String token) {
            sSettings.edit().putString(PREF_USER_SESSION, token).apply();
        }

        public synchronized String getAccessToken() {
            return sSettings.getString(PREF_USER_SESSION, "");
        }
    }
}