package com.example.realestate.ui.main.uppost;

import com.example.realestate.ui.BasePresenter;

class UpPostPresenter extends BasePresenter<UpPostView> {
    public void upPost() {
        mView.onPostSuccess();
    }
}
