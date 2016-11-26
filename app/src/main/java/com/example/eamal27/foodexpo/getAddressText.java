package com.example.eamal27.foodexpo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GetAddressText extends AppCompatActivity {

    public static final int getAddressMap = 3333;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address_text);
    }

    public void submitAddress(View view){

        Intent resultIntent = new Intent();

        // Get the values submitted
        EditText getAddressOne = (EditText)findViewById(R.id.edittext_addressone);
        EditText getAddressTwo = (EditText)findViewById(R.id.edittext_addresstwo);
        EditText getCity = (EditText)findViewById(R.id.edittext_city);
        EditText getProv = (EditText)findViewById(R.id.edittext_prov);
        EditText getCountry = (EditText)findViewById(R.id.edittext_country);
        EditText getPostal = (EditText)findViewById(R.id.edittext_postal);

        String addressOne = getAddressOne.getText().toString();
        String addressTwo = getAddressTwo.getText().toString();
        String city = getCity.getText().toString();
        String prov = getProv.getText().toString();
        String country = getCountry.getText().toString();
        String postal = getPostal.getText().toString();

        // Check that everything is entered
        // If something isn't entered pop up a little toast
        // telling them which is missing, focus on that if possible?

        Context context = getApplicationContext();
        String errorMessage="";
        int duration = Toast.LENGTH_SHORT;
        boolean showError = false;
        EditText toFocus = getAddressOne;

        if (addressOne.equals("")){
            errorMessage = "Please enter your address";
            showError=true;
            toFocus = getAddressOne;
        } else if (city.equals("")){
            errorMessage = "Please enter your city";
            showError=true;
            toFocus = getCity;
        } else if (prov.equals("")){
            errorMessage = "Please enter your province";
            showError=true;
            toFocus = getProv;
        } else if (country.equals("")){
            errorMessage = "Please enter your country";
            showError=true;
            toFocus = getCountry;
        } else if (postal.equals("")){
            errorMessage = "Please enter your postal code";
            showError=true;
            toFocus = getPostal;
        }


        // If an error was found we will display the toast
        // If no error was found we will put the items in the resultIntent extras and finish up here

        if (showError){
            Toast.makeText(context,errorMessage,duration).show();
            toFocus.requestFocus();
        } else {
            resultIntent.putExtra("addressOne",addressOne);
            resultIntent.putExtra("addressTwo",addressTwo);
            resultIntent.putExtra("city",city);
            resultIntent.putExtra("prov",prov);
            resultIntent.putExtra("country",country);
            resultIntent.putExtra("postal",postal);
            setResult(SignUpUserActivity.returnTextAddress, resultIntent);
            finish();
        }
    }

    public void getLocationFromMap(View view){
        Intent getLocation = new Intent(this, GetAddressMap.class);
        startActivityForResult(getLocation, getAddressMap);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result){
        super.onActivityResult(requestCode, resultCode, result);

    }
}
