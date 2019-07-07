package com.reales.realestate.data.remote.response;

import com.reales.realestate.data.model.SaveResult;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 11/06/2019
 */
public class SaveEstateResponse {
    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("result")
    private SaveResult result;


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

    public SaveResult getSaveResult() {
        return result;
    }

    public void setSaveResult(SaveResult result) {
        this.result = result;
    }
}
