package com.example.realestate;

import android.content.res.Resources;
import android.util.Log;

import com.example.realestate.utils.AndroidUtilities;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

/**
 * @author anhquoc09
 * @since 20/03/2019
 */

public class LogoutManager {
    private GoogleApiClient mGoogleApiClient;

    private FragmentActivity mActivity;

    private Resources mResources;

    private final GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            AndroidUtilities.showToast(mResources.getString(R.string.google_connection_error));
        }
    };

    public LogoutManager(FragmentActivity activity) {
        mActivity = activity;
        mResources = activity.getResources();
    }

    public void init() {
        initGoogleClient();
    }

    public void logOut() {
        UserManager.clearSession();
        logoutGoogle();
    }

    private void logoutGoogle() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    Log.e(this.getClass().getSimpleName(), "<-- Logout Google Success");
                }
            });
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

    public void cleanUp() {
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }
            mGoogleApiClient = null;
        }
    }
}