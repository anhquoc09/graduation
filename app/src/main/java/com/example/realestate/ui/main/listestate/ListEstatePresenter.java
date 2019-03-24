package com.example.realestate.ui.main.listestate;

import com.example.realestate.ui.BasePresenter;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class ListEstatePresenter extends BasePresenter<ListEstateView> {
    public static final String TAG = ListEstatePresenter.class.getSimpleName();

    public void fetchData() {
        mView.fetchDataSuccess(null);
    }
}
