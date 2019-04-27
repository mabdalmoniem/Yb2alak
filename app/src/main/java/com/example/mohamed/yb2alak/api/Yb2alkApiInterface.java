package com.example.mohamed.yb2alak.api;

import com.example.mohamed.yb2alak.api.mapping.user.responses.FriendsResponse;
import com.example.mohamed.yb2alak.api.mapping.user.requests.LoginRequest;
import com.example.mohamed.yb2alak.api.mapping.user.responses.LoginResponse;
import com.example.mohamed.yb2alak.api.mapping.user.requests.QueryQRRequest;
import com.example.mohamed.yb2alak.api.mapping.user.requests.RegisterRequest;
import com.example.mohamed.yb2alak.api.mapping.user.responses.QueryQRResponse;
import com.example.mohamed.yb2alak.api.mapping.user.responses.RegisterResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface Yb2alkApiInterface {
    @Headers({"Accept: application/json", "Content-type: application/json"})
    @POST("api/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @Headers({"Accept: application/json", "Content-type: application/json"})
    @POST("api/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @Headers({"Accept: application/json", "Content-type: application/json"})
    @GET("api/user/friends")
    Call<FriendsResponse> getFriends(@Header("Authorization") String jwt);

    @Headers({"Accept: application/json", "Content-type: application/json"})
    @GET("api/qr-code/generate")
    Call<ResponseBody> generateQR(@Header("Authorization") String jwt);

    @Headers({"Accept: application/json", "Content-type: application/json"})
    @POST("api/qr-code/get-user")
    Call<QueryQRResponse> queryQR(@Header("Authorization") String jwt, @Body QueryQRRequest queryQRRequest);
}
