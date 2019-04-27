package com.example.mohamed.yb2alak.api.mapping.user.responses;

import com.example.mohamed.yb2alak.api.mapping.user.models.UserQR;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueryQRResponse {
    @SerializedName("data")
    @Expose
    private UserQR userQR;

    public UserQR getUserQR() {
        return userQR;
    }
}
