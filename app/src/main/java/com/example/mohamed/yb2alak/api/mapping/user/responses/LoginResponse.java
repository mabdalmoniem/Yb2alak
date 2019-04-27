package com.example.mohamed.yb2alak.api.mapping.user.responses;

import com.example.mohamed.yb2alak.api.mapping.user.models.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("data")
    @Expose
    private LoginResponse.NestedUser data;

    public LoginResponse.NestedUser getData() {
        return data;
    }

    public class NestedUser {
        @SerializedName("user")
        @Expose
        private User user;

        @SerializedName("token")
        @Expose
        private String token;

        public User getUser() {
            return user;
        }

        public String getToken() {
            return token;
        }
    }
}
