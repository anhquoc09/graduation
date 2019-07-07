package com.reales.realestate.ui.widget;

public final class Paging extends AbstractPaging {

    private int mCurrentPage;

    public Paging() {
        this(DEFAULT_PAGE_SIZE);
    }

    public Paging(int pageSize) {
        this(0, pageSize);
    }

    public Paging(int currentPage, int pageSize) {
        super(pageSize);
        mCurrentPage = currentPage;
    }

    public int getCurrent() {
        return mCurrentPage;
    }

    public int getNext() {
        return mCurrentPage + 1;
    }

    public void next() {
        mCurrentPage = mCurrentPage + 1;
    }

    public boolean isFirstPage() {
        return mCurrentPage == 1;
    }

    @Override
    public boolean isBeforeFirstPage() {
        return mCurrentPage == 0;
    }

    @Override
    public void reset() {
        super.reset();
        mCurrentPage = 0;
    }
}
