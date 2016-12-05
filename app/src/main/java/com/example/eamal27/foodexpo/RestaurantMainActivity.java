package com.example.eamal27.foodexpo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RestaurantMainActivity extends AppCompatActivity {
    final static int RESULT_LOAD_IMG = 1;

    public final static int requestAddNewFoodItem = 8700;
    public final static int requestEditFoodItem = 8800;
    public final static int returnAddSuccess = 8701;
	public final static int returnAddFailure = 8702;
    public final static int returnEditSuccess = 8801;
    public final static int returnEditFailure = 8802;

    private Restaurant loggedIn;
	private ArrayList<FoodItem> menu;
	private GridLayout selected;
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
		updateMenu();
		selected = null;
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

    public void uploadLogo(View view) {
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
                    ImageView imgView = (ImageView) findViewById(R.id.restaurantLogo);
                    // Set the Image in ImageView after decoding the String
                    imgView.setImageBitmap(BitmapFactory
                            .decodeFile(imgDecodableString));
                } else {
                    Toast.makeText(this, "You haven't picked an Image",
                            Toast.LENGTH_LONG).show();
                }
            }
			// When a new food item is added
			else if (requestCode == requestAddNewFoodItem){
                String displayText;
                if(resultCode == returnAddSuccess) {
                    displayText = getString(R.string.addItemSuccess);
                }else{
                    displayText = getString(R.string.addItemFailure);
                }
                Context context = getApplicationContext();
                int duration = Toast.LENGTH_SHORT;
                updateMenu();
                Toast.makeText(context, displayText, duration).show();
            }
			// When an item has been edited
			else if (requestCode == requestEditFoodItem){
				String displayText;
				if(resultCode == returnEditSuccess) {
					displayText = getString(R.string.editItemSuccess);
				}else{
					displayText = getString(R.string.editItemFailure);
				}
				Context context = getApplicationContext();
				int duration = Toast.LENGTH_SHORT;
				updateMenu();
				Toast.makeText(context, displayText, duration).show();
			}

        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show();
        }
    }

    public void editFoodItem(View view){

		if (selected!=null) {
			TextView getItemName = (TextView) selected.getChildAt(0);
			TextView getItemPrice = (TextView) selected.getChildAt(1);
			TextView getItemDescription = (TextView) selected.getChildAt(2);

			String itemName = getItemName.getText().toString();
			String itemPrice = getItemPrice.getText().toString();
			String itemDescription = getItemDescription.getText().toString();

			Intent editFoodItem = new Intent(this, EditFoodItemActivity.class);
			editFoodItem.putExtra("email", loggedIn.getEmail());
			editFoodItem.putExtra("requestCode",requestEditFoodItem);
			editFoodItem.putExtra("itemName",itemName);
			editFoodItem.putExtra("itemPrice",itemPrice);
			editFoodItem.putExtra("itemDescription",itemDescription);
			startActivityForResult(editFoodItem,requestEditFoodItem);
		}else{
			Toast.makeText(this, getString(R.string.please_select_item), Toast.LENGTH_SHORT).show();
		}
    }

    public void addFoodItem(View view){
        Intent addFoodItem = new Intent(this, EditFoodItemActivity.class);
        addFoodItem.putExtra("email", loggedIn.getEmail());
		addFoodItem.putExtra("requestCode", requestAddNewFoodItem);
        startActivityForResult(addFoodItem,requestAddNewFoodItem);
    }

	private void updateMenu(){
        menu = dbHelper.getMenu(loggedIn.getEmail());
        displayMenu();
	}

	private void displayMenu(){
        if (menu != null) {
            LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menuLayout);
            menuLayout.removeAllViews();
            for (int menuItem = 0; menuItem < menu.size(); menuItem++) {
                // TODO: get the image
                // TODO: fix the display to be nicer looking

                // Get the values to display for each menu item
                String name = menu.get(menuItem).getName();
                String price = Float.toString(menu.get(menuItem).getPrice());
                String description = menu.get(menuItem).getDescription();

                // Create a new TextView and add the correct text to each
                TextView displayName = new TextView(this);
                TextView displayPrice = new TextView(this);
                TextView displayDescription = new TextView(this);
                displayName.setText(name);
                displayPrice.setText(price);
                displayDescription.setText(description);

				// Set the correct layout params for each TextView to line them up nicely
				GridLayout.LayoutParams displayNameParams = new GridLayout.LayoutParams();
				GridLayout.LayoutParams displayPriceParams = new GridLayout.LayoutParams();
				GridLayout.LayoutParams displayDescriptionParams = new GridLayout.LayoutParams();

                // Add the TextViews to a new Relative Layout
                final GridLayout displayItem = new GridLayout(this);

				displayItem.setClickable(true);
				// Add the onclick functionality to each display item
				displayItem.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view){
						if (selected!=null){
							selected.setBackgroundColor(Color.WHITE);
						}
						selected = displayItem;
						selected.setBackgroundColor(Color.LTGRAY);
					}
				});
                displayItem.addView(displayName, displayNameParams);
                displayItem.addView(displayPrice, displayPriceParams);
                displayItem.addView(displayDescription, displayDescriptionParams);

                // Add the new Relative Layout into the scrollview
                menuLayout.addView(displayItem);
            }
        }
	}
}



