package com.example.realestate.data.remote.response;

import com.example.realestate.data.model.LoginData;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */

public class LoginResponse {

    private int mCode = 0;

    private String mMessage = "ahihi";

    private LoginData mLoginData = new LoginData();

    public LoginData getData() {
        return null;
    }

    public boolean isSuccessful() {
        return mCode == 0;
    }

    public String getMessage() {
        return mMessage;
    }
}
