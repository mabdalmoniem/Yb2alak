package com.example.mohamed.yb2alak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mohamed.yb2alak.api.mapping.user.models.Friend;
import com.example.mohamed.yb2alak.api.mapping.user.models.User;
import com.example.mohamed.yb2alak.api.mapping.user.models.UserQR;

public class AddTransactionActivity extends AppCompatActivity {

    TextView tv_user_name;
    Button btn_confirm;
    Button btn_cancel;
    String name;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        UserQR userQR = (UserQR) getIntent().getSerializableExtra("userQR");
        Friend friend = (Friend) getIntent().getSerializableExtra("friend");

        tv_user_name = findViewById(R.id.tv_user_name);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_confirm = findViewById(R.id.btn_confirm);

        if (friend != null) {
            id = friend.getId();
            name = friend.getName();
        } else if (userQR != null) {
            id = userQR.getId();
            name = userQR.getName();
        } else {
            Log.e("AddTransactionActivity", "friend / user is epmty");
            return;
        }

        tv_user_name.setText(name);

    }

    public void navigateToMain(View v) {
        Intent i = new Intent(AddTransactionActivity.this, MainActivity.class);
        startActivity(i);
    }
}
