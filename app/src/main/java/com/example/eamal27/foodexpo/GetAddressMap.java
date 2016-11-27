package com.example.eamal27.foodexpo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class GetAddressMap extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE = 7777;
    private GoogleMap mMap;
    FragmentManager fragManager;
    LocationManager locManager ;
    String gpsProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address_map);
        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gpsProvider = LocationManager.GPS_PROVIDER;
        fragManager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)fragManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map){
        mMap = map;
        LatLng location = getCurrentLocation();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    public LatLng getCurrentLocation(){


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE);
        }else{

            if (!locManager.isProviderEnabled(gpsProvider)) {
                String locConfig = Settings.ACTION_LOCATION_SOURCE_SETTINGS;
                Intent enableGPS = new Intent(locConfig);
                startActivity(enableGPS);
            }
            try{
                //Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1,this);

            }catch(SecurityException e){
                e.printStackTrace();
            }
        }

        // temporary jus to run the app
        LatLng location = new LatLng(37.0,-76.2);

        return location;
    }

    public void setLocation(View view){
        //TODO: take the values from the map location and pass them back

        finish();
    }


    @Override
    public void onLocationChanged(Location location){
        Log.d("LocationSample", "onLocationChanged(" + location + ")");


    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("LocationSample", "onProviderDisabled(" + provider + ")");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("LocationSample", "onProviderEnabled(" + provider + ")");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("LocationSample", "onStatusChanged(" + provider + ", " + status + ", extras)");
    }

}
