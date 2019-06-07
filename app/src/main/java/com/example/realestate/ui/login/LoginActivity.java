package com.example.realestate.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.realestate.GoogleManager;
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

public class LoginActivity extends AppCompatActivity implements LoginView, GoogleManager.LoginResultCallback {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.tv_go_home)
    TextView mTvGoHome;

    private Unbinder mUnbinder;

    private GoogleManager mGoogleManager;

    private ProgressDialog mProgressDialog;

    private LoginPresenter mPresenter;

    public static Intent intentFor(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUnbinder = ButterKnife.bind(this);

        initProgressDialog();
        initPresenter();
        initGoogle();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        super.onDestroy();
    }

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.login);
    }

    private void initPresenter() {
        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
    }

    private void initGoogle() {
        mGoogleManager = new GoogleManager();
        mGoogleManager.init();
        mGoogleManager.setLoginCallback(this);
    }

    private void goToHome() {
        startActivity(MainActivity.intentFor(this));
        finish();
    }

    private void showProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (mGoogleManager != null && mGoogleManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

//    GoogleManager Callback
    @Override
    public void startForResult(Intent signInIntent, int loginGgRequestCode) {
        startActivityForResult(signInIntent, loginGgRequestCode);
    }

    @Override
    public void onLoginError(String errorMessage) {
        AndroidUtilities.showToast(errorMessage);
    }

    @Override
    public void onLoginSuccess(String idToken) {
        if (mPresenter != null) {
            mPresenter.login(idToken);
        }
    }

//    Presenter callback
    @Override
    public void showNoNetworkConnection() {
        AndroidUtilities.showToast(getString(R.string.no_network_connection));
    }

    @Override
    public void hideLoginProgress() {
        hideProgressDialog();
    }

    @Override
    public void showLoginProgress() {
        showProgressDialog();
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

        if (mGoogleManager != null) {
            mGoogleManager.loginGoogle();
        }
    }

    @OnClick(R.id.tv_go_home)
    public void goHome() {
        goToHome();
        mTvGoHome.setClickable(false);
    }
}
