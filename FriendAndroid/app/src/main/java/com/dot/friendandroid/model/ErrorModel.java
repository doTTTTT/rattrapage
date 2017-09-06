package com.dot.friendandroid.model;

import com.google.gson.annotations.SerializedName;

public class ErrorModel {
        @SerializedName("status")
        public int Code;

        @SerializedName("message")
        public String ErrorMessage;
}