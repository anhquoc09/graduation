package com.example.realestate;

import com.example.realestate.data.model.Profile;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public class User {

    private Profile mProfile;
    private long mTokenExpiredTime;
    private String mAccessToken;

    public User() {
        mProfile = new Profile();
    }

    public User(Profile profile) {
        mProfile = profile;
    }

    public Profile getProfile() {
        return mProfile;
    }

    public void setProfile(Profile profile) {
        mProfile = profile;
    }

    public String getIdentify() {
        return mProfile.getIdentify();
    }

    public void setIdentify(String identify) {
        mProfile.setIdentify(identify);
    }

    public String getId() {
        return mProfile.getId();
    }

    public void setId(String id) {
        mProfile.setId(id);
    }

    public String getFullname() {
        return mProfile.getFullname();
    }

    public void setFullname(String fullname) {
        mProfile.setFullname(fullname);
    }

    public String getAddress() {
        return mProfile.getAddress();
    }

    public void setAddress(String address) {
        mProfile.setAddress(address);
    }

    public String getPhone() {
        return mProfile.getPhone();
    }

    public void setPhone(String phone) {
        mProfile.setPhone(phone);
    }

    public String getDescription() {
        return mProfile.getDescription();
    }

    public void setDescription(String description) {
        mProfile.setDescription(description);
    }

    public String getEmail() {
        return mProfile.getEmail();
    }

    public void setEmail(String email) {
        mProfile.setEmail(email);
    }

    public Integer getTotalProject() {
        return mProfile.getTotalProject();
    }

    public void setTotalProject(Integer totalProject) {
        mProfile.setTotalProject(totalProject);
    }

    public Integer getStatusAccount() {
        return mProfile.getStatusAccount();
    }

    public void setStatusAccount(Integer statusAccount) {
        mProfile.setStatusAccount(statusAccount);
    }

    public String getAvatar() {
        return mProfile.getAvatar();
    }

    public void setAvatar(String avatar) {
        mProfile.setAvatar(avatar);
    }

    public String getCompany() {
        return mProfile.getCompany();
    }

    public void setCompany(String company) {
        mProfile.setCompany(company);
    }

    public boolean getVerify() {
        return mProfile.getVerify();
    }

    public void setVerify(boolean verify) {
        mProfile.setVerify(verify);
    }

    public boolean getLock() {
        return mProfile.getLock();
    }

    public void setLock(boolean lock) {
        mProfile.setLock(lock);
    }

    public Integer getHash() {
        return mProfile.getHash();
    }

    public void setHash(Integer hash) {
        mProfile.setHash(hash);
    }

    public Integer getV() {
        return mProfile.getV();
    }

    public void setV(Integer v) {
        mProfile.setV(v);
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String accessToken) {
        mAccessToken = accessToken;
    }

    public long getTokenExpiredTime() {
        return mTokenExpiredTime;
    }

    public void setTokenExpiredTime(long tokenExpiredTime) {
        mTokenExpiredTime = tokenExpiredTime;
    }
}
