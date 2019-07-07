package com.reales.realestate.ui.submit;

import android.net.Uri;

import com.cloudinary.android.callback.ErrorInfo;
import com.reales.realestate.data.model.EstateDetail;

import java.util.List;

public interface SubmitPostView {
    void addImage(Uri imageUri);

    void setImageList(List<Uri> imageListUri);

    void onSubmitSuccess(EstateDetail estateDetail);

    void onSubmitFailed(String message);

    void uploadError(ErrorInfo error, int position);

    void startUploadImage(int position);

    void uploadImageSuccess(int position);

    void updateUploadPercent(int percent, int position);

    void showNoNetwork();

    void hideNoNetwork();
}
