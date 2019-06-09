package com.example.realestate.data.remote.response;

import com.example.realestate.data.model.EstateDetail;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 05/06/2019
 */
public class SubmitEstateResponse {

    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("project")
    private EstateDetail project;

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

    public EstateDetail getEstateDetail() {
        return project;
    }

    public void setEstateDetail(EstateDetail project) {
        this.project = project;
    }
}
