package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }


    //Two login functions, one for the user and one for the restaurant
    public void userLogin(View view){
        Intent launchLogin = new Intent(this, LoginActivity.class);
        launchLogin.putExtra("loginType", "user");
        startActivity(launchLogin);
    }

    public void restaurantLogin(View view){
        Intent launchLogin = new Intent(this, LoginActivity.class);
        launchLogin.putExtra("loginType", "restaurant");
        startActivity(launchLogin);
    }

}
