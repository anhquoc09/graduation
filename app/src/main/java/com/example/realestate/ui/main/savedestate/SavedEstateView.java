package com.example.realestate.ui.main.savedestate;

import com.example.realestate.data.model.SavedProject;

import java.util.List;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public interface SavedEstateView {
    void fetchDataSuccess(List<SavedProject> list);

    void showProgress();

    void hideProgress();

    void hideNoNetwork();

    void showNoNetwork();

    void unSaveEstateSuccess(int position);
}
