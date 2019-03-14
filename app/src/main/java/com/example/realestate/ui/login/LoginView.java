package com.example.realestate.ui.login;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public interface LoginView {
    void showNoNetworkConnection();

    void onLoginSuccess();

    void showLoginError(String message);
}
