package com.dot.friendandroid.model;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PeopleModel {
    @SerializedName("_id")
    public String ID;

    @SerializedName("firstname")
    public String FirstName;

    @SerializedName("lastname")
    public String LastName;

    @SerializedName("date_of_birth")
    public String DateOfBirth;

    @SerializedName("description")
    public String Description;

    @SerializedName("phone_number")
    public String PhoneNumber;

    @SerializedName("relationship")
    public String RelationShip;

    @SerializedName("address")
    public Address Address;

    public class Address {
        @SerializedName("street")
        public String Street;

        @SerializedName("city")
        public String City;

        @SerializedName("state")
        public String State;

        @SerializedName("zipcode")
        public String ZipCode;

        @SerializedName("country")
        public String Country;
    }
}
