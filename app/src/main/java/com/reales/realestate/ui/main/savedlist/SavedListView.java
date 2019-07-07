package com.reales.realestate.ui.main.savedlist;

import com.reales.realestate.data.model.SavedPost;

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
