package com.example.realestate.ui.widget;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class EndlessScrollDownListener extends RecyclerView.OnScrollListener {

    private static final boolean DEBUG = true;

    private final EndlessScrollPaging mPaging;

    private int mVisibleThreshold = 5;

    private int[] mLastStaggerPosition;

    private PageLoader mPageLoader;

    public EndlessScrollDownListener(EndlessScrollPaging paging, RecyclerView.LayoutManager layoutManager) {
        mPaging = paging;

        if (layoutManager instanceof GridLayoutManager) {
            mVisibleThreshold = mVisibleThreshold * ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            mVisibleThreshold = mVisibleThreshold * ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        loadNextPageIfNeeded(recyclerView, dx, dy);
    }

    public void setPageLoader(PageLoader pageLoader) {
        mPageLoader = pageLoader;
    }

    private void loadNextPageIfNeeded(RecyclerView recyclerView, int dx, int dy) {
        if (mPaging == null
                || !mPaging.hasNext()
                || mPaging.isLoading()) {
            return;
        }

        if (isScrollNearBottom(recyclerView, dy) && mPageLoader != null) {
            mPageLoader.loadNextPage();
        }
    }

    private boolean isScrollNearBottom(RecyclerView recyclerView, int dy) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        boolean isScrollDown = dy > 0;

        int lastVisibleIndex;

        int totalItem = layoutManager.getItemCount();

        if (layoutManager instanceof LinearLayoutManager) {
            lastVisibleIndex = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (mLastStaggerPosition == null) {
                mLastStaggerPosition = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            }

            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(mLastStaggerPosition);

            lastVisibleIndex = findMax(mLastStaggerPosition);
        } else {
            throw new RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }

        return ((totalItem - lastVisibleIndex) <= mVisibleThreshold) && isScrollDown;
    }

    private int findMax(int[] lastStaggerPosition) {
        int max = Integer.MIN_VALUE;

        for (int value : lastStaggerPosition) {
            if (value > max) {
                max = value;
            }
        }

        return max;
    }

    /**
     * {@link PageLoader}
     */
    public interface PageLoader {
        void loadNextPage();
    }
}
