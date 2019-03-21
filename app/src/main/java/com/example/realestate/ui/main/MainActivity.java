package com.example.realestate.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.realestate.LogoutManager;
import com.example.realestate.R;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.ui.login.LoginActivity;
import com.example.realestate.ui.widget.MainTabLayout;
import com.example.realestate.utils.AndroidUtilities;

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

public class MainActivity extends BaseActivity implements MainView, MainTabLayout.OnTabSelectListener {

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

    private MainPagerAdapter mAdapter;

    private MainPresenter mPresenter;

    private ActionBarDrawerToggle mDrawerListener;

    private Unbinder mUnbinder;

    private LogoutManager mLogoutManager;

    public static Intent intentFor(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);

        initAdapter();

        setupTabLayout();

        setupNavigationLayout();

        setToolbarTitle(mViewPager.getCurrentItem());

        initPresenter();
    }

    private void setupNavigationLayout() {
        mDrawerListener = new ActionBarDrawerToggle(this, mDrawerLayout, null, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerListener);
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
        mLogoutManager = new LogoutManager(this);
        mLogoutManager.init();

        mPresenter = new MainPresenter(mLogoutManager);
        mPresenter.attachView(this);
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
    public void goToLoginScreen() {
        Intent intent = LoginActivity.intentFor(this);
        startActivity(intent);
        finish();
    }

    @Override
    public void onTabSelect(int tabIndex) {
        mViewPager.setCurrentItem(tabIndex, true);
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
        mPresenter.signOut();
    }

    @OnClick(R.id.btn_open_navigation)
    public void onOpenNavigationIconClick() {
        if (!mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @OnClick(R.id.btn_open_navigation)
    public void onCloseNavigationIconClick() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }
}
