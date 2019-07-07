package com.reales.realestate.data.remote.rest;

import com.reales.realestate.data.model.EstateDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by quocha2
 * On 08/06/2019
 */
public class UserListEstateResponse {


    @SerializedName("status")
    private Integer status;

    @SerializedName("message")
    private String message;

    @SerializedName("page")
    private Integer page;

    @SerializedName("count")
    private Integer count;

    @SerializedName("projects")
    private List<EstateDetail> projects = null;

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<EstateDetail> getEstateDetails() {
        return projects;
    }

    public void setEstateDetails(List<EstateDetail> projects) {
        this.projects = projects;
    }
}
