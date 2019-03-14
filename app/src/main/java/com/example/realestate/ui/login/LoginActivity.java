package com.example.realestate.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.ui.main.MainActivity;
import com.example.realestate.utils.AndroidUtilities;
import com.example.realestate.utils.NetworkUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class LoginActivity extends AppCompatActivity implements LoginView {

    public static final String TAG = LoginActivity.class.getSimpleName();

    private Unbinder mUnbinder;

    private LoginPresenter mPresenter;

    private LoginManager mLoginManager;

    private LoginManager.LoginResultCallback mResultCallback = new LoginManager.LoginResultCallback() {
        @Override
        public void onLoginError(String errorMessage, int loginMethod) {
            AndroidUtilities.showToast(errorMessage);
        }

        @Override
        public void onLoginSuccess(String accessToken, int loginMethod) {
            if (mPresenter != null) {
                mPresenter.login(accessToken, loginMethod);
            }
        }
    };

    public static Intent intentFor(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUnbinder = ButterKnife.bind(this);

        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);

        mLoginManager = new LoginManager(this, mResultCallback);
        mLoginManager.init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mLoginManager != null && mLoginManager.onActivityResult(this, requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        super.onDestroy();
    }

    @Override
    public void showNoNetworkConnection() {
        AndroidUtilities.showToast(getString(R.string.no_network_connection));
    }

    @Override
    public void onLoginSuccess() {
        AndroidUtilities.showToast("Login success, GoogleToken: " + UserManager.getGoogleToken());
        goToHome();
    }

    @Override
    public void showLoginError(String message) {
        AndroidUtilities.showToast(message);
    }

    @OnClick(R.id.btn_login_google)
    public void loginGoogle() {
        if (!NetworkUtils.isNetworkConnected(this)) {
            showNoNetworkConnection();
            return;
        }

        if (mLoginManager != null) {
            mLoginManager.loginGoogle();
        }
    }

    @OnClick(R.id.tv_go_home)
    public void goHome() {
        goToHome();
    }

    private void goToHome() {
        startActivity(MainActivity.intentFor(this));
    }
}
