package com.example.realestate.data.model;

import java.io.Serializable;

public class EstateDetail implements Serializable {
    private String mImage;
    private String mTitle;
    private String mTime;
    private String mPrice;
    private String mStatus;
    private String mPoster;

    public EstateDetail(String image, String title, String time, String price, String status, String poster) {
        mImage = image;
        mTitle = title;
        mTime = time;
        mPrice = price;
        mStatus = status;
        mPoster = poster;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getOwnerName() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getOwnerAvatar() {
        return "";
    }

    public String getOwnerProvince() {
        return "";
    }

    public String getType() {
        return "";
    }

    public String getAddress() {
        return "";
    }

    public String getSquare() {
        return "";
    }

    public String getDescription() {
        return "";
    }

    public String getPhoneNumber() {
        return "0348898600";
    }
}
