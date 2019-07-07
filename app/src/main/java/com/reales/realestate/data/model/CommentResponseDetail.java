package com.reales.realestate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 06/07/2019
 */
public class CommentResponseDetail {

    @SerializedName("_id")
    private String mId;

    @SerializedName("user")
    private String mUserId;

    @SerializedName("projectId")
    private String mProjectId;

    @SerializedName("createTime")
    private Long mCreateTime;

    @SerializedName("updateTime")
    private Long mUpdateTime;

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

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getProjectId() {
        return mProjectId;
    }

    public void setProjectId(String projectId) {
        mProjectId = projectId;
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
