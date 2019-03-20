package com.example.realestate.ui.main.home.listpost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.main.home.map.MapFragment;

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

public class ListEstateFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener, ListEstateAdapter.OnItemClickListener {

    public static final String TAG = ListEstateFragment.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    private Unbinder mUnbinder;

    private ListEstateAdapter mAdapter;

    private OnCallBackListener mCallBackListener;

    public static Fragment newInstance(OnCallBackListener listener) {
        ListEstateFragment fragment = new ListEstateFragment();
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
        View view = inflater.inflate(R.layout.list_estate_layout, container, false);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(this);
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
        if (mRefreshLayout.isRefreshing()) {
            mRefreshLayout.setRefreshing(false);
        }

        if (mAdapter != null) {
            mAdapter.setData(list);
        }
    }

    public void setOnCallBackListener(OnCallBackListener listener) {
        mCallBackListener = listener;
    }

    /**
     * {@link MapFragment.OnCallBackListener}
     */
    public interface OnCallBackListener {
        void onRefreshList();
    }
}
