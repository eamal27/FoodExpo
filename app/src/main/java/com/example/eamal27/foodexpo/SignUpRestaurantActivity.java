package com.example.eamal27.foodexpo;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SignUpRestaurantActivity extends AppCompatActivity {

    private Address address;
	private Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_restaurant);
        Locale locale = getResources().getConfiguration().locale;
        address = new Address(locale);
		geocoder = new Geocoder(this, locale);
    }

    public void getLocation(View view){
        if (checkPage()) {
            Intent getAddress = new Intent(this, GetAddress.class);
            startActivityForResult(getAddress, GetAddress.getTextAddress);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (requestCode == GetAddress.getTextAddress) {
            if (resultCode == GetAddress.returnTextAddress) {
                String addressOne = result.getStringExtra("addressOne");
                String addressTwo = result.getStringExtra("addressTwo");
                String city = result.getStringExtra("city");
                String prov = result.getStringExtra("prov");
                String country = result.getStringExtra("country");
                String postal = result.getStringExtra("postal");

                address.setAddressLine(0, addressOne);
                address.setAddressLine(1, addressTwo);
                address.setLocality(city);
                address.setAdminArea(prov);
                address.setCountryName(country);
                address.setPostalCode(postal);
				address.setLatitude(0.0);
				address.setLongitude(0.0);

                // Use a Geocoder to get the longitude and latitude
				try{
					List<Address> getCoords = geocoder.getFromLocationName(addressOne, 1);
					if (getCoords.size()!=0){
						Address getCoordsAddress = getCoords.get(0);
						address.setLatitude(getCoordsAddress.getLatitude());
						address.setLongitude(getCoordsAddress.getLongitude());
					}
				}catch(IOException e) {
					e.printStackTrace();
				}

                EditText restaurantNameET = (EditText)findViewById(R.id.edittext_restaurantName);
                EditText businessIdET = (EditText)findViewById(R.id.edittext_businessId);
                EditText emailET = (EditText)findViewById(R.id.edittext_email);
                EditText phoneET = (EditText)findViewById(R.id.edittext_phone);
                EditText passwordET = (EditText)findViewById(R.id.edittext_password);

                String restaurantName = restaurantNameET.getText().toString();
                String businessId = businessIdET.getText().toString();
                String email = emailET.getText().toString();
                String phone = phoneET.getText().toString();
                String password = passwordET.getText().toString();

                Restaurant newRestaurant = new Restaurant(restaurantName,businessId,email,phone,address);
                createNewRestaurant(newRestaurant, password);
            }
        }
    }

    // This function checks all the forms on the page and makes sure that they are all filled in
    // and that the passwords match each other
    private boolean checkPage(){
        EditText restaurantNameET = (EditText)findViewById(R.id.edittext_restaurantName);
        EditText businessIdET = (EditText)findViewById(R.id.edittext_businessId);
        EditText emailET = (EditText)findViewById(R.id.edittext_email);
        EditText phoneET = (EditText)findViewById(R.id.edittext_phone);
        EditText passwordET = (EditText)findViewById(R.id.edittext_password);
        EditText passwordConfirmET = (EditText)findViewById(R.id.edittext_password_confirm);

        String restaurantName = restaurantNameET.getText().toString();
        String businessId = businessIdET.getText().toString();
        String email = emailET.getText().toString();
        String phone = phoneET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passwordConfirmET.getText().toString();

        Context context = getApplicationContext();
        String errorMessage="";
        int duration = Toast.LENGTH_SHORT;
        boolean showError = false;
        EditText toFocus = restaurantNameET;

        if (restaurantName.equals("")){
            errorMessage = "Please enter the name of your Restaurant";
            showError=true;
            toFocus = restaurantNameET;
        } else if (businessId.equals("")){
            errorMessage = "Please enter a Business ID";
            showError=true;
            toFocus = businessIdET;
        } else if (email.equals("")){
            errorMessage = "Please enter your email";
            showError=true;
            toFocus = emailET;
        } else if (phone.equals("")){
            errorMessage = "Please enter your phone number";
            showError=true;
            toFocus = phoneET;
        } else if (password.equals("")){
            errorMessage = "Please enter a password";
            showError=true;
            toFocus = passwordET;
        } else if (!password.equals(passwordConfirm)){
            errorMessage = "Passwords do not match";
            showError=true;
            toFocus = passwordET;
        }
        // If an error was found we will display the toast and return false
        // If no error was found we will return true

        if (showError){
            Toast.makeText(context,errorMessage,duration).show();
            toFocus.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    private void createNewRestaurant(Restaurant restaurant, String password){
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        if (dbHelper.addRestaurant(restaurant, password)!=-1){
            loginNewRestaurant(restaurant.getEmail(), password);
        }else{
            Log.i("Error", "Something went wrong during user creation");
        }
    }

    private void loginNewRestaurant(String email, String password){
        // Launch the restaurant main intent with the new email address
        Intent loadRestaurantMain = new Intent(this, RestaurantMainActivity.class);
        loadRestaurantMain.putExtra("email", email);
        loadRestaurantMain.putExtra("password",password);
        loadRestaurantMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loadRestaurantMain);
    }

}
