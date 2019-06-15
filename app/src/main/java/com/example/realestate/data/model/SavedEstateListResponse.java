package com.example.realestate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 15/06/2019
 */
public class SavedEstateListResponse {

    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("count")
    private Integer count;

    @SerializedName("result")
    private SavedListResult result;


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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public SavedListResult getSavedListResult() {
        return result;
    }

    public void setSavedListResult(SavedListResult result) {
        this.result = result;
    }
}
