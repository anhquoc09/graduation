package com.reales.realestate.ui.submit;

import android.net.Uri;

public class ImageInfoItem {
    private Uri mUri;

    private int mUploadPercent = 0;

    private boolean mIsUploading = false;

    private boolean mIsUploadSuccess = false;

    private boolean mIsUploadError = false;

    public ImageInfoItem(Uri uri) {
        mUri = uri;
    }

    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    public int getUploadPercent() {
        return mUploadPercent;
    }

    public void setUploadPercent(int uploadPercent) {
        mUploadPercent = uploadPercent;
    }

    public boolean isUploading() {
        return mIsUploading;
    }

    public void setUploading(boolean uploading) {
        mIsUploading = uploading;
    }

    public boolean isUploadSuccess() {
        return mIsUploadSuccess;
    }

    public void setUploadSuccess(boolean uploadSuccess) {
        mIsUploadSuccess = uploadSuccess;
    }

    public boolean isUploadError() {
        return mIsUploadError;
    }

    public void setUploadError(boolean uploadError) {
        mIsUploadError = uploadError;
    }
}
