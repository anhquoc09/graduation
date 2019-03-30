package com.example.realestate.ui.main.savedestate;

import com.example.realestate.ui.BasePresenter;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public class SavedEstatePresenter extends BasePresenter<SavedEstateView> {

    public void fetchData() {
        mView.fetchDataSuccess(null);
    }
}
