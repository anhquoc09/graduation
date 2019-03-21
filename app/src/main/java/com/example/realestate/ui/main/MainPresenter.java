package com.example.realestate.ui.main;

import com.example.realestate.LogoutManager;
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

    private LogoutManager mLogoutManager;

    private CompositeSubscription mSubscription = new CompositeSubscription();

    public MainPresenter(LogoutManager logoutManager) {
        mLogoutManager = logoutManager;
        mUserService = ServiceProvider.getUserService();
    }

    public void signOut() {
        signOutFinished();
        goToSignInScreen();
    }

    private void goToSignInScreen() {
        if (isViewAttached()) {
            mView.goToLoginScreen();
        }
    }

    private void signOutFinished() {
        if (mLogoutManager != null) {
            mLogoutManager.logOut();
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mLogoutManager != null) {
            mLogoutManager.cleanUp();
            mLogoutManager = null;
        }
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
