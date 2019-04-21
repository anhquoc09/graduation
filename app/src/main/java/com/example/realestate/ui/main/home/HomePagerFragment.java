package com.example.realestate.ui.main.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.realestate.R;
import com.example.realestate.data.model.EstateDetail;
import com.example.realestate.ui.main.home.map.MapFragment;
import com.example.realestate.ui.widget.DebounceEditText;
import com.example.realestate.utils.AndroidUtilities;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class HomePagerFragment extends Fragment
        implements HomePagerView,
        MapFragment.OnCallBackListener,
        HomeTabListFragment.OnCallBackListener {

    public static final String TAG = HomePagerFragment.class.getSimpleName();

    @BindView(R.id.ic_delete_search)
    ImageView mIconClear;

    @BindView(R.id.search_text)
    EditText mSearchText;

    @BindView(R.id.more_filter_layout)
    View mMoreFilterLayout;

    @BindView(R.id.btn_more_filter)
    View mBtnMoreFilter;

    @BindView(R.id.btn_less_filter)
    View mBtnLessFilter;

    @BindView(R.id.home_viewpager)
    ViewPager mViewPager;

    @BindView(R.id.home_tab_layout)
    TabLayout mTabLayout;

    private Unbinder mUnbinder;

    private HomePagerPresenter mPresenter;

    private HomePagerAdapter mAdapter;

    private String mCurrentSearchKey;

    private DebounceEditText mDebounceEditText;

    public static HomePagerFragment newInstance() {
        HomePagerFragment fragment = new HomePagerFragment();
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
        View view = inflater.inflate(R.layout.fragment_home_layout, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        initSearchBox();
        initPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();
//        mPresenter.fetchData();
        fakeData();
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mDebounceEditText != null) {
            mDebounceEditText.destroy();
        }
        super.onDestroyView();
    }

    private void initView() {
        initAdapter();
        setupTabLayout();
    }

    private void initAdapter() {
        mAdapter = new HomePagerAdapter(getChildFragmentManager(), getContext());
        mAdapter.setMapListener(this);
        mAdapter.setListListener(this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mAdapter.getCount() - 1);
        mViewPager.setCurrentItem(mAdapter.getDefaultIndex());
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
                        tv.setText(mAdapter.getTitle(i));
                    }
                }
            }
        }
    }

    private void initSearchBox() {
        mDebounceEditText = new DebounceEditText(mSearchText, result -> mCurrentSearchKey = result.toString());

        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mIconClear.setVisibility(View.INVISIBLE);
                } else {
                    mIconClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initPresenter() {
        mPresenter = new HomePagerPresenter();
        mPresenter.attachView(this);
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

        fetchDataSuccess(list);
    }

    private void fetchData() {
        if (mPresenter != null) {
//            mPresenter.fetchData();
        }
    }

    @OnClick(R.id.btn_more_filter)
    public void onMoreFilterClick() {
        mMoreFilterLayout.setVisibility(View.VISIBLE);
        mBtnLessFilter.setVisibility(View.VISIBLE);
        mBtnMoreFilter.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_less_filter)
    public void onLessFilterClick() {
        mMoreFilterLayout.setVisibility(View.GONE);
        mBtnLessFilter.setVisibility(View.GONE);
        mBtnMoreFilter.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_for)
    public void onBtnForClick() {

    }

    @OnClick(R.id.btn_type)
    public void onBtnTypeClick() {

    }

    @OnClick(R.id.btn_district)
    public void onBtnDistrictClick() {

    }

    @OnClick(R.id.btn_search)
    public void onBtnSearchClick() {

    }

    @Override
    public void onMoveMap() {
        fetchData();
    }

    @Override
    public void onRefreshMap() {
        fetchData();
    }

    @Override
    public void onRefreshList() {
        fetchData();
        AndroidUtilities.showToast("Refresh");
    }

    @Override
    public void fetchDataSuccess(List<EstateDetail> list) {
        if (mAdapter == null) {
            return;
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            Fragment page = mAdapter.getPage(i);

            if (page instanceof MapFragment) {
                ((MapFragment) page).setData(list);
            }

            if (page instanceof HomeTabListFragment) {
                ((HomeTabListFragment) page).setData(list);
            }
        }
    }
}
