package com.example.realestate.ui.main.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.Profile;
import com.example.realestate.data.model.ProfileDetail;
import com.example.realestate.ui.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileActivity extends BaseActivity
        implements ProfileView,
        ProfileTabDetailFragment.OnCallBackListener,
        ProfileTabPostFragment.OnCallBackListener {

    public static final String TAG = ProfileActivity.class.getSimpleName();

    public static final String PROFILE = "profileId";

    @BindView(R.id.profile_avatar)
    ImageView mImageView;

    @BindView(R.id.profile_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.profile_tab_layout)
    TabLayout mTabLayout;

    private int mProfileId;

    private Profile mProfile;

    private ProfilePresenter mPresenter;

    private ProfilePagerAdapter mPagerAdapter;

    private Unbinder mUnbinder;

    public static Intent intentFor(Context context, String profileId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(PROFILE, profileId);

        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_layout);
        mUnbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        mProfileId = intent.getIntExtra(PROFILE, -1);

        initView();
        setupTabLayout();
        initPresenter();
    }

    private void initView() {
        mPagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), this, mProfile);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(mPagerAdapter.getCount() - 1);
        mViewPager.setCurrentItem(mPagerAdapter.getDefaultIndex());
    }

    private void setupTabLayout() {
        mTabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);

            if (tab != null) {
                tab.setCustomView(R.layout.home_tab_title_layout);
                View v = tab.getCustomView();

                if (v != null) {
                    TextView tv = v.findViewById(R.id.title);

                    if (tv != null) {
                        tv.setText(mPagerAdapter.getTitle(i));
                    }
                }
            }
        }
    }

    private void initPresenter() {
        mPresenter = new ProfilePresenter();
        mPresenter.attachView(this);
//        mPresenter.getProfile(mProfileId);
        mProfile = UserManager.getCurrentUser().getProfile();
        onFetchProfileSuccess(mProfile);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    @Override
    public void onFetchProfileSuccess(Profile profile) {
        Glide.with(this)
                .load(mProfile.getAvatar())
                .placeholder(R.color.silver)
                .into(mImageView);
    }

    @Override
    public void onEditProfile(String name, String phone, String address) {

    }

    @Override
    public void onRefreshList() {

    }
}
