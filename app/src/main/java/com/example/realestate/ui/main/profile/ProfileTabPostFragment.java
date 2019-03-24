package com.example.realestate.ui.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.main.home.HomeTabListFragment;
import com.example.realestate.ui.main.listestate.ListEstateAdapter;
import com.example.realestate.ui.widget.CustomListLayout;
import com.google.android.gms.common.util.CollectionUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProfileTabPostFragment extends Fragment
        implements ListEstateAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.list_layout)
    CustomListLayout mListLayout;

    private Unbinder mUnbinder;

    private ListEstateAdapter mAdapter;

    private OnCallBackListener mCallBackListener;

    public static Fragment newInstance(OnCallBackListener listener) {
        ProfileTabPostFragment fragment = new ProfileTabPostFragment();
        fragment.setOnCallBackListener(listener);
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
        View view = inflater.inflate(R.layout.fragment_list_estate_layout, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
    }

    private void initView() {
        mAdapter = new ListEstateAdapter();
        mAdapter.setItemClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mListLayout.setLayoutManager(layoutManager);
        mListLayout.setAdapter(mAdapter);
        mListLayout.setOnRefreshListener(this);
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        if (mCallBackListener != null) {
            mCallBackListener.onRefreshList();
        }
    }

    @Override
    public void onItemSelected(EstateDetail item) {

    }

    public void setData(List<EstateDetail> list) {
        if (mListLayout.isRefreshing()) {
            mListLayout.setRefreshing(false);
        }

        if (CollectionUtils.isEmpty(list)) {
            mListLayout.showEmptyLayout();
        } else {
            mListLayout.hideEmptyLayout();
            mAdapter.setData(list);
        }
    }

    public void setOnCallBackListener(OnCallBackListener listener) {
        mCallBackListener = listener;
    }

    /**
     * {@link OnCallBackListener}
     */
    public interface OnCallBackListener {
        void onRefreshList();
    }
}
