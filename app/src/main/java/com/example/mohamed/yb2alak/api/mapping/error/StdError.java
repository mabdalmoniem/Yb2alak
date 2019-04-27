package com.example.mohamed.yb2alak.api.mapping.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StdError {
    @SerializedName("errors")
    @Expose
    private ArrayList<Error> errors;
}
