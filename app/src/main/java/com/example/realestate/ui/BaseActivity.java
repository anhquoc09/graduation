package com.example.realestate.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.realestate.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author anhquoc09
 * @since 10/03/2019
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogLoading();
    }

    private void initDialogLoading() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle(R.string.loading);
        mProgressDialog.setCanceledOnTouchOutside(false);
    }

    protected void showDialogLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    protected void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
