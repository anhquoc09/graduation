package com.reales.realestate.ui.main;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public interface MainView {
    void logoutFinished();

    void showProgress();

    void hideProgress();

    void hideNoNetwork();

    void showNoNetwork();

    void fetchSavedListSuccess();
}
