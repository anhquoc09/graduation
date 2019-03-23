package com.example.realestate.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.example.realestate.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author anhquoc09
 * @since 24/03/2019
 */

public class CustomListLayout extends FrameLayout {
    private static final String TAG = CustomListLayout.class.getSimpleName();

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_layout)
    View mEmpty;

    public CustomListLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public CustomListLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomListLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.custom_list_layout, this);
        ButterKnife.bind(this);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setOnRefreshListener(listener);
        }
    }

    public boolean isRefreshing() {
        if (mRefreshLayout != null) {
            return mRefreshLayout.isRefreshing();
        }
        return false;
    }

    public void setRefreshing(boolean b) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshing(b);
        }
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(layoutManager);
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }

    public void showEmptyLayout() {
        if (mEmpty != null) {
            mEmpty.setVisibility(VISIBLE);
        }
    }

    public void hideEmptyLayout() {
        if (mEmpty != null) {
            mEmpty.setVisibility(GONE);
        }
    }
}
