package com.example.realestate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 14/06/2019
 */
public class SaveResult {
    @SerializedName("_id")
    private String id;

    @SerializedName("project")
    private String project;

    @SerializedName("createTime")
    private String createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost() {
        return project;
    }

    public void setPost(String project) {
        this.project = project;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
