package com.example.realestate.ui.main.savedlist;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.realestate.R;
import com.example.realestate.UserManager;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.data.model.SavedPost;
import com.example.realestate.ui.login.LoginActivity;
import com.example.realestate.ui.estatedetail.EstateDetailActivity;
import com.example.realestate.ui.profile.ProfileActivity;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 30/03/2019
 */

public class SavedListFragment extends Fragment
        implements SavedListView,
        SavedListEstateAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.coordination_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_layout)
    TextView mEmptyView;

    @BindView(R.id.progress_layout)
    View mProgress;

    @BindView(R.id.login_layout)
    View mLogInLayout;

    private Unbinder mUnbinder;

    private SavedListEstateAdapter mAdapter;

    private SavedListPresenter mPresenter;

    private Snackbar mSnackBar;

    public static Fragment newInstance() {
        SavedListFragment fragment = new SavedListFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initPresenter();
    }

    private void initPresenter() {
        mPresenter = new SavedListPresenter();
        mPresenter.attachView(this);
    }

    private void initView() {
        mAdapter = new SavedListEstateAdapter();
        mAdapter.setItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSnackBar = Snackbar
                .make(mCoordinatorLayout, getString(R.string.no_network_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.setting), view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));

        mEmptyView.setText(getString(R.string.saved_empty));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!UserManager.isUserLoggedIn()) {
            mLogInLayout.setVisibility(View.VISIBLE);
        } else {
            mLogInLayout.setVisibility(View.GONE);
            mPresenter.fetchData();
        }
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        if (mPresenter != null) {
            mPresenter.fetchData();
        }
    }

    @Override
    public void onItemSelected(EstateDetail item) {
        startActivity(EstateDetailActivity.intentFor(getActivity(), item));
    }

    @Override
    public void onOwnerClick(String userId) {
        startActivity(ProfileActivity.intentFor(getActivity(), userId));
    }

    @Override
    public void unSavePost(EstateDetail item, int position) {
        mPresenter.unSavePost(item.getId(), position);
    }

    @Override
    public void fetchDataSuccess(List<SavedPost> list) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if (CollectionUtils.isEmpty(list)) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
        mAdapter.setData(list);
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
    public void unSaveEstateSuccess(int position) {
        mAdapter.unSaveSuccess(position);
    }

    @OnClick(R.id.btn_login)
    public void onLogInClick() {
        startActivity(LoginActivity.intentFor(getContext(), true));
    }
}
