package com.example.realestate.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realestate.EstateApplication;
import com.example.realestate.GoogleManager;
import com.example.realestate.R;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.ui.login.LoginActivity;
import com.example.realestate.ui.profile.ProfileActivity;
import com.example.realestate.ui.submit.SubmitPostActivity;
import com.example.realestate.ui.widget.MainTabLayout;
import com.example.realestate.utils.AndroidUtilities;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class MainActivity extends BaseActivity
        implements MainView,
        MainTabLayout.OnTabSelectListener,
        GoogleManager.LogoutResultCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.coordination_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.main_tabs_layout)
    MainTabLayout mTabLayout;

    @BindView(R.id.main_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.navigation_layout)
    View mNavigationLayout;

    @BindView(R.id.toolbar_title)
    TextView mTabTitle;

    @BindView(R.id.profile)
    View mProfileView;

    @BindView(R.id.img_avatar)
    ImageView mProfileAvatar;

    @BindView(R.id.tv_name)
    TextView mProfileName;

    @BindView(R.id.tv_app_name)
    View mAppName;

    @BindView(R.id.btn_log_out)
    TextView mLogOutBtn;

    @BindView(R.id.btn_log_in)
    TextView mLoginBtn;

    @BindView(R.id.progress_layout)
    View mProgress;

    private MainPagerAdapter mAdapter;

    private MainPresenter mPresenter;

    private Unbinder mUnbinder;

    private GoogleManager mGoogleManager;

    private Snackbar mSnackBar;

    private long mPressedTimeMillis;

    public static Intent intentFor(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

        setupTabLayout();

        setupNavigationLayout();

        initPresenter();

        initGoogle();

        mSnackBar = Snackbar
                .make(mCoordinatorLayout, getString(R.string.no_network_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.setting), view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));

        if (UserManager.isUserLoggedIn()) {
            mPresenter.fetchData();
        } else {
            initAdapter();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupView();
    }

    private void setupView() {
        if (UserManager.isUserLoggedIn()) {
            showProfileView();
            hideBtnLogIn();
            showBtnLogOut();
        } else {
            hideProfileView();
            showBtnLogIn();
            hideBtnLogOut();
        }
    }

    private void hideBtnLogIn() {
        mLoginBtn.setVisibility(View.GONE);
    }

    private void showBtnLogOut() {
        mLogOutBtn.setVisibility(View.VISIBLE);
    }

    private void showBtnLogIn() {
        mLoginBtn.setVisibility(View.VISIBLE);
    }

    private void hideBtnLogOut() {
        mLogOutBtn.setVisibility(View.GONE);
    }

    private void setupNavigationLayout() {
        ActionBarDrawerToggle drawerListener = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.addDrawerListener(drawerListener);
    }

    private void initAdapter() {
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), mTabLayout);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mAdapter.getCount() - 1);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setSelected(position, false);
                mTabTitle.setText(mTabLayout.getTabTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(mAdapter.getHomeIndex());
        setToolbarTitle(mViewPager.getCurrentItem());
    }

    private void setupTabLayout() {
        mTabLayout.setTabSelectListener(this);
    }

    private void setToolbarTitle(int position) {
        mTabTitle.setText(mAdapter.getItemTitle(position));
    }

    private void initPresenter() {
        mPresenter = new MainPresenter();
        mPresenter.attachView(this);
    }

    private void initGoogle() {
        mGoogleManager = new GoogleManager();
        mGoogleManager.init();
        mGoogleManager.setLogoutManagert(this);
    }


    private void hideProfileView() {
        if (mProfileView != null) {
            mLogOutBtn.setVisibility(View.GONE);
            mProfileView.setVisibility(View.GONE);
        }
        if (mAppName != null) {
            mAppName.setVisibility(View.VISIBLE);
        }
    }

    private void showProfileView() {
        if (mProfileView != null) {
            mLogOutBtn.setVisibility(View.VISIBLE);
            mProfileView.setVisibility(View.VISIBLE);
            mProfileName.setText(UserManager.getCurrentUser().getFullname());
            Glide.with(this)
                    .load(UserManager.getCurrentUser().getAvatar())
                    .placeholder(R.drawable.avatar_default_small)
                    .into(mProfileAvatar);
        }
        if (mAppName != null) {
            mAppName.setVisibility(View.GONE);
        }
    }

    private void goToLoginScreen() {
        Intent intent = LoginActivity.intentFor(this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();

        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - mPressedTimeMillis <= 2000) {
                super.onBackPressed();
            } else {
                AndroidUtilities.showToast(getString(R.string.pressed_back_to_exit));
                mPressedTimeMillis = currentTimeMillis;
            }
        }
    }

    @Override
    public void onTabSelect(int tabIndex) {
        mViewPager.setCurrentItem(tabIndex, true);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void logoutFinished() {
        if (mGoogleManager != null) {
            mGoogleManager.logoutGoogle();
        }
    }

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void hideNoNetwork() {
        mSnackBar.dismiss();
    }

    @Override
    public void showNoNetwork() {
        mSnackBar.show();
    }

    @Override
    public void fetchSavedListSuccess() {
        initAdapter();
    }

    @Override
    public void onLogoutSuccess() {
        EstateApplication.clearSavedList();
        goToLoginScreen();
    }

    @OnClick(R.id.btn_support)
    public void onSupportClick() {
        AndroidUtilities.showToast("Support Clicked!");
    }

    @OnClick(R.id.btn_about_us)
    public void onAboutUsClick() {
        AndroidUtilities.showToast("We are Supper Heroes!");
    }

    @OnClick(R.id.btn_log_out)
    public void onLogOutClick() {
        mPresenter.logout();
    }

    @OnClick(R.id.btn_log_in)
    public void onLogInClick() {
        Intent intent = LoginActivity.intentFor(this);
        startActivity(intent);
    }

    @OnClick(R.id.btn_open_navigation)
    public void onOpenNavigationIconClick() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @OnClick(R.id.btn_close_navigation)
    public void onCloseNavigationIconClick() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @OnClick(R.id.profile)
    public void onProfileClick() {
        User user = UserManager.getCurrentUser();
        startActivity(ProfileActivity.intentFor(this, String.valueOf(user.getId())));
    }

    @OnClick(R.id.btn_submit)
    public void onUpPostClick() {
        if (UserManager.isUserLoggedIn()) {
            startActivity(SubmitPostActivity.intentFor(this));
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle(getString(R.string.not_login));
            alertDialog.setMessage(getString(R.string.login_now));
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.button_cancel), (dialog, which) -> alertDialog.dismiss());
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.login), (dialog, which) -> startActivity(LoginActivity.intentFor(this, true)));
            alertDialog.setCancelable(true);
            alertDialog.show();
        }
    }
}
