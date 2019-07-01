package com.example.realestate.data.remote.rest;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 01/07/2019
 */
public class SimpleResponse {

    @SerializedName("status")
    private int mStatus;

    @SerializedName("message")
    private String mMessage;

    public SimpleResponse(int status, String message) {
        mStatus = status;
        mMessage = message;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
