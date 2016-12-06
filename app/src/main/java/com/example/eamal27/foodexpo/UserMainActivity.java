package com.example.eamal27.foodexpo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserMainActivity extends AppCompatActivity {
    public final static int UPDATE_RADIUS_REQUEST = 4444;

    private ArrayList<FoodItem> al;
    private ItemsAdapter arrayAdapter;
    private int i;
    Button leftButton, rightButton;
    DatabaseHelper dbHelper;
    private User loggedIn;
    private int radius;
    private String email;

    @InjectView(R.id.frame) SwipeFlingAdapterView flingContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        ButterKnife.inject(this);

        Intent data = getIntent();
        email = data.getStringExtra("email");
        dbHelper = new DatabaseHelper(this);
        loggedIn = dbHelper.getUserInfo(email);
        radius = dbHelper.getUserRadius(email);


        leftButton = (Button) findViewById(R.id.left);
        rightButton = (Button) findViewById(R.id.right);

        al = new ArrayList<FoodItem>();

        loadCards();

        // create dummy food items
//        FoodItem fi = new FoodItem("Burrito",8.99f,"Best Burrito Ever!");
//        FoodItem fi2 = new FoodItem("Pizza",12.99f,"Large Pepperoni Pizza!");
//        al.add(fi);
//        al.add(fi2);

        arrayAdapter = new ItemsAdapter(this, al);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
//                makeToast(UserMainActivity.this, "Left!");
            }

            @Override
            public void onRightCardExit(Object dataObject) {

                AlertDialog.Builder confirmDialog = new AlertDialog.Builder(
                        UserMainActivity.this);
                LayoutInflater factory = LayoutInflater.from(UserMainActivity.this);
                View view = factory.inflate(R.layout.order_confirmation, null);

                confirmDialog.setView(view);
                confirmDialog.setPositiveButton("Order!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        makeToast(UserMainActivity.this, "Food Item Ordered");
                    }
                });
                confirmDialog.setNegativeButton("Cancel", null);

                confirmDialog.show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", itemsInAdapter+" items");

                // disable/enable buttons
                if (itemsInAdapter == 0) {
                    leftButton.setEnabled(false);
                    rightButton.setEnabled(false);
                } else {
                    if(!leftButton.isEnabled()) leftButton.setEnabled(true);
                    if(!rightButton.isEnabled()) rightButton.setEnabled(true);
                }
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
            Intent intent = new Intent(UserMainActivity.this, FoodItemActivity.class);
            intent.putExtra("name", al.get(itemPosition).getName());
            intent.putExtra("price", al.get(itemPosition).getPrice());
            intent.putExtra("description", al.get(itemPosition).getDescription());
            intent.putExtra("restaurantName", al.get(itemPosition).getRestaurantName());
            startActivity(intent);
            }
        });

    }

    static void makeToast(Context ctx, String s){
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.right)
    public void right() {
        /**
         * Trigger the right event manually.
         */
        flingContainer.getTopCardListener().selectRight();
    }

    @OnClick(R.id.left)
    public void left() {
        flingContainer.getTopCardListener().selectLeft();
    }


    // Menu setup

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_preferences:
                Intent intent = new Intent(this, PreferencesActivity.class);
                intent.putExtra("email", loggedIn.getEmail());
                startActivityForResult(intent, UPDATE_RADIUS_REQUEST);
                return true;
            case R.id.menu_item_profile:
                Intent viewProfileIntent = new Intent(this, UserProfile.class);
                viewProfileIntent.putExtra("email", loggedIn.getEmail());
                startActivity(viewProfileIntent);
                return true;
            case R.id.menu_item_logout:
                // logout and go back to LoginActivity
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_RADIUS_REQUEST) {
            if (resultCode == RESULT_OK && null != data) {
                radius = data.getExtras().getInt("radius");
                Log.i("radius",radius+"");
                loadCards();
                arrayAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Unable to store radius in database", Toast.LENGTH_LONG)
                        .show();
            }
        }
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

    private ArrayList<Restaurant> getRestaurants(Address address){
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        restaurants = dbHelper.getRestaurantsWithinRadius(address, radius);

        TextView resultsText = (TextView)findViewById(R.id.txtNumberOfRestaurants);
        resultsText.setText("Results from " + restaurants.size() + " restaurants");

        return restaurants;
    }

//    private void loadCards(ArrayList<Restaurant> restaurants){
    private void loadCards(){
        ArrayList<Restaurant> restaurants = getRestaurants(loggedIn.getAddress());
        ArrayList<FoodItem> foodItems = new ArrayList<FoodItem>();

        for (int j = 0; j < restaurants.size(); j++) {
            Restaurant restaurant = restaurants.get(j);
            ArrayList<FoodItem> items = dbHelper.getMenu(restaurant.getEmail());

            for (int k = 0; k < items.size(); k++) {
                items.get(k).setRestaurantName(restaurant.getName());
            }

            foodItems.addAll(items);
        }

        al.addAll(foodItems);
    }

    public class ItemsAdapter extends ArrayAdapter<FoodItem> {
        public ItemsAdapter(Context context, ArrayList<FoodItem> foodItems) {
            super(context, 0, foodItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            FoodItem foodItem = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            }
            // Lookup view for data population
            TextView price = (TextView) convertView.findViewById(R.id.foodItemPrice);
//            TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome);
            // Populate the data into the template view using the data object
            price.setText(foodItem.getPrice()+"");
//            tvHome.setText(user.hometown);
            // Return the completed view to render on screen
            return convertView;
        }
    }


}
