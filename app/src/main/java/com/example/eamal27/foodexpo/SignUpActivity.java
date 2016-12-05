package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignUpActivity extends AppCompatActivity {
    static final int USER_SIGNUP_REQUEST = 1001;
    static final int RESTAURANT_SIGNUP_REQUEST = 1002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }


    public void userSignup(View view) {
        Intent launchSignup = new Intent(this, SignUpUserActivity.class);
        startActivityForResult(launchSignup, USER_SIGNUP_REQUEST);
    }

    public void restaurantSignup(View view) {
        Intent launchSignup = new Intent(this, SignUpRestaurantActivity.class);
        startActivityForResult(launchSignup, RESTAURANT_SIGNUP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}

