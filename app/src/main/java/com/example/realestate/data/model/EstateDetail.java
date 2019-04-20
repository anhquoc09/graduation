package com.example.realestate.data.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EstateDetail implements Serializable {

    @SerializedName("imageUrl")
    private String mImageUrl;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("time")
    private String mTime;
    @SerializedName("price")
    private String mPrice;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("type")
    private String mType;
    @SerializedName("address")
    private String mAddress;
    @SerializedName("square")
    private String mSquare;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("contact")
    private String mContact;
    @SerializedName("ownerId")
    private int mOwnerId;
    @SerializedName("ownerAvatar")
    private String mOwnerAvatar;
    @SerializedName("ownerDisplayName")
    private String mOwnerDisplayName;
    @SerializedName("ownerProvince")
    private String mOwnerProvince;
    @SerializedName("latitude")
    private String mLatitude;
    @SerializedName("longitude")
    private String mLongitude;

    public EstateDetail(String imageUrl, String title, String time, String price, String status, String type, String address, String square, String description, String contact, int ownerId, String ownerAvatar, String ownerDisplayName, String ownerProvince, String latitude, String longitude) {
        mImageUrl = imageUrl;
        mTitle = title;
        mTime = time;
        mPrice = price;
        mStatus = status;
        mType = type;
        mAddress = address;
        mSquare = square;
        mDescription = description;
        mContact = contact;
        mOwnerId = ownerId;
        mOwnerAvatar = ownerAvatar;
        mOwnerDisplayName = ownerDisplayName;
        mOwnerProvince = ownerProvince;
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
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

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getSquare() {
        return mSquare;
    }

    public void setSquare(String square) {
        mSquare = square;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String contact) {
        mContact = contact;
    }

    public int getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(int ownerId) {
        mOwnerId = ownerId;
    }

    public String getOwnerAvatar() {
        return mOwnerAvatar;
    }

    public void setOwnerAvatar(String ownerAvatar) {
        mOwnerAvatar = ownerAvatar;
    }

    public String getOwnerDisplayName() {
        return mOwnerDisplayName;
    }

    public void setOwnerDisplayName(String ownerDisplayName) {
        mOwnerDisplayName = ownerDisplayName;
    }

    public String getOwnerProvince() {
        return mOwnerProvince;
    }

    public void setOwnerProvince(String ownerProvince) {
        mOwnerProvince = ownerProvince;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(mLatitude), Double.parseDouble(mLongitude));
    }
}
