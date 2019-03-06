package com.example.realestate.ui;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public class BasePresenter<V>{

    protected V mView;

    public void attachView(V view) {
        mView = view;
    }

    public void detachView() {
        mView = null;
    }

    public boolean isViewAttached() {
        return null != mView;
    }
}