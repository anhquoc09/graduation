package com.example.realestate.ui.main.home;

import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.rest.UserService;
import com.example.realestate.ui.BasePresenter;

import rx.subscriptions.CompositeSubscription;

public class HomePagerPresenter extends BasePresenter<HomePagerView> {

    public static final String TAG = HomePagerPresenter.class.getSimpleName();

    private UserService mService;

    private CompositeSubscription mSubscription = new CompositeSubscription();

    public HomePagerPresenter() {
        mService = ServiceProvider.getUserService();
    }

    public void fetchData() {
        mView.fetchDataSuccess(null);
    }
}
