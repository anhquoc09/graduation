package com.example.realestate;

import com.example.realestate.data.model.LoginData;
import com.example.realestate.data.model.Profile;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public class User {

    private Profile mProfile;

//    private Credentials mCredentials;

    public User() {
        mProfile = new Profile();
//        mCredentials = new Credentials();
    }

    public User(LoginData loginData) {
        mProfile = new Profile();
        mProfile.setUserId(loginData.getUserId());
        mProfile.setPhoneNumber(loginData.getPhoneNumber());

//        mCredentials = new Credentials(loginData.getAccessToken(), loginData.getTokenExpiredTime());
    }

    public String getPhoneNumber() {
        return mProfile.getPhoneNumber();
    }

    public String getAccessToken() {
        return "123456";
//        return mCredentials.getAccessToken();
    }

    public void setAccessToken(String accessToken) {
//        mCredentials.setAccessToken(accessToken);
    }

    public void setPhoneNumber(String phoneNumber) {
        mProfile.setPhoneNumber(phoneNumber);
    }

//    public long getTokenExpiredTime() {
//        return mCredentials.getTokenExpiredTime();
//    }
//
//    public void setTokenExpiredTime(long tokenExpiredTime) {
//        mCredentials.setTokenExpiredTime(tokenExpiredTime);
//    }
//
    public Profile getProfile() {
        return mProfile;
    }

    public void setProfile(Profile profile) {
        mProfile = profile;
    }

    public String getAvatar() {
        return mProfile.getAvatar();
    }

    public void setAvatar(String avatar) {
        mProfile.setAvatar(avatar);
    }

    public String getDisplayName() {
        return mProfile.getDisplayName();
    }

    public void setDisplayName(String displayName) {
        mProfile.setDisplayName(displayName);
    }

    public String getUserName() {
        return mProfile.getUserName();
    }

    public void setUserName(String userName) {
        mProfile.setUserName(userName);
    }

    public int getType() {
        return mProfile.getType();
    }

    public int getUserId() {
        return mProfile.getUserId();
    }


    public String getEmail() {
        return mProfile.getEmail();
    }

    public void setEmail(String email) {
        mProfile.setEmail(email);
    }

    public String getAddress() {
        return mProfile.getAddress();
    }

    public void setAddress(String address) {
        mProfile.setAddress(address);
    }

    public String getBirthday() {
        return mProfile.getBirthday();
    }

    public void setBirthday(String birthday) {
        mProfile.setBirthday(birthday);
    }

    public boolean isVerified() {
        return mProfile.isVerified();
    }

    public void setVerified(boolean verified) {
        mProfile.setVerified(verified);
    }

    public boolean isDev() {
        return getType() == 1000;
    }
}
