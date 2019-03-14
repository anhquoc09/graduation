package com.example.realestate.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.realestate.ui.map.MapActivity;
import com.example.realestate.R;
import com.example.realestate.ui.BaseActivity;
import com.example.realestate.ui.widget.DebounceEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.ic_delete_search)
    ImageView mIconClear;

    @BindView(R.id.search_text)
    EditText mSearchText;

    private String mCurrentSearchKey;

    private Unbinder mUnbinder;

    private DebounceEditText mDebounceEditText;

    public static Intent intentFor(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mUnbinder = ButterKnife.bind(this);
        
        initView();

        initPresenter();
    }

    private void initView() {
        initSearchBox();
    }

    private void initPresenter() {

    }

    private void initSearchBox() {
        mDebounceEditText = new DebounceEditText(mSearchText, new DebounceEditText.PublishResultListener() {
            @Override
            public void onPublishResult(CharSequence result) {
                mCurrentSearchKey = result.toString();
            }
        });

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

    private void fetchNewList() {

    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        if (mDebounceEditText != null) {
            mDebounceEditText.destroy();
        }
        super.onDestroy();
    }

    private void hideIconDeleteSearch() {
        if (mIconClear != null) {
            mIconClear.setVisibility(View.GONE);
        }
    }

    private void showIconDeleteSearch() {
        if (mIconClear != null) {
            mIconClear.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.ic_profile)
    public void onProfileClick() {
    }

    @OnClick(R.id.ic_menu)
    public void onMenuClick() {
    }

    @OnClick(R.id.btn_search)
    public void onSearchClick() {
        fetchNewList();
    }

    @OnClick(R.id.ic_delete_search)
    public void onClearSearchClick() {
        mSearchText.setText("");
    }

    @OnClick(R.id.btn_view_map)
    public void onViewMapClick() {
        startActivity(MapActivity.intentFor(this));
    }

    @OnClick(R.id.btn_forum)
    public void onForumClick() {
    }

    @OnClick(R.id.btn_exchange)
    public void onExchangeClick() {
    }

    @OnClick(R.id.btn_project)
    public void onProjectClick() {
    }

    @OnClick(R.id.btn_consultancy)
    public void onConsultancyClick() {
    }
}
