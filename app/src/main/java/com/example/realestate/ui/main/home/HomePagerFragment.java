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

    private void fetchData(double latitude, double longitude) {
        if (mPresenter != null) {
            mPresenter.fetchData(latitude, longitude);
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
    public void getListInMap(double latitude, double longitude) {
        fetchData(latitude, longitude);
    }

    @Override
    public void onRefreshList() {
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
