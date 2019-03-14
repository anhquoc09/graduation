package com.example.realestate.ui.splash;

import android.os.Bundle;
import android.os.Handler;

import com.example.realestate.R;
import com.example.realestate.ui.login.LoginActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenView {

    private static final long SPLASH_SCREEN_DURATION = 500L;

    private SplashScreenPresenter mPresenter;

    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mPresenter = new SplashScreenPresenter();
        mPresenter.attachView(this);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    startActivity(LoginActivity.intentFor(SplashScreenActivity.this));
                    finish();
                }
            }
        }, SPLASH_SCREEN_DURATION);
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}