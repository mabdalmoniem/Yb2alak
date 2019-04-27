package com.example.mohamed.yb2alak.api.mapping.user.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("qr_key")
    @Expose
    private String qrKey;

    public User(String name, String email, String token) {
        this.name = name;
        this.email = email;
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getQrKey() {
        return qrKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
