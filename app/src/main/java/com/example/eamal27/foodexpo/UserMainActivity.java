package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

public class UserMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        Location currentLocation = getLocation();
    }

    private Location getLocation(){
        LocationManager locationManager;
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        String gpsProvider = LocationManager.GPS_PROVIDER;

        //if location isn't enabled then ask the user to enable it
        if(!locationManager.isProviderEnabled(gpsProvider)){
            String locConfig = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
            Intent enableGPS = new Intent(locConfig);
            startActivity(enableGPS);
        }

        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationCriteria.setPowerRequirement(Criteria.POWER_HIGH);
        locationCriteria.setAltitudeRequired(false);
        locationCriteria.setBearingRequired(true);
        locationCriteria.setSpeedRequired(true);
        locationCriteria.setCostAllowed(false);
        String recommended = locationManager.getBestProvider(locationCriteria,true);
        Location currentLocation = new Location(recommended);

        try {
            currentLocation = locationManager.getLastKnownLocation(recommended);
        }catch(SecurityException e){

        }

        return currentLocation;
    }

    private ArrayList<Restaurant> getRestaurants(Location location){
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

        return restaurants;
    }

    private void displayItem(FoodItem item){

    }
}
