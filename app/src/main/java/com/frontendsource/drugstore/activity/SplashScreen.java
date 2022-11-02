package com.frontendsource.drugstore.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.frontendsource.drugstore.R;
import com.frontendsource.drugstore.model.User;
import com.frontendsource.drugstore.util.localstorage.LocalStorage;
import com.google.gson.Gson;

public class SplashScreen extends AppCompatActivity {
    Gson gson = new Gson();
    LocalStorage localStorage;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        localStorage = new LocalStorage(getApplicationContext());
        String userString = localStorage.getUserLogin();
        user = gson.fromJson(userString,User.class);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                 if(user==null){
                     Intent i = new Intent(getApplicationContext(), WelcomeActivity.class);
                     startActivity(i);
                     finish();
                     overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                 }else if(user!=null && user.getVerified()!=null &&  user.getVerified().equalsIgnoreCase("1")){
                     Intent i = new Intent(getApplicationContext(), MainActivity.class);
                     startActivity(i);
                     finish();
                     overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                 }else{
                     Intent i = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                     startActivity(i);
                     finish();
                     overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
                 }


            }


        }, 3000);
    }
}