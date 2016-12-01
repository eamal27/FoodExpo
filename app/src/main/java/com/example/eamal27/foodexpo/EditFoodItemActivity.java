package com.example.eamal27.foodexpo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditFoodItemActivity extends AppCompatActivity {

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
