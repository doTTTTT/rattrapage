package com.dot.friendandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserModel {
    @SerializedName("_id")
    public String ID;

    @SerializedName("firstname")
    public String FirstName;

    @SerializedName("lastname")
    public String LastName;

    @SerializedName("email")
    public String email;

    @SerializedName("people")
    public List<PeopleModel> Friends;
}
