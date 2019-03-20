package com.example.realestate.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.ui.BaseActivity;
import com.google.android.material.tabs.TabLayout;

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

public class MainActivity extends BaseActivity implements MainView {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation_tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.home_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.navigation_layout)
    View mNavigationLayout;

    @BindView(R.id.toolbar_title)
    TextView mTabTitle;

    private MainPagerAdapter mAdapter;

    private MainPresenter mPresenter;

    private ActionBarDrawerToggle mDrawerListener;

    private Unbinder mUnbinder;

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
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mAdapter.getCount() - 1);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabTitle.setText(mAdapter.getItemTitle(mViewPager.getCurrentItem()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(mAdapter.getHomeIndex());
    }

    private void setupTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);

        int tabCount = mTabLayout.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tabAt = mTabLayout.getTabAt(i);

            if (tabAt != null) {
                tabAt.setCustomView(R.layout.navigation_tab_item);
                View v = tabAt.getCustomView();
                setItemIcon(v, i);
                setItemTitle(v, i);
            }
        }
    }

    private void setItemIcon(View view, int position) {
        if (view != null) {
            ImageView ic = view.findViewById(R.id.ic_navigation_item);

            if (ic != null) {
                ic.setImageDrawable(mAdapter.getItemIcon(position));
            }
        }
    }

    private void setItemTitle(View view, int position) {
        if (view != null) {
            TextView tv = view.findViewById(R.id.tv_navigation_item);

            if (tv != null) {
                tv.setText(mAdapter.getItemTitle(position));
            }
        }
    }

    private void setToolbarTitle(int position) {
        mTabTitle.setText(mAdapter.getItemTitle(position));
    }

    private void initPresenter() {
        mPresenter = new MainPresenter();
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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.btn_support)
    public void onSupportClick() {

    }

    @OnClick(R.id.btn_about_us)
    public void onAboutUsClick() {

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
