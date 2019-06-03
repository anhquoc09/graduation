package com.example.realestate.ui.main.uppost;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.realestate.ui.BasePresenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class UpPostPresenter extends BasePresenter<UpPostView> {

    private List<Uri> mUriList = new ArrayList<>();

    private List<String> mUrlList = new ArrayList<>();

    private List<String> mRequestIdList = new ArrayList<>();

    public void upPost() {
        for (int i = 0; i < mUriList.size(); i++) {
            int finalI = i;
            MediaManager.get()
                    .upload(mUriList.get(finalI))
                    .unsigned("dels6a22")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            startUploadImage(finalI);
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {
                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {
                            uploadSuccess(requestId, resultData, finalI);
                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            uploadError(requestId, error, finalI);
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {
                        }
                    })
                    .dispatch();
        }
    }

    private void uploadError(String requestId, ErrorInfo error, int position) {
        Log.d("haq", "Upload Error" + error.toString());
        if (isViewAttached()) {
            mView.uploadError(error, position);
        }
    }

    private void uploadSuccess(String requestId, Map resultData, int position) {
        Log.d("haq", "upload Success");
        if (isViewAttached()) {
            mRequestIdList.add(requestId);
            mView.uploadImageSuccess(position);
        }
    }

    private void updateUploadPercent(long percent, int position) {
        if (isViewAttached()) {
            mView.updateUploadPercent((int)percent, position);
        }
    }

    private void startUploadImage(int position) {
        Log.d("haq", "start Upload");
        if (isViewAttached()) {
            mView.startUploadImage(position);
        }
    }

    public void pickImageResultOK(Intent data) {
        if (data.getClipData() != null) {
            ClipData clipData = data.getClipData();
            for (int i = 0; i < clipData.getItemCount(); i++) {

                Uri uri = clipData.getItemAt(i).getUri();
                mUriList.add(uri);
                mView.setImageList(mUriList);
            }
        } else if (data.getData() != null) {
            Uri imageUri = data.getData();
            mUriList.add(imageUri);
            mView.addImage(imageUri);
        }
    }

    public void deleteImage(int position) {
        if (position > 0 && position < mUriList.size()) {
            mUriList.remove(position);
        }
    }
}
