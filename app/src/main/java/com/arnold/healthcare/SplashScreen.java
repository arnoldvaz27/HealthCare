package com.arnold.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT=1000;
    private String username,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.red));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.red));
        setContentView(R.layout.splashscreen);

        SharedPreferences settingLayoutType = getSharedPreferences("login", MODE_PRIVATE);
        username = settingLayoutType.getString("username", null);
        password = settingLayoutType.getString("password", null);

        if(username == null || password == null ){
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashScreen.this,
                        Login.class);
                SplashScreen.this.startActivity(i);
                SplashScreen.this.finishAffinity();
            }, SPLASH_SCREEN_TIME_OUT);
        }
        else{
            new Handler().postDelayed(() -> {
                Intent i = new Intent(SplashScreen.this,
                        Home.class);
                SplashScreen.this.startActivity(i);
                SplashScreen.this.finishAffinity();
            }, SPLASH_SCREEN_TIME_OUT);
        }
    }
}