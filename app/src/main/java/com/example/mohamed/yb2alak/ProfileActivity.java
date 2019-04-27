package com.example.mohamed.yb2alak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.mohamed.yb2alak.api.Yb2alkApiClient;
import com.example.mohamed.yb2alak.api.Yb2alkApiInterface;
import com.example.mohamed.yb2alak.api.mapping.user.models.User;
import com.example.mohamed.yb2alak.helper.LocalStorageHelper;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    ImageView iv_qr;
    Yb2alkApiInterface yb2alkAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        boolean isLoggedIn = LocalStorageHelper.isLoggedIn(ProfileActivity.this);
        User user = LocalStorageHelper.getAuthUser(ProfileActivity.this);
        if (user.getToken().isEmpty() || user.getToken() == null || user.getToken().length() == 0 || !isLoggedIn) {
            Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(i);
            return;
        }

        iv_qr = findViewById(R.id.iv_qr);

        yb2alkAPI = Yb2alkApiClient.getClient().create(Yb2alkApiInterface.class);

        Call<ResponseBody> call = yb2alkAPI.generateQR("bearer " + user.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("res =>>", response.body().toString());
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                iv_qr.setImageBitmap(bmp);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("err =>>", t.getMessage());
            }
        });
    }
}
