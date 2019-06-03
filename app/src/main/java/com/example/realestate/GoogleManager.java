package com.example.realestate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.realestate.data.model.Profile;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class GoogleManager {

    public static final String TAG = GoogleManager.class.getSimpleName();

    private static final int LOGIN_GG_REQUEST_CODE = 9001;

    private Context mContext;

    private GoogleSignInClient mClient;

    private LoginResultCallback mLoginCallback;

    private LogoutResultCallback mLogoutCallback;

    public GoogleManager() {
        mContext = EstateApplication.getInstance().getApplicationContext();
    }

    private void initGoogleClient() {
        if (mClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(mContext.getResources().getString(R.string.google_client_id))
                    .requestEmail()
                    .build();

            mClient = GoogleSignIn.getClient(mContext, gso);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account == null) {
                onError(mContext.getResources().getString(R.string.message_login_fail, "NoAcc"));
            } else {
                onSuccess(account.getIdToken());
            }
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            onError(mContext.getResources().getString(R.string.message_login_fail, "NoAcc"));
        }
    }

    private void logoutSuccess() {
        UserManager.clearSession();
        if (mLogoutCallback != null) {
            mLogoutCallback.onLogoutSuccess();
        }
    }

    private void onError(String errorMessage) {
        if (mLoginCallback != null) {
            mLoginCallback.onLoginError(errorMessage);
        }
    }

    private void onSuccess(String token) {
        if (mLoginCallback != null) {
            mLoginCallback.onLoginSuccess(token);
        }
    }

    public void setLoginCallback(LoginResultCallback loginCallback) {
        mLoginCallback = loginCallback;
    }

    public void setLogoutManagert(LogoutResultCallback logoutCallback) {
        mLogoutCallback = logoutCallback;
    }

    public void init() {
        initGoogleClient();
    }

    public void loginGoogle() {
        if (mClient == null) {
            onError(mContext.getResources().getString(R.string.google_connection_error));
            return;
        }

        mLoginCallback.startForResult(mClient.getSignInIntent(), LOGIN_GG_REQUEST_CODE);
    }

    public void logoutGoogle() {
        if (mClient != null) {
            mClient.signOut().addOnCompleteListener(task -> logoutSuccess());
        }
    }

    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {

        if (LOGIN_GG_REQUEST_CODE == requestCode) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
            return true;
        }

        return false;
    }

    /**
     * {@link LoginResultCallback}
     */
    public interface LoginResultCallback {
        void onLoginError(String errorMessage);

        void startForResult(Intent signInIntent, int loginGgRequestCode);

        void onLoginSuccess(String accessToken);
    }


    /**
     * {@link LogoutResultCallback}
     */
    public interface LogoutResultCallback {
        void onLogoutSuccess();
    }
}
