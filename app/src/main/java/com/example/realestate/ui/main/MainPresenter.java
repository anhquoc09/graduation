package com.example.realestate.ui.main;

import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.rest.UserService;
import com.example.realestate.ui.BasePresenter;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public class MainPresenter extends BasePresenter<MainView> {
    public static final String TAG = MainPresenter.class.getSimpleName();

    private UserService mUserService;

    private CompositeSubscription mSubscription = new CompositeSubscription();

    public MainPresenter() {
        mUserService = ServiceProvider.getUserService();
    }

    public void logout() {
        logoutFinished();
    }

    private void logoutFinished() {
        if (mView != null) {
            mView.logoutFinished();
        }
    }

    @Override
    public void detachView() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        super.detachView();
    }
}
