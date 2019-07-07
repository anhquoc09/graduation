package com.reales.realestate.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.reales.realestate.EstateApplication;
import com.reales.realestate.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author anhquoc09
 * @since 20/03/2019
 */

public class MainTabLayout extends LinearLayout {

    public static final int HOME_INDEX = 0;

    public static final int NEWEST_INDEX = 1;

    public static final int SAVED_INDEX = 2;

    @BindView(R.id.tab_home)
    TextView mHomeTab;

    @BindView(R.id.tab_newest)
    TextView mNewestTab;

    @BindView(R.id.tab_saved)
    TextView mSavedTab;

    private int mSelectedIndex = -1;

    private List<Tab> mTabs;

    private OnTabSelectListener mTabSelectListener;

    public MainTabLayout(Context context) {
        super(context);
        init(context, null);
    }

    public MainTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MainTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.main_tabs_layout, this);
        ButterKnife.bind(this);

        setBackgroundColor(Color.WHITE);

        setOrientation(VERTICAL);

        final TextView[] tabViews = {mHomeTab, mNewestTab, mSavedTab};

        int count = tabViews.length;
        mTabs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Tab tab = new Tab();
            tab.setTextView(tabViews[i]);
            tabViews[i].setVisibility(VISIBLE);
            mTabs.add(tab);
        }

        setSelected(HOME_INDEX, true);
    }

    private void notifyTabSelect(int tabIndex) {
        if (mTabSelectListener != null) {
            mTabSelectListener.onTabSelect(tabIndex);
        }
    }

    public void setTabSelectListener(OnTabSelectListener tabSelectListener) {
        mTabSelectListener = tabSelectListener;
    }

    public void setSelected(int tabIndex, boolean notify) {
        if (tabIndex < 0 || tabIndex >= mTabs.size()) {
            return;
        }
        if (mSelectedIndex != -1 && tabIndex != mSelectedIndex) {
            mTabs.get(mSelectedIndex).setSelected(false);
        }
        mTabs.get(tabIndex).setSelected(true);
        mSelectedIndex = tabIndex;

        if (notify) {
            notifyTabSelect(tabIndex);
        }
    }

    public int getSelected() {
        return mSelectedIndex;
    }

    public int getCount() {
        return mTabs == null ? 0 : mTabs.size();
    }

    public String getTabTitle(int i) {
        if (i < 0 || i >= mTabs.size()) {
            return null;
        }
        return mTabs.get(i).getTitle();
    }

    @OnClick(R.id.tab_home)
    public void onHomeClick() {
        setSelected(HOME_INDEX, true);
    }

    @OnClick(R.id.tab_newest)
    public void onNewestClick() {
        setSelected(NEWEST_INDEX, true);
    }

    @OnClick(R.id.tab_saved)
    public void onSavedClick() {
        setSelected(SAVED_INDEX, true);
    }

    /**
     * {@link Tab}
     */
    private static class Tab {

        private TextView mView;

        public void setTextView(TextView tv) {
            mView = tv;
        }

        public void setSelected(boolean selected) {
            if (selected) {
                mView.setBackgroundColor(
                        EstateApplication.getInstance()
                                .getApplicationContext()
                                .getResources()
                                .getColor(R.color.tab_selected));
            } else {
                mView.setBackgroundColor(Color.WHITE);
            }
        }

        public String getTitle() {
            return String.valueOf(mView.getText());
        }
    }

    /**
     * {@link OnTabSelectListener}
     */
    public interface OnTabSelectListener {
        void onTabSelect(int tabIndex);
    }
}
