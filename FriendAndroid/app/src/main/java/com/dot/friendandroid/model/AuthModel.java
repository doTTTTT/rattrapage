package com.dot.friendandroid.model;

import com.google.gson.annotations.SerializedName;

import retrofit2.Call;

public class AuthModel {
    @SerializedName("token")
    public String token;

    @SerializedName("user")
    public UserModel User;
}
