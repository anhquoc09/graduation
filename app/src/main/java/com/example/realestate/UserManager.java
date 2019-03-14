package com.example.realestate;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.realestate.data.model.Profile;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public final class UserManager {

    private static User sCurrentUser;

    private static int sIsNewUser = -1;

    private static UserLocalStorage mUserLocalStorage;

    private static CheckInStorage mCheckInStorage;

    private UserManager() {
    }

    public static synchronized void init(Context context) {
        if (mUserLocalStorage == null) {
            mUserLocalStorage = new UserLocalStorage(context);
        }

        if (sCurrentUser == null && mUserLocalStorage.isLoggedIn()) {
            sCurrentUser = mUserLocalStorage.loadUser();
        }

        if (mCheckInStorage == null) {
            mCheckInStorage = new CheckInStorage(context);
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

    public static synchronized void initUser(User currentUser, int loginMethod) {
        setCurrentUser(currentUser);
    }

    public static synchronized boolean isUserLoggedIn() {
        final User user = getCurrentUser();
        return user != null && !TextUtils.isEmpty(user.getAccessToken());
    }

    public static synchronized void setGoogleToken(String token) {
        mUserLocalStorage.saveGoogleToken(token);
    }

    public static synchronized String getGoogleToken() {
        return mUserLocalStorage.getGoogleToken();
    }

    public synchronized static void clearSession() {
        mUserLocalStorage.clear();
        mCheckInStorage.clear();
        sCurrentUser = null;
    }

    public static synchronized void setIsNewUser(boolean isNewUser) {
        sIsNewUser = isNewUser ? 1 : 0;
        mUserLocalStorage.setIsNewRegisterUser(sIsNewUser);
    }

    public static synchronized boolean isNewUser() {
        if (sIsNewUser == -1) {
            sIsNewUser = mUserLocalStorage.getIsNewRegisterUser();
        }
        return sIsNewUser == 1;
    }

    /**
     * {@link UserLocalStorage}
     */
    private static final class UserLocalStorage {

        private static final String NAME = "user_prefs";

        private static final String PREF_USER_SESSION = "key_user_session";

        private static final String PREF_USER_ID = "key_user_id";

        private static final String PREF_USER_NAME = "key_user_name_key";

        private static final String PREF_DISPLAY_NAME = "key_display_name_key";

        private static final String PREF_USER_AVATAR = "key_user_avatar_60_key";

        private static final String PREF_USER_EMAIL = "key_user_email";

        private static final String PREF_USER_BIRTHDAY = "key_user_birth_date";

        private static final String PREF_USER_PHONE = "key_user_phone";

        private static final String PREF_USER_GOOGLE_TOKEN = "key_user_google_token";

        private static final String PREF_IS_NEW_REGISTER_USER = "key_is_new_register_user";

        private SharedPreferences sSettings = null;

        private UserLocalStorage(Context context) {
            sSettings = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }

        public synchronized String getUserSession() {
            return sSettings.getString(PREF_USER_SESSION, "");
        }

        public synchronized boolean isLoggedIn() {
            return !TextUtils.isEmpty(getUserSession());
        }

        public synchronized boolean saveUser(User user) {
            if (user == null) {
                return false;
            }
            sSettings.edit()
                    .putString(PREF_USER_SESSION, user.getAccessToken())
                    .putInt(PREF_USER_ID, user.getUserId())
                    .putString(PREF_USER_PHONE, user.getPhoneNumber())
                    .apply();

            saveProfile(user.getProfile());
            return true;
        }

        public synchronized User loadUser() {
            User user = new User();
            user.setAccessToken(sSettings.getString(PREF_USER_SESSION, ""));
            user.setPhoneNumber(sSettings.getString(PREF_USER_PHONE, ""));
            user.setProfile(loadProfile());
            return user;
        }

        public synchronized void saveProfile(Profile profile) {
            if (profile != null) {
                SharedPreferences.Editor editor = sSettings.edit();
                editor.putInt(PREF_USER_ID, profile.getUserId());
                editor.putString(PREF_USER_NAME, profile.getUserName());
                editor.putString(PREF_DISPLAY_NAME, profile.getDisplayName());
                editor.putString(PREF_USER_AVATAR, profile.getAvatar());
                editor.putString(PREF_USER_EMAIL, profile.getEmail());
                editor.putInt(PREF_USER_BIRTHDAY, profile.getBirthday());
                editor.apply();
            }
        }

        private synchronized Profile loadProfile() {
            final Profile profile = new Profile();
            profile.setUserId(sSettings.getInt(PREF_USER_ID, -1));
            profile.setUserName(sSettings.getString(PREF_USER_NAME, ""));
            profile.setDisplayName(sSettings.getString(PREF_DISPLAY_NAME, ""));
            profile.setAvatar(sSettings.getString(PREF_USER_AVATAR, ""));
            profile.setEmail(sSettings.getString(PREF_USER_EMAIL, ""));
            profile.setBirthday(sSettings.getInt(PREF_USER_BIRTHDAY, 0));
            return profile;
        }

        public synchronized void clear() {
            sSettings.edit().clear().apply();
        }

        public synchronized void saveGoogleToken(String token) {
            sSettings.edit().putString(PREF_USER_GOOGLE_TOKEN, token).apply();
        }

        public synchronized String getGoogleToken() {
            return sSettings.getString(PREF_USER_GOOGLE_TOKEN, "");
        }

        public void setIsNewRegisterUser(int flag) {
            sSettings.edit().putInt(PREF_IS_NEW_REGISTER_USER, flag).apply();
        }

        public int getIsNewRegisterUser() {
            return sSettings.getInt(PREF_IS_NEW_REGISTER_USER, 0);
        }

    }

    private static final class CheckInStorage {

        private static final String NAME = "check_in_prefs";

        private static final String PREF_CHECK_IN_GIFT_DETAILS = "key_check_in_gift_details";

        private SharedPreferences mSettings = null;

        private CheckInStorage(Context context) {
            mSettings = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        }

        public String getCheckInGiftDetails() {
            return mSettings.getString(PREF_CHECK_IN_GIFT_DETAILS, null);
        }

        public void saveCheckInGiftDetails(String giftDetailsJson) {
            mSettings.edit().putString(PREF_CHECK_IN_GIFT_DETAILS, giftDetailsJson).apply();
        }

        public void clear() {
            mSettings.edit().clear().apply();
        }
    }
}