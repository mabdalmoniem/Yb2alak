package com.example.mohamed.yb2alak.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.mohamed.yb2alak.LoginActivity;
import com.example.mohamed.yb2alak.api.mapping.user.models.User;

public class LocalStorageHelper {
    public static void setAuthUser(User user, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name",user.getName());
        editor.putString("email",user.getEmail());
        editor.putString("token",user.getToken());
        editor.putBoolean("isLoggedIn", true);
        editor.apply();
    }

    public static User getAuthUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String name = preferences.getString("name", "");
        String email = preferences.getString("email", "");
        String token = preferences.getString("token", "");
        User user = new User(name, email, token);
        return user;
    }


    public static boolean isLoggedIn(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean("isLoggedIn", false);
    }

    public static void destoryAuthUser(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        preferences.edit().remove("name").apply();
        preferences.edit().remove("email").apply();
        preferences.edit().remove("token").apply();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();
    }

    public static void logout(Context context) {
        destoryAuthUser(context);
        Intent i = new Intent(context, LoginActivity.class);
        context.startActivity(i);
    }
}
