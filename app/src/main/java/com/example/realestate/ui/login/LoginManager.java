package com.example.realestate.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.example.realestate.EstateApplication;
import com.example.realestate.R;
import com.example.realestate.utils.NetworkUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class LoginManager {

    public static final String TAG = LoginManager.class.getSimpleName();

    public static final int LOGIN_NONE = 0;

    public static final int LOGIN_GOOGLE = 1;

    private static final int LOGIN_GG_REQUEST_CODE = 9001;

    private Resources mResources;

    private FragmentActivity mActivity;

    private GoogleApiClient mGoogleApiClient;

    private LoginResultCallback mResultCallback;

    private final GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            onError(mResources.getString(R.string.google_connection_error) + " "
                    + connectionResult.getErrorMessage(), LOGIN_GOOGLE);
        }
    };

    public LoginManager(FragmentActivity activity, LoginResultCallback resultCallback) {
        mActivity = activity;
        mResources = activity.getResources();
        mResultCallback = resultCallback;
    }

    private void handleGGSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount signInAccount = result.getSignInAccount();
            if (signInAccount != null) {
                String idToken = signInAccount.getIdToken();

                if (!TextUtils.isEmpty(idToken)) {
                    onSuccess(idToken, LOGIN_GOOGLE);
                } else {
                    onError(mResources.getString(R.string.message_login_fail, mResources.getString(R.string.google_plus), "NoTK"), LOGIN_GOOGLE);
                }
            } else {
                onError(mResources.getString(R.string.message_login_fail, mResources.getString(R.string.google_plus), "NoAcc"), LOGIN_GOOGLE);
            }
        } else {

            if (NetworkUtils.isNetworkConnected(EstateApplication.getInstance())) {
                onError(mResources.getString(R.string.message_login_fail, mResources.getString(R.string.google_plus), String.valueOf(result.getStatus().getStatusCode())), LOGIN_GOOGLE);
            } else {
                onError(mResources.getString(R.string.no_network_connection), LOGIN_GOOGLE);
            }
        }
    }

    private void onError(String errorMessage, int method) {
        if (mResultCallback != null) {
            mResultCallback.onLoginError(errorMessage, method);
        }
    }

    private void onSuccess(String token, int method) {
        if (mResultCallback != null) {
            mResultCallback.onLoginSuccess(token, method);
        }
    }

    private void initGoogleClient() {
        if (mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(mResources.getString(R.string.google_client_id))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .enableAutoManage(mActivity, 1, mOnConnectionFailedListener)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
    }

    public void init() {
        initGoogleClient();
    }

    public void loginGoogle() {
        if (mGoogleApiClient == null || !mGoogleApiClient.isConnected()) {
            onError(mResources.getString(R.string.google_connection_error), LOGIN_GOOGLE);
            return;
        }

        Intent ggSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        if (null != ggSignInIntent.resolveActivity(mActivity.getPackageManager())) {
            mActivity.startActivityForResult(ggSignInIntent, LOGIN_GG_REQUEST_CODE);
        } else {
            onError(mResources.getString(R.string.google_connection_error), LOGIN_GOOGLE);
        }
    }

    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

        if (LOGIN_GG_REQUEST_CODE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                handleGGSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
            } else if (Activity.RESULT_CANCELED == resultCode) {
                onError(mResources.getString(R.string.cancel_sign_in_gg), LOGIN_GOOGLE);
            }
            return true;
        }

        return false;
    }

    /**
     * {@link LoginResultCallback}
     */
    public interface LoginResultCallback {
        void onLoginError(String errorMessage, int loginMethod);

        void onLoginSuccess(String accessToken, int loginMethod);
    }
}
