package com.example.mohamed.yb2alak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.yb2alak.api.Yb2alkApiClient;
import com.example.mohamed.yb2alak.api.Yb2alkApiInterface;
import com.example.mohamed.yb2alak.api.mapping.user.requests.LoginRequest;
import com.example.mohamed.yb2alak.api.mapping.user.responses.LoginResponse;
import com.example.mohamed.yb2alak.api.mapping.user.models.User;
import com.example.mohamed.yb2alak.helper.LocalStorageHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button b_login;
    EditText et_email;
    EditText et_password;
    TextView tv_register_action;
    String email;
    String password;
    Yb2alkApiInterface yb2alkAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        boolean isLoggedIn = LocalStorageHelper.isLoggedIn(LoginActivity.this);
        if (isLoggedIn) {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }

        yb2alkAPI = Yb2alkApiClient.getClient().create(Yb2alkApiInterface.class);

        b_login = findViewById(R.id.b_login);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
    }

    public void loginHandler(View v) {
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if (email.isEmpty()) {
            et_email.setError("Email is required!");
        }

        if (password.isEmpty()) {
            et_password.setError("Password is required!");
        }

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Please fill any missing data", Toast.LENGTH_LONG).show();
            return;
        }

        Call<LoginResponse> call = yb2alkAPI.login(new LoginRequest(email, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                int code = response.code();
                switch (code) {
                    case 200:
                        User user = response.body().getData().getUser();
                        String token = response.body().getData().getToken();
                        user.setToken(token);
                        LocalStorageHelper.destoryAuthUser(LoginActivity.this);
                        LocalStorageHelper.setAuthUser(user, LoginActivity.this);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                        break;

                    case 422:
                        Toast.makeText(LoginActivity.this, "Validation error", Toast.LENGTH_LONG).show();
                        break;

                    case 400:
                        Toast.makeText(LoginActivity.this, "Bad Request", Toast.LENGTH_LONG).show();
                        break;

                    case 500:
                        Toast.makeText(LoginActivity.this, "Internal server error", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "مفيش نت ارضى بنصيبك", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void navigateToRegister(View v) {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }
}
