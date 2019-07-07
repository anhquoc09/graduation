package com.reales.realestate.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Profile implements Serializable {

    @SerializedName("identify")
    private String identify;

    @SerializedName("_id")
    private String id;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("address")
    private String address;

    @SerializedName("phone")
    private String phone;

    @SerializedName("description")
    private String description;

    @SerializedName("email")
    private String email;

    @SerializedName("totalProject")
    private Integer totalProject;

    @SerializedName("statusAccount")
    private Integer statusAccount;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("company")
    private String company;

    @SerializedName("verify")
    private Boolean verify;

    @SerializedName("lock")
    private Boolean lock;

    @SerializedName("hash")
    private Integer hash;

    @SerializedName("__v")
    private Integer v;

    public String getIdentify() {
        return identify;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getTotalPost() {
        return totalProject;
    }

    public void setTotalPost(Integer totalProject) {
        this.totalProject = totalProject;
    }

    public Integer getStatusAccount() {
        return statusAccount;
    }

    public void setStatusAccount(Integer statusAccount) {
        this.statusAccount = statusAccount;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
