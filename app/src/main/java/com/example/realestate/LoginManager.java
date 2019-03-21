package com.example.realestate;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;

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

    private static final int LOGIN_GG_REQUEST_CODE = 9001;

    private Resources mResources;

    private FragmentActivity mActivity;

    private GoogleApiClient mGoogleApiClient;

    private LoginResultCallback mResultCallback;

    private final GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            onError(mResources.getString(R.string.google_connection_error) + " "
                    + connectionResult.getErrorMessage());
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
                    onSuccess(idToken);
                } else {
                    onError(mResources.getString(R.string.message_login_fail, mResources.getString(R.string.google_plus), "NoTK"));
                }
            } else {
                onError(mResources.getString(R.string.message_login_fail, mResources.getString(R.string.google_plus), "NoAcc"));
            }
        } else {

            if (NetworkUtils.isNetworkConnected(EstateApplication.getInstance())) {
                onError(mResources.getString(R.string.message_login_fail, mResources.getString(R.string.google_plus), String.valueOf(result.getStatus().getStatusCode())));
            } else {
                onError(mResources.getString(R.string.no_network_connection));
            }
        }
    }

    private void onError(String errorMessage) {
        if (mResultCallback != null) {
            mResultCallback.onLoginError(errorMessage);
        }
    }

    private void onSuccess(String token) {
        if (mResultCallback != null) {
            mResultCallback.onLoginSuccess(token);
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
            onError(mResources.getString(R.string.google_connection_error));
            return;
        }

        Intent ggSignInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        if (null != ggSignInIntent.resolveActivity(mActivity.getPackageManager())) {
            mActivity.startActivityForResult(ggSignInIntent, LOGIN_GG_REQUEST_CODE);
        } else {
            onError(mResources.getString(R.string.google_connection_error));
        }
    }

    public boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

        if (LOGIN_GG_REQUEST_CODE == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                handleGGSignInResult(Auth.GoogleSignInApi.getSignInResultFromIntent(data));
            } else if (Activity.RESULT_CANCELED == resultCode) {
                onError(mResources.getString(R.string.cancel_sign_in_gg));
            }
            return true;
        }

        return false;
    }

    public void cleanUp() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
            mGoogleApiClient.stopAutoManage(mActivity);
            mGoogleApiClient = null;
        }
    }

    /**
     * {@link LoginResultCallback}
     */
    public interface LoginResultCallback {
        void onLoginError(String errorMessage);

        void onLoginSuccess(String accessToken);
    }
}
