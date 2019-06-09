package com.example.realestate.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realestate.GoogleManager;
import com.example.realestate.R;
import com.example.realestate.User;
import com.example.realestate.UserManager;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.ui.login.LoginActivity;
import com.example.realestate.ui.main.profile.ProfileActivity;
import com.example.realestate.ui.main.uppost.UpPostActivity;
import com.example.realestate.ui.widget.MainTabLayout;
import com.example.realestate.utils.AndroidUtilities;
import com.example.realestate.utils.PermissionUtils;

import androidx.appcompat.app.ActionBarDrawerToggle;
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

    @BindView(R.id.btn_sign_out)
    TextView mSignOutBtn;

    private MainPagerAdapter mAdapter;

    private MainPresenter mPresenter;

    private Unbinder mUnbinder;

    private GoogleManager mGoogleManager;

    public static Intent intentFor(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        PermissionUtils.Request_FINE_LOCATION(this, 1);
        PermissionUtils.Request_COARSE_LOCATION(this, 2);

        initAdapter();

        setupTabLayout();

        setupNavigationLayout();

        setToolbarTitle(mViewPager.getCurrentItem());

        initPresenter();

        initGoogle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupProfileView();
    }

    private void setupProfileView() {
        if (UserManager.isUserLoggedIn()) {
            showProfileView();
        } else {
            hideProfileView();
        }
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
            mSignOutBtn.setVisibility(View.GONE);
            mProfileView.setVisibility(View.GONE);
        }
        if (mAppName != null) {
            mAppName.setVisibility(View.VISIBLE);
        }
    }

    private void showProfileView() {
        if (mProfileView != null) {
            mSignOutBtn.setVisibility(View.VISIBLE);
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
            super.onBackPressed();
        }
    }

    @Override
    public void onTabSelect(int tabIndex) {
        mViewPager.setCurrentItem(tabIndex, true);
    }

    @Override
    public void logoutFinished() {
        if (mGoogleManager != null) {
            mGoogleManager.logoutGoogle();
        }
    }

    @Override
    public void onLogoutSuccess() {
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

    @OnClick(R.id.btn_sign_out)
    public void onSignOutClick() {
        mPresenter.logout();
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
            startActivity(UpPostActivity.intentFor(this));
        } else {
            startActivity(LoginActivity.intentFor(this));
        }
    }
}
