package com.reales.realestate.data.remote.rest;

import com.reales.realestate.data.model.Profile;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 08/06/2019
 */
public class UserProfileResponse {
    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("page")
    private Integer page;

    @SerializedName("info")
    private Profile info;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Profile getInfo() {
        return info;
    }

    public void setInfo(Profile info) {
        this.info = info;
    }
}
