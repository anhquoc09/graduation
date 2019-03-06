package com.example.realestate.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.realestate.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public class LoginActivity extends AppCompatActivity implements LoginView{

    public static final String TAG = LoginActivity.class.getSimpleName();

    private LoginPresenter mPresenter;

    public static Intent intenfor(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPresenter = new LoginPresenter();
        mPresenter.attachView(this);
    }

    @OnClick(R.id.btn_login_google)
    public void loginGoogle() {
        mPresenter.loginGoogle();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        super.onDestroy();
    }
}
