package com.example.realestate.data.remote.response;

import com.example.realestate.data.model.CommentDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by quocha2
 * On 06/07/2019
 */
public class ListCommentResponse {
    @SerializedName("status")
    private Integer mStatus;

    @SerializedName("count")
    private Integer mCount;

    @SerializedName("comments")
    private List<CommentDetail> mCommentDetails;

    public Integer getStatus() {
        return mStatus;
    }

    public void setStatus(Integer status) {
        mStatus = status;
    }

    public Integer getCount() {
        return mCount;
    }

    public void setCount(Integer count) {
        mCount = count;
    }

    public List<CommentDetail> getCommentList() {
        return mCommentDetails;
    }

    public void setCommentList(List<CommentDetail> commentDetails) {
        mCommentDetails = commentDetails;
    }
}
