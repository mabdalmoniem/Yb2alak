package com.example.mohamed.yb2alak.api.mapping.user.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QueryQRRequest {
    @SerializedName("qr_key")
    @Expose
    private String qrKey;

    public QueryQRRequest(String qrKey) {
        this.qrKey = qrKey;
    }
}
