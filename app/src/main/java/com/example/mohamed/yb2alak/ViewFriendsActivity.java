package com.example.mohamed.yb2alak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.mohamed.yb2alak.Recyclers.AllFriendsRecycler;
import com.example.mohamed.yb2alak.api.Yb2alkApiClient;
import com.example.mohamed.yb2alak.api.Yb2alkApiInterface;
import com.example.mohamed.yb2alak.api.mapping.user.models.Friend;
import com.example.mohamed.yb2alak.api.mapping.user.responses.FriendsResponse;
import com.example.mohamed.yb2alak.api.mapping.user.models.User;
import com.example.mohamed.yb2alak.helper.LocalStorageHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewFriendsActivity extends AppCompatActivity {

    RecyclerView rv_friends;
    AllFriendsRecycler adapter;
    Yb2alkApiInterface yb2alkAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_friends);

        boolean isLoggedIn = LocalStorageHelper.isLoggedIn(ViewFriendsActivity.this);
        User user = LocalStorageHelper.getAuthUser(ViewFriendsActivity.this);
        if (user.getToken().isEmpty() || user.getToken() == null || user.getToken().length() == 0 || !isLoggedIn) {
            Intent i = new Intent(ViewFriendsActivity.this, LoginActivity.class);
            startActivity(i);
            return;
        }

        rv_friends = findViewById(R.id.rv_friends);

        yb2alkAPI = Yb2alkApiClient.getClient().create(Yb2alkApiInterface.class);

        Call<FriendsResponse> call = yb2alkAPI.getFriends("bearer " + user.getToken());
        call.enqueue(new Callback<FriendsResponse>() {
            @Override
            public void onResponse(Call<FriendsResponse> call, Response<FriendsResponse> response) {
                Log.e("res", response.body().toString());
                ArrayList<Friend> friends = response.body().getFriends();
                rv_friends.setLayoutManager(new LinearLayoutManager(ViewFriendsActivity.this));
                adapter = new AllFriendsRecycler(ViewFriendsActivity.this, friends);
//                adapter.setClickListener(this);
                rv_friends.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<FriendsResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        boolean isLoggedIn = LocalStorageHelper.isLoggedIn(ViewFriendsActivity.this);
        if (!isLoggedIn) {
            Intent i = new Intent(ViewFriendsActivity.this, LoginActivity.class);
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
            LocalStorageHelper.logout(ViewFriendsActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }
}
