package com.example.realestate.ui.login;

import android.content.Context;
import android.text.TextUtils;

import com.example.realestate.EstateApplication;
import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.LoginData;
import com.example.realestate.data.remote.ServiceProvider;
import com.example.realestate.data.remote.response.LoginResponse;
import com.example.realestate.data.remote.rest.EstateService;
import com.example.realestate.ui.BasePresenter;
import com.example.realestate.utils.AndroidUtilities;
import com.example.realestate.utils.NetworkUtils;
import com.example.realestate.utils.SimpleSubscriber;

import java.net.SocketTimeoutException;

import rx.subscriptions.CompositeSubscription;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class LoginPresenter extends BasePresenter<LoginView> {

    public static final String TAG = LoginPresenter.class.getSimpleName();

    private Context mContext;

    private EstateService mEstateService;

    private final CompositeSubscription mSubscriptions = new CompositeSubscription();

    public LoginPresenter() {
        mContext = EstateApplication.getInstance().getApplicationContext();
        mEstateService = ServiceProvider.getEstateService();
    }

    public void login(String accessToken) {
        if (!canLogin(accessToken)) {
            if (isViewAttached()) {
                mView.showNoNetworkConnection();
            }
        }

        loginGoogle(accessToken);
    }

    private boolean canLogin(String accessToken) {
        return !TextUtils.isEmpty(accessToken) && NetworkUtils.isNetworkConnected(mContext);
    }


    private void loginGoogle(String accessToken) {
        UserManager.setGoogleToken(accessToken);
        mView.onLoginSuccess();
//        showLoadingProgress();
//        Subscription subscription = mEstateService.loginGoogle(accessToken,
//                null,
//                null, AndroidUtilities.currentOsVersion())
//                .subscribeOn(SchedulerProvider.io())
//                .observeOn(SchedulerProvider.ui())
//                .subscribe(new LoginSubscriber());
//        mSubscriptions.add(subscription);
    }

    private void showLoadingProgress() {
    }


    private void hideLoginProgress() {
    }

    private void showLoginError(String string) {
        if (isViewAttached()) {
            mView.showLoginError(string);
        }
    }

    private class LoginSubscriber extends SimpleSubscriber<LoginResponse> {

        @Override
        public void onNext(LoginResponse loginResponse) {
            hideLoginProgress();
            if (loginResponse.isSuccessful()) {
                LoginData loginData = loginResponse.getData();
                if (loginData == null || TextUtils.isEmpty(loginData.getAccessToken())) {
                    showLoginError(mContext.getString(R.string.login_fail));
                } else {
                    mView.onLoginSuccess();
                }
            } else {
                showLoginError(loginResponse.getMessage());
            }
        }

        @Override
        public void onError(Throwable e) {
            hideLoginProgress();
            if (e instanceof SocketTimeoutException) {
                AndroidUtilities.showToast(mContext.getString(R.string.slow_network_connection));
            } else {
                AndroidUtilities.showToast(mContext.getString(R.string.message_cannot_connect_server));
            }
        }
    }
}
