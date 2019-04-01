package com.example.realestate.ui.main.listestate;

import com.example.realestate.ui.BasePresenter;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class ListNewEstatePresenter extends BasePresenter<ListNewEstateView> {
    public static final String TAG = ListNewEstatePresenter.class.getSimpleName();

    public void fetchData() {
        mView.fetchDataSuccess(null);
    }
}
