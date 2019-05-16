package com.example.realestate.ui.main;

import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.ui.BasePresenter;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public class MainPresenter extends BasePresenter<MainView> {
    public static final String TAG = MainPresenter.class.getSimpleName();

    private EstateService mEstateService;

    private CompositeSubscription mSubscription = new CompositeSubscription();

    public MainPresenter() {
        mEstateService = ServiceProvider.getEstateService();
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
