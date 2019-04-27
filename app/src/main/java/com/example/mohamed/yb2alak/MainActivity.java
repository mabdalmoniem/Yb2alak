package com.example.mohamed.yb2alak;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mohamed.yb2alak.api.mapping.user.models.User;
import com.example.mohamed.yb2alak.helper.LocalStorageHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    LinearLayout ll_view_friends;
    LinearLayout ll_add_qr_transaction;
    LinearLayout ll_view_transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isLoggedIn = LocalStorageHelper.isLoggedIn(MainActivity.this);
        User user = LocalStorageHelper.getAuthUser(MainActivity.this);
        if (user.getToken().isEmpty() || user.getToken() == null || user.getToken().length() == 0 || !isLoggedIn) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            return;
        }

        ll_view_friends = findViewById(R.id.ll_view_friends);
        ll_view_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ViewFriendsActivity.class);
                startActivity(i);
            }
        });

        ll_add_qr_transaction = findViewById(R.id.ll_add_qr_transaction);
        ll_add_qr_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, QRTransactionActivity.class);
                startActivity(i);
            }
        });

        ll_view_transactions = findViewById(R.id.ll_view_transactions);
        ll_view_transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String token = task.getResult().getToken();
                        Log.e("token =>>", token);
                    }
                });

    }

    @Override
    public void onResume()
    {
        super.onResume();
        boolean isLoggedIn = LocalStorageHelper.isLoggedIn(MainActivity.this);
        if (!isLoggedIn) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            LocalStorageHelper.logout(MainActivity.this);
        } else if (id == R.id.profile) {
            Intent i = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
