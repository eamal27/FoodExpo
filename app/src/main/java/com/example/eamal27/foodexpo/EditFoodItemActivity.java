package com.example.eamal27.foodexpo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class EditFoodItemActivity extends AppCompatActivity {

	private static final int RESULT_LOAD_IMG = 1;
	DatabaseHelper dbHelper;
	String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_item);
		dbHelper = new DatabaseHelper(this);
		Intent data = getIntent();
		email = data.getStringExtra("email");
    }

    public void editImage(View view){
		Intent galleryIntent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// When an Image is picked
			if (requestCode == RESULT_LOAD_IMG) {
				if (resultCode == RESULT_OK && null != data) {
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
					ImageView imgView = (ImageView) findViewById(R.id.foodItemImage);
					// Set the Image in ImageView after decoding the String
					imgView.setImageBitmap(BitmapFactory
							.decodeFile(imgDecodableString));
				} else {
					Toast.makeText(this, "You haven't picked an Image",
							Toast.LENGTH_LONG).show();
				}
			}
		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
		}
	}

    public void submit(View view){

		//Check that all the fields are filled in
		if (checkPage()){

			Intent resultIntent = new Intent();
			// Add the item to the database

			EditText getItemName = (EditText)findViewById(R.id.getItemName);
			EditText getItemPrice = (EditText)findViewById(R.id.getItemPrice);
			EditText getDescription = (EditText)findViewById(R.id.getDescription);

			String itemName = getItemName.getText().toString();
			String itemPrice = getItemPrice.getText().toString();
			Float price = Float.parseFloat(itemPrice);
			String description = getDescription.getText().toString();

			FoodItem newItem = new FoodItem(itemName,price,description);

			String returnText;
			if (dbHelper.addFoodItem(newItem, email)!=-1){
				setResult(RestaurantMainActivity.returnAddSuccess);
			}else{
				setResult(RestaurantMainActivity.returnAddFailure);
			}
			finish();
		}
    }

	private boolean checkPage(){

		EditText getItemName = (EditText)findViewById(R.id.getItemName);
		EditText getItemPrice = (EditText)findViewById(R.id.getItemPrice);
		EditText getDescription = (EditText)findViewById(R.id.getDescription);

		String itemName = getItemName.getText().toString();
		String itemPrice = getItemPrice.getText().toString();
		String description = getDescription.getText().toString();

		Context context = getApplicationContext();
		String errorMessage="";
		int duration = Toast.LENGTH_SHORT;
		boolean showError = false;
		EditText toFocus = getItemName;

		if (itemName.equals("")){
			errorMessage = "Please enter a name for your item";
			showError=true;
			toFocus = getItemName;
		} else if (itemPrice.equals("")){
			errorMessage = "Please enter a price for your item";
			showError=true;
			toFocus = getItemPrice;
		} else if (description.equals("")) {
			errorMessage = "Please enter a description for your item";
			showError = true;
			toFocus = getDescription;
		}

		if (showError){
			Toast.makeText(context,errorMessage,duration).show();
			toFocus.requestFocus();
			return false;
		} else {
			return true;
		}
	}
}
