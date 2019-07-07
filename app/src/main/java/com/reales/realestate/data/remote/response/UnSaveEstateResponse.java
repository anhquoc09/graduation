package com.reales.realestate.data.remote.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 14/06/2019
 */
public class UnSaveEstateResponse {
    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;


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
}
