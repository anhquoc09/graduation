package com.example.realestate.ui.profile;

import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.data.model.Profile;

import java.util.List;

public interface ProfileView {
    void onFetchProfileSuccess(Profile profile);

    void onFetchListEstateSuccess(List<EstateDetail> data);

    void onLoadMoreListSuccess(List<EstateDetail> data);

    void editProfileSuccess();

    void deleteProfileSuccess(int position);

    void hideListProgress();

    void showListProgress();

    void hideNoNetwork();

    void showNoNetwork();

    void showProgress();

    void hideProgress();
}
