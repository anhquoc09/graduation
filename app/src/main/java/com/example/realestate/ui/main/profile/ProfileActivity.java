package com.example.realestate.ui.main.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.data.model.Profile;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.ui.main.ListEstateAdapter;
import com.example.realestate.ui.main.estatedetail.EstateDetailActivity;
import com.example.realestate.ui.widget.EndlessNestedScrollViewListener;
import com.example.realestate.utils.AndroidUtilities;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileActivity extends BaseActivity implements ProfileView, ListEstateAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = ProfileActivity.class.getSimpleName();

    public static final String PROFILE = "profileId";

    @BindView(R.id.coordination_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.scroll_view)
    NestedScrollView mScrollView;

    @BindView(R.id.btn_edit_profile)
    ImageView mBtnEdit;

    @BindView(R.id.profile_avatar)
    ImageView mAvatar;

    @BindView(R.id.profile_name)
    TextView mName;

    @BindView(R.id.profile_phone)
    TextView mPhone;

    @BindView(R.id.profile_email)
    TextView mEmail;

    @BindView(R.id.profile_address)
    TextView mAddress;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_layout)
    View mEmptyView;

    @BindView(R.id.progress_layout)
    View mProgress;

    private String mProfileId;

    private ProfilePresenter mPresenter;

    private ListEstateAdapter mAdapter;

    private Unbinder mUnbinder;

    private Snackbar mSnackbar;

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
        mProfileId = intent.getStringExtra(PROFILE);

        initPresenter();
        initView();
        getUserProfile(UserManager.getCurrentUser().getId().equals(mProfileId));
    }

    private void initView() {
        if (UserManager.isUserLoggedIn() && UserManager.getCurrentUser().getId().equals(mProfileId)) {
            onFetchProfileSuccess(UserManager.getCurrentUser().getProfile());
            mBtnEdit.setVisibility(View.VISIBLE);
        } else {
            mBtnEdit.setVisibility(View.INVISIBLE);
        }

        mAdapter = new ListEstateAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        EndlessNestedScrollViewListener listener = new EndlessNestedScrollViewListener(mPresenter.getPaging());
        listener.setPageLoader(() -> mPresenter.loadMore());

        mScrollView.setOnScrollChangeListener(listener);

        mSnackbar = Snackbar
                .make(mCoordinatorLayout, getString(R.string.no_network_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.setting), view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
    }

    private void initPresenter() {
        mPresenter = new ProfilePresenter();
        mPresenter.attachView(this);
    }

    private void getUserProfile(boolean equals) {
        mPresenter.getUserProfile(mProfileId, equals);
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
        if (profile != null) {
            setAvatar(profile.getAvatar());
            setName(profile.getFullname());
            setPhone(profile.getPhone());
            setEmail(profile.getEmail());
            setAddress(profile.getAddress());
        }
    }

    @Override
    public void onFetchListEstateSuccess(List<EstateDetail> data) {
        mRefreshLayout.setRefreshing(false);
        mAdapter.setData(data);

        if (!CollectionUtils.isEmpty(data)) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoadMoreListSuccess(List<EstateDetail> data) {
        mAdapter.appendData(data);
    }

    @Override
    public void hideListProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showListProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoNetwork() {
        mSnackbar.dismiss();
    }

    @Override
    public void showNoNetwork() {
        mSnackbar.show();
    }

    @Override
    public void showProgress() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mRefreshLayout.setRefreshing(false);
    }

    private void setAvatar(String avatar) {
        Glide.with(this)
                .load(avatar)
                .placeholder(R.color.silver)
                .error(R.drawable.avatar_default_small)
                .into(mAvatar);
    }

    private void setName(String fullName) {
        mName.setText(fullName);
    }

    private void setPhone(String phone) {
        mPhone.setText(phone);
    }

    private void setEmail(String email) {
        mEmail.setText(email);
    }

    private void setAddress(String address) {
        mAddress.setText(address);
    }

    @OnClick(R.id.btn_back)
    public void onBackClick() {
        onBackPressed();
    }

    @OnClick(R.id.btn_edit_profile)
    public void onEditClick() {
        AndroidUtilities.showToast(getString(R.string.feature_not_support));
    }

    @Override
    public void onItemSelected(EstateDetail item) {
        startActivity(EstateDetailActivity.intentFor(this, item));
    }

    @Override
    public void onRefresh() {
        mPresenter.resetProfile();
    }
}
