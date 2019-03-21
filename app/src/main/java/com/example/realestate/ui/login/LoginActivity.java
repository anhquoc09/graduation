package com.example.realestate.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.realestate.LoginManager;
import com.example.realestate.R;
import com.example.realestate.ui.main.MainActivity;
import com.example.realestate.utils.AndroidUtilities;
import com.example.realestate.utils.NetworkUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class LoginActivity extends AppCompatActivity implements LoginView {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.tv_go_home)
    TextView mTvGoHome;

    private Unbinder mUnbinder;

    private LoginPresenter mPresenter;

    private LoginManager mLoginManager;

    private LoginManager.LoginResultCallback mResultCallback = new LoginManager.LoginResultCallback() {
        @Override
        public void onLoginError(String errorMessage) {
            AndroidUtilities.showToast(errorMessage);
        }

        @Override
        public void onLoginSuccess(String accessToken) {
            if (mPresenter != null) {
                mPresenter.login(accessToken);
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

        if (mLoginManager != null) {
            mLoginManager.cleanUp();
            mLoginManager = null;
        }

        super.onDestroy();
    }

    @Override
    public void showNoNetworkConnection() {
        AndroidUtilities.showToast(getString(R.string.no_network_connection));
    }

    @Override
    public void onLoginSuccess() {
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
        mTvGoHome.setClickable(false);
    }

    private void goToHome() {
        startActivity(MainActivity.intentFor(this));
        finish();
    }
}
