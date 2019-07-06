package com.example.realestate.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by quocha2
 * On 06/07/2019
 */
public class UserComment {
    @SerializedName("id")
    private String mId;

    @SerializedName("email")
    private String mEmail;

    @SerializedName("fullname")
    private String mFullname;

    @SerializedName("avatar")
    private String mAvatar;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFullname() {
        return mFullname;
    }

    public void setFullname(String fullname) {
        mFullname = fullname;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }
}
