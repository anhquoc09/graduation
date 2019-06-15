package com.example.realestate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 15/06/2019
 */
public class SavedProject {
    @SerializedName("_id")
    private String id;

    @SerializedName("project")
    private EstateDetail project;

    @SerializedName("createTime")
    private Integer createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public EstateDetail getEstateDetail() {
        return project;
    }

    public void setEstateDetail(EstateDetail project) {
        this.project = project;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }
}
