package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Locale;

public class SignUpUserActivity extends AppCompatActivity {

    public static final int getTextAddress = 1111;
    public static final int returnTextAddress = 1112;
    public static final int cancelTextAddress = 1113;
    public static final int getMapAddresss = 2222;
    public static final int returnMapAddress = 2223;
    public static final int cancelMapAddress = 2224;

    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_user);
        Locale locale = getResources().getConfiguration().locale;
        address = new Address(locale);
    }

    public void submitUserRegistration(View view) {

    }

    public void getLocation(View view){
        Intent getAddress = new Intent(this, GetAddress.class);
        startActivityForResult(getAddress,getTextAddress);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result){
        super.onActivityResult(requestCode,resultCode,result);
        if (requestCode==getTextAddress){
            if (resultCode==returnTextAddress){
                String addressOne = result.getStringExtra("addressOne");
                String addressTwo = result.getStringExtra("addressTwo");
                String city = result.getStringExtra("city");
                String prov = result.getStringExtra("prov");
                String country = result.getStringExtra("country");
                String postal = result.getStringExtra("postal");

                address.setAddressLine(0,addressOne);
                address.setAddressLine(1,addressTwo);
                address.setLocality(city);
                address.setAdminArea(prov);
                address.setCountryName(country);
                address.setPostalCode(postal);

                loginNewUser();
            }
        }
    }

    private void loginNewUser(){


    }
    //Todo: at some point in the sign up user we should ask them for a delivery location which they can change later
}
