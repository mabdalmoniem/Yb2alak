package com.example.mohamed.yb2alak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.yb2alak.api.Yb2alkApiClient;
import com.example.mohamed.yb2alak.api.Yb2alkApiInterface;
import com.example.mohamed.yb2alak.api.mapping.user.requests.RegisterRequest;
import com.example.mohamed.yb2alak.api.mapping.user.responses.RegisterResponse;
import com.example.mohamed.yb2alak.api.mapping.user.models.User;
import com.example.mohamed.yb2alak.helper.LocalStorageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    Button b_regitser;
    EditText et_name;
    EditText et_email;
    EditText et_password;
    TextView tv_login_action;
    String name;
    String email;
    String password;
    Yb2alkApiInterface yb2alkAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        boolean isLoggedIn = LocalStorageHelper.isLoggedIn(RegisterActivity.this);
        if (isLoggedIn) {
            Intent i = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(i);
        }

        yb2alkAPI = Yb2alkApiClient.getClient().create(Yb2alkApiInterface.class);

        b_regitser = findViewById(R.id.b_regitser);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_login_action = findViewById(R.id.tv_login_action);
    }

    public void registerHandler(View v) {
        email = et_email.getText().toString().trim();
        password = et_password.getText().toString().trim();
        name = et_name.getText().toString().trim();

        if (name.isEmpty()) {
            et_name.setError("Name is required!");
        }

        if (email.isEmpty()) {
            et_email.setError("Email is required!");
        }

        if (password.isEmpty()) {
            et_password.setError("Password is required!");
        }

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Please fill any missing data", Toast.LENGTH_LONG).show();
            return;
        }

        Call<RegisterResponse> call = yb2alkAPI.register(new RegisterRequest(name, email, password));
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                int code = response.code();
                switch (code) {
                    case 200:
                        User user = response.body().getData().getUser();
                        String token = response.body().getData().getToken();
                        user.setToken(token);
                        LocalStorageHelper.destoryAuthUser(RegisterActivity.this);
                        LocalStorageHelper.setAuthUser(user, RegisterActivity.this);
                        Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(i);
                        break;

                    case 422:
                        try {
                            JSONObject obj = new JSONObject(response.errorBody().string());
                            JSONObject errors = new JSONObject(obj.getString("errors"));
                            Toast.makeText(RegisterActivity.this, "Validation error", Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            Log.e("422 catch", e.getMessage().toString());
                            e.printStackTrace();
                        } catch (IOException e) {
                            Log.e("422 string catch", e.getMessage().toString());
                            e.printStackTrace();
                        }

                        break;

                    case 400:
                        Toast.makeText(RegisterActivity.this, "Bad Request", Toast.LENGTH_LONG).show();
                        break;

                    case 500:
                        Toast.makeText(RegisterActivity.this, "Internal server error", Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Log.e("error", "!!!!!");
                Toast.makeText(RegisterActivity.this, "مفيش نت ارضى بنصيبك", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void navigateToLogin(View v) {
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
