package com.example.eamal27.foodexpo;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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


    public void getLocation(View view){
        if (checkPage()) {
            Intent getAddress = new Intent(this, GetAddress.class);
            startActivityForResult(getAddress, getTextAddress);
        }
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

    // This function checks all the forms on the page and makes sure that they are all filled in
    // and that the passwords match each other
    private boolean checkPage(){
        EditText firstNameET = (EditText)findViewById(R.id.edittext_firstName);
        EditText lastNameET = (EditText)findViewById(R.id.edittext_lastName);
        EditText emailET = (EditText)findViewById(R.id.edittext_email);
        EditText phoneET = (EditText)findViewById(R.id.edittext_phone);
        EditText passwordET = (EditText)findViewById(R.id.edittext_password);
        EditText passwordConfirmET = (EditText)findViewById(R.id.edittext_password_confirm);

        String firstName = firstNameET.getText().toString();
        String lastName = lastNameET.getText().toString();
        String email = emailET.getText().toString();
        String phone = phoneET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordConfirm = passwordConfirmET.getText().toString();

        Context context = getApplicationContext();
        String errorMessage="";
        int duration = Toast.LENGTH_SHORT;
        boolean showError = false;
        EditText toFocus = firstNameET;

        if (firstName.equals("")){
            errorMessage = "Please enter your first name";
            showError=true;
            toFocus = firstNameET;
        } else if (lastName.equals("")){
            errorMessage = "Please enter your last name";
            showError=true;
            toFocus = lastNameET;
        } else if (email.equals("")){
            errorMessage = "Please enter your email";
            showError=true;
            toFocus = emailET;
        } else if (phone.equals("")){
            errorMessage = "Please enter your phone number";
            showError=true;
            toFocus = phoneET;
        } else if (password.equals("")){
            errorMessage = "Please enter a passowrd";
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

    private void loginNewUser(){

        if (createNewUser()) {
            // Launch the user main intent with the new email address
            Intent loadUserMain = new Intent(this, UserMainActivity.class);
            EditText getEmail = (EditText) findViewById(R.id.edittext_email);
            String email = getEmail.getText().toString();
            loadUserMain.putExtra("email", email);
            loadUserMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loadUserMain);
        }
    }

    private boolean createNewUser(){
        return true;
    }
}
