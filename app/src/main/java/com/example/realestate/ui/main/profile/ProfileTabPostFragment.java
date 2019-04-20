package com.example.realestate.ui.main.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.main.ListEstateAdapter;
import com.example.realestate.ui.main.estatedetail.EstateDetailAcivity;
import com.example.realestate.ui.widget.CustomListLayout;
import com.google.android.gms.common.util.CollectionUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
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
        View view = inflater.inflate(R.layout.fragment_list_layout, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        fakeData();
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
        startActivity(EstateDetailAcivity.intentFor(getActivity(), item));
    }

    private void fakeData() {
        List<EstateDetail> list = new ArrayList<>();

        list.add(new EstateDetail(
                "https://sites.google.com/site/camnangtansv/_/rsrc/1396354609847/truong-dhai-hoc-khoa-hoc-tu-nhien/cac-co-so-cua-truong/29.jpg",
                "Cần bán nhà mặt tiền Quận 5, Tp.HCM",
                "Thứ hai, 01/04/2019",
                "10.000",
                "Available",
                "Bán nhà",
                "227 Nguyễn Văn Cừ, Phường 4, quận 5, Tp.HCMmmmmmmmmmmmmmmmm",
                "10.000m2",
                "Nhà 11 tầng có sân thượng, khuôn viên rộng rãi. Ahihi Ahoho Ahaha Ahehe",
                "0348898600",
                1512444,
                "https://instagram.fsgn5-2.fna.fbcdn.net/vp/113926ae61b700f7a15e7ab7dc2172e4/5D4F684C/t51.2885-19/s150x150/49648280_535498350271620_5522308940190187520_n.jpg?_nc_ht=instagram.fsgn5-2.fna.fbcdn.net",
                "Hoàng Anh Quốc",
                "TP.Hồ Chí Minh",
                "10.762879",
                "106.682189"));

        list.add(new EstateDetail(
                "https://images.foody.vn/res/g15/145153/prof/s576x330/foody-mobile-t2-jpg-738-635702210022756988.jpg",
                "Cần bán căn hộ chung cư Quận 1, Tp.HCM",
                "Thứ hai, 01/04/2019",
                "10.000",
                "Available",
                "Bán nhà",
                "235 Đường Nguyễn Văn Cừ, Phường Nguyễn Cư Trinh, Quận 1, Hồ Chí Minh",
                "100m2",
                "Nhà nhiều phòng, có trung tâm mua sắm. Ahihi Ahoho Ahaha Ahehe",
                "0348898600",
                1512444,
                "https://instagram.fsgn5-2.fna.fbcdn.net/vp/113926ae61b700f7a15e7ab7dc2172e4/5D4F684C/t51.2885-19/s150x150/49648280_535498350271620_5522308940190187520_n.jpg?_nc_ht=instagram.fsgn5-2.fna.fbcdn.net",
                "Hoàng Anh Quốc",
                "TP.Hồ Chí Minh",
                "10.764354",
                "106.682675"));

        setData(list);
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
