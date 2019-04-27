package com.example.mohamed.yb2alak.api.mapping.user.responses;

import com.example.mohamed.yb2alak.api.mapping.user.models.Friend;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FriendsResponse {
    @SerializedName("data")
    @Expose
    private ArrayList<Friend> friends;

    public ArrayList<Friend> getFriends() {
        return friends;
    }
}
