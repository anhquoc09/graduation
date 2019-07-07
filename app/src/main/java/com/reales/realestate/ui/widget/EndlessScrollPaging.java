package com.reales.realestate.ui.widget;

public interface EndlessScrollPaging {

    public boolean hasNext();

    public boolean isLoading();

    public int getPageSize();

    public boolean isBeforeFirstPage();

    public void reset();
}
