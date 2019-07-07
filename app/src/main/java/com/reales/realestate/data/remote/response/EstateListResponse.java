package com.reales.realestate.data.remote.response;

import com.reales.realestate.data.model.EstateDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EstateListResponse {
    @SerializedName("status")
    private Integer status;

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
