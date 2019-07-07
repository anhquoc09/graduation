package com.reales.realestate.ui.estatedetail;

import com.reales.realestate.data.model.CommentDetail;
import com.reales.realestate.data.model.CommentResponseDetail;

import java.util.List;

public interface EstateDetailView {

    void setComment(List<CommentDetail> commentList);

    void saveEstateSuccess();

    void unSaveEstateSuccess();

    void showNoNetworkConnection();

    void hideNoNetworkConnection();

    void addCommentSuccess(CommentResponseDetail detail);

    void addCommentError();

    void editCommentSuccess(int position, CommentResponseDetail detail);

    void editCommentError(int position);

    void deleteCommentSuccess(int position);

    void deleteCommentError(int position);
}
