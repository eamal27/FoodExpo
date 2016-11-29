package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class RestaurantMainActivity extends AppCompatActivity {

    private Restaurant loggedIn;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_main);
        Intent data = getIntent();
        String email = data.getStringExtra("email");
        dbHelper = new DatabaseHelper(this);
        loggedIn = dbHelper.getRestaurantInfo(email);
        displayRestaurantInfo();
    }

    private void displayRestaurantInfo(){
        TextView showRestaurantName = (TextView)findViewById(R.id.restaurantName);
        TextView showRestaurantPhone = (TextView)findViewById(R.id.restaurantPhone);
        TextView showRestaurantEmail = (TextView)findViewById(R.id.restaurantEmail);
        TextView showRestaurantAddress = (TextView)findViewById(R.id.restaurantAddress);
        TextView showRestaurantCityProvCountry = (TextView)findViewById(R.id.restaurantCityProvCountry);
        TextView showRestaurantPostal = (TextView)findViewById(R.id.restaurantPostal);

        showRestaurantName.setText(loggedIn.getName());
        showRestaurantPhone.setText(loggedIn.getPhone());
        showRestaurantEmail.setText(loggedIn.getEmail());

        // Put different parts of the address class together to put on the display
        Address address = loggedIn.getAddress();
        String addressLineOne = address.getAddressLine(0) + " " + address.getAddressLine(1);
        String city = address.getLocality() + ",";
        String prov;
        if (address.getAdminArea()!=null) {
            prov = address.getAdminArea() + ",";
        } else {
            prov = "";
        }
        String country = address.getCountryName();
        String addressLineTwo = city + prov + country;

        showRestaurantAddress.setText(addressLineOne);
        showRestaurantCityProvCountry.setText(addressLineTwo);
        showRestaurantPostal.setText(address.getPostalCode());
    }
}
