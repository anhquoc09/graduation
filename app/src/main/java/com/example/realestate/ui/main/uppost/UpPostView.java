package com.example.realestate.ui.main.uppost;

import android.net.Uri;

import java.util.List;

public interface UpPostView {
    void addImage(Uri imageUri);

    void setImageList(List<Uri> imageListUri);

    void onPostSuccess();

    void onPostFailed(String message);
}
