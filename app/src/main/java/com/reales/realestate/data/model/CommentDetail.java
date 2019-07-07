package com.reales.realestate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 05/07/2019
 */
public class CommentDetail {

    @SerializedName("_id")
    private String mId;

    @SerializedName("user")
    private UserComment mUser;

    @SerializedName("createTime")
    private Long mCreateTime;

    @SerializedName("updateTime")
    private Long mUpdateTime;

    @SerializedName("projectId")
    private String mProjectId;

    @SerializedName("content")
    private String mContent;

    @SerializedName("star")
    private Integer mStar;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public UserComment getUser() {
        return mUser;
    }

    public void setUser(UserComment user) {
        mUser = user;
    }

    public Long getCreateTime() {
        return mCreateTime;
    }

    public void setCreateTime(Long createTime) {
        mCreateTime = createTime;
    }

    public Long getUpdateTime() {
        return mUpdateTime;
    }

    public void setUpdateTime(Long updateTime) {
        mUpdateTime = updateTime;
    }

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        mProjectId = projectId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public Integer getStar() {
        return mStar;
    }

    public void setStar(Integer star) {
        mStar = star;
    }
}
