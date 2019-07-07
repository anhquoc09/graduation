package com.reales.realestate.ui.widget;

import android.view.View;

import androidx.core.widget.NestedScrollView;

public class EndlessNestedScrollViewListener implements NestedScrollView.OnScrollChangeListener {

    private final EndlessScrollPaging mPaging;

    private PageLoader mPageLoader;

    private int mOffsetFromBottom;

    public EndlessNestedScrollViewListener(EndlessScrollPaging paging) {
        mPaging = paging;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (!isScrollDown(scrollY, oldScrollY)
                || mPaging == null
                || !mPaging.hasNext()
                || mPaging.isLoading()
                || mPageLoader == null) {
            return;
        }

        View lastChild = v.getChildAt(v.getChildCount() - 1);

        int bottomOfLastChild = lastChild.getBottom();
        int scrollViewHeight = v.getHeight();

        int diff = bottomOfLastChild - (scrollViewHeight + scrollY);

        if (diff <= mOffsetFromBottom) {
            mPageLoader.loadNextPage();
        }
    }

    private boolean isScrollDown(int newScrollY, int oldScrollY) {
        return newScrollY - oldScrollY > 0;
    }

    public void setPageLoader(PageLoader pageLoader) {
        mPageLoader = pageLoader;
    }

    public void setOffsetFromBottom(int offsetFromBottom) {
        mOffsetFromBottom = offsetFromBottom;
    }

    /**
     * {@link PageLoader}
     */
    public interface PageLoader {
        void loadNextPage();
    }
}
