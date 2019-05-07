package com.example.realestate.ui.main.uppost;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;

import com.example.realestate.ui.BasePresenter;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

class UpPostPresenter extends BasePresenter<UpPostView> {

    StorageReference filepath = FirebaseStorage.getInstance().getReference().child("gpic");

    private List<Uri> mUriList = new ArrayList<>();

    public void upPost() {
        mView.onPostSuccess();
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
