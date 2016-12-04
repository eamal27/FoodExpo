package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends AppCompatActivity {
    final static int RESULT_LOAD_IMG = 1;

    private User loggedIn;
    DatabaseHelper dbHelper;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        dbHelper = new DatabaseHelper(this);
        Intent data = getIntent();
        email = data.getStringExtra("email");
        loggedIn = dbHelper.getUserInfo(email);
        displayUserInfo();
    }

    private void displayUserInfo() {
        TextView showUserName = (TextView)findViewById(R.id.userName);
        TextView showUserPhone = (TextView)findViewById(R.id.userPhone);
        TextView showUserEmail = (TextView)findViewById(R.id.userEmail);
        TextView showUserAddress = (TextView)findViewById(R.id.userAddress);
        TextView showUserCityProvCountry = (TextView)findViewById(R.id.userCityProvCountry);
        TextView showUserPostal = (TextView)findViewById(R.id.userPostal);

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

        showUserName.setText(loggedIn.getFirstName()+" "+loggedIn.getLastName());
        showUserPhone.setText(loggedIn.getPhone());
        showUserEmail.setText(loggedIn.getEmail());
        showUserAddress.setText(addressLineOne);
        showUserCityProvCountry.setText(addressLineTwo);
        showUserPostal.setText(address.getPostalCode());
    }

    public void changeImage(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.restaurantLogo);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked an Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
    }
}
