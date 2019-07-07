package com.reales.realestate.ui.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.reales.realestate.EstateApplication;
import com.reales.realestate.R;
import com.reales.realestate.User;
import com.reales.realestate.UserManager;
import com.reales.realestate.data.model.Profile;
import com.reales.realestate.data.remote.ServiceProvider;
import com.reales.realestate.data.remote.response.LoginResponse;
import com.reales.realestate.data.remote.rest.EstateService;
import com.reales.realestate.data.remote.rest.SchedulerProvider;
import com.reales.realestate.ui.BasePresenter;
import com.reales.realestate.utils.AndroidUtilities;
import com.reales.realestate.utils.NetworkUtils;

import java.net.SocketTimeoutException;

import rx.Subscriber;
import rx.Subscription;
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

    public void login(String idToken) {
        if (!canLogin(idToken)) {
            if (isViewAttached()) {
                mView.showNoNetworkConnection();
            }
        }
        showLoginProgress();
        Subscription subscription = mEstateService.loginGoogle(idToken)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new LoginSubscriber());
        mSubscriptions.add(subscription);
    }

    private boolean canLogin(String accessToken) {
        return !TextUtils.isEmpty(accessToken) && NetworkUtils.isNetworkConnected(mContext);
    }

    private void showLoginProgress() {
        mView.showLoginProgress();
    }

    private void hideLoginProgress() {
        mView.hideLoginProgress();
    }

    private void showLoginError(String string) {
        if (isViewAttached()) {
            mView.showLoginError(string);
        }
    }

    private void onLoginSuccess(Profile profile, Long expireTime, String accessToken) {
        User user = new User(profile);
        user.setTokenExpiredTime(expireTime * 1000 - 60000);
        user.setAccessToken(accessToken);
        UserManager.setCurrentUser(user);
        mView.onLoginSuccess();
    }

    private class LoginSubscriber extends Subscriber<LoginResponse> {

        @Override
        public void onNext(LoginResponse loginResponse) {
            hideLoginProgress();

            if (loginResponse != null) {
                Profile profile = loginResponse.getProfile();
                if (profile == null || loginResponse.getToken().isEmpty()) {
                    Log.e(TAG, loginResponse.getMessage());
                    showLoginError(mContext.getString(R.string.login_fail));
                } else {
                    onLoginSuccess(profile, loginResponse.getExpireTime(), loginResponse.getToken());
                }
            }
        }

        @Override
        public void onCompleted() {

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
