package com.reales.realestate.data.remote.response;

import com.reales.realestate.data.model.CommentResponseDetail;
import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 06/07/2019
 */
public class CommentResponse {
    @SerializedName("status")
    private Integer mStatus;

    @SerializedName("message")
    private String mMessage;

    @SerializedName("comment")
    private CommentResponseDetail mComment;

    public Integer getStatus() {
        return mStatus;
    }

    public void setStatus(Integer status) {
        mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public CommentResponseDetail getComment() {
        return mComment;
    }

    public void setComment(CommentResponseDetail comment) {
        mComment = comment;
    }
}
