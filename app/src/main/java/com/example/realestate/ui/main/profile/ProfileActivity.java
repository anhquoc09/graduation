package com.example.realestate.ui.main.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.realestate.R;
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

    public static final String PROFILE_ID = "profile_id";

    public static final String PROFILE_AVATAR = "profile_avatar";

    public static final String PROFILE_DISPLAYNAME = "profile_display_name";

    public static final String PROFILE_ADDRESS = "profile_address";

    public static final String PROFILE_EMAIL = "profile_email";

    public static final String PROFILE_PHONE = "profile_phone";

    @BindView(R.id.profile_avatar)
    ImageView mImageView;

    @BindView(R.id.profile_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.profile_tab_layout)
    TabLayout mTabLayout;

    private int mProfileId;

    private String mProfileAvatar;

    private String mProfileDisplayName;

    private String mProfileAddress;

    private String mProfileEmail;

    private String mProfilePhone;

    private ProfilePresenter mPresenter;

    private ProfilePagerAdapter mPagerAdapter;

    private Unbinder mUnbinder;

    public static Intent intentFor(Context context, int profileId, String avatar, String displayName, String address, String email, String phoneNumber) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(PROFILE_ID, profileId);
        intent.putExtra(PROFILE_AVATAR, avatar);
        intent.putExtra(PROFILE_DISPLAYNAME, displayName);
        intent.putExtra(PROFILE_ADDRESS, address);
        intent.putExtra(PROFILE_EMAIL, email);
        intent.putExtra(PROFILE_PHONE, phoneNumber);

        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile_layout);
        mUnbinder = ButterKnife.bind(this);

        Intent intent = getIntent();
        mProfileId = intent.getIntExtra(PROFILE_ID, 0);
        mProfileAvatar = intent.getStringExtra(PROFILE_AVATAR);
        mProfileDisplayName = intent.getStringExtra(PROFILE_DISPLAYNAME);
        mProfileAddress = intent.getStringExtra(PROFILE_ADDRESS);
        mProfileEmail = intent.getStringExtra(PROFILE_EMAIL);
        mProfilePhone = intent.getStringExtra(PROFILE_PHONE);

        initView();
        setupTabLayout();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new ProfilePresenter();
        mPresenter.attachView(this);
        mPresenter.getProfile(mProfileId);
    }

    private void initView() {
        mPagerAdapter = new ProfilePagerAdapter(getSupportFragmentManager(), this);
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
    public void onFetchProfileSuccess(ProfileDetail profile) {

    }

    @Override
    public void onEditProfile(String name, String phone, String address) {

    }

    @Override
    public void onRefreshList() {

    }
}
