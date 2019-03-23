package com.example.realestate.ui.main.listestate;

import com.example.realestate.ui.BasePresenter;

public class ListEstatePresenter extends BasePresenter<ListEstateView> {
    public static final String TAG = ListEstatePresenter.class.getSimpleName();

    public void fetchData() {
        mView.fetchDataSuccess(null);
    }
}
