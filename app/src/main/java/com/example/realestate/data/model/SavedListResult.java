package com.example.realestate.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by quocha2
 * On 15/06/2019
 */
public class SavedListResult {
    @SerializedName("_id")
    private String id;

    @SerializedName("userid")
    private String userid;

    @SerializedName("fullname")
    private String fullname;

    @SerializedName("projects")
    private List<SavedPost> projects = null;

    @SerializedName("__v")
    private Integer v;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public List<SavedPost> getSavedPosts() {
        return projects;
    }

    public void setSavedPosts(List<SavedPost> projects) {
        this.projects = projects;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }
}
