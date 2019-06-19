package com.example.realestate.ui.main.savedlist;

import com.example.realestate.data.model.SavedPost;

import java.util.List;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public interface SavedListView {
    void fetchDataSuccess(List<SavedPost> list);

    void showProgress();

    void hideProgress();

    void hideNoNetwork();

    void showNoNetwork();

    void unSaveEstateSuccess(int position);
}
