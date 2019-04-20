package com.example.realestate.ui.main.uppost;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;

import com.example.realestate.R;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.utils.AndroidUtilities;

import java.util.Locale;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class UpPostActivity extends BaseActivity implements UpPostView {

    private Unbinder mUnbinder;

    private UpPostPresenter mPresenter;

    private Geocoder mGeocoder;

    public static Intent intentFor(Context context) {
        Intent intent = new Intent(context, UpPostActivity.class);
        intent.putExtras(new Bundle());
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_post);
        mUnbinder = ButterKnife.bind(this);
        initView();
        initPresenter();
        initGeoCoder();
    }

    private void initGeoCoder() {
        mGeocoder = new Geocoder(this, Locale.getDefault());
    }

    private void initView() {

    }

    private void initPresenter() {
        mPresenter = new UpPostPresenter();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onPostSuccess() {
        AndroidUtilities.showToast("Your post is upload success!");
    }
}
