package com.example.realestate.ui.main.newestate;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.main.ListEstateAdapter;
import com.example.realestate.ui.main.estatedetail.EstateDetailActivity;
import com.example.realestate.ui.main.profile.ProfileActivity;
import com.example.realestate.ui.widget.CustomListLayout;
import com.example.realestate.ui.widget.EndlessNestedScrollViewListener;
import com.example.realestate.ui.widget.EndlessScrollDownListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class ListNewEstateFragment extends Fragment
        implements ListNewEstateView,
        SwipeRefreshLayout.OnRefreshListener,
        ListEstateAdapter.OnItemClickListener {

    public static final String TAG = ListNewEstateFragment.class.getSimpleName();

    @BindView(R.id.coordination_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_layout)
    View mEmptyView;

    @BindView(R.id.progress_layout)
    View mProgress;

    private Unbinder mUnbinder;

    private ListEstateAdapter mAdapter;

    private ListNewEstatePresenter mPresenter;

    private Snackbar mSnackBar;

    public static Fragment newInstance() {
        ListNewEstateFragment fragment = new ListNewEstateFragment();
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
        EndlessScrollDownListener listener = new EndlessScrollDownListener(mPresenter.getPaging(), mRecyclerView.getLayoutManager());
        listener.setPageLoader(() -> mPresenter.fetchData());
        mRecyclerView.addOnScrollListener(listener);
    }

    private void initPresenter() {
        mPresenter = new ListNewEstatePresenter();
        mPresenter.attachView(this);
        mPresenter.fetchData();
    }

    private void initView() {
        mAdapter = new ListEstateAdapter();
        mAdapter.setItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mSnackBar = Snackbar
                .make(mCoordinatorLayout, getString(R.string.no_network_connection), Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.setting), view -> startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS)));
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
            mPresenter.reset();
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
    public void saveProject(EstateDetail item, int position) {
        mPresenter.saveProject(item.getFullName(), item.getId(), item.getCreateTime(), position);
    }

    @Override
    public void unSaveProject(EstateDetail item, int position) {
        mPresenter.unSaveProject(item.getId(), position);
    }

    @Override
    public void fetchDataSuccess(List<EstateDetail> list) {
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
    public void loadMoreDataSuccess(List<EstateDetail> estateDetails) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mAdapter.appendData(estateDetails);
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
    public void saveEstateSuccess(int position) {
        mAdapter.saveSuccess(position);
    }

    @Override
    public void unSaveEstateSuccess(int position) {
        mAdapter.saveSuccess(position);
    }
}
