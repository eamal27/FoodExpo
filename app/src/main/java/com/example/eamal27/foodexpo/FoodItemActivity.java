package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class FoodItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setIcon(android.R.drawable.btn_star);

        Intent resultIntent = getIntent();
        String name = resultIntent.getExtras().getString("name");
        float price = resultIntent.getExtras().getFloat("price");
        String description = resultIntent.getExtras().getString("description");
        String restaurantName = resultIntent.getExtras().getString("restaurantName");

        TextView _name = (TextView)findViewById(R.id.foodItemDetails_name);
        TextView _price = (TextView)findViewById(R.id.foodItemDetails_price);
        TextView _description = (TextView)findViewById(R.id.foodItemDetails_description);
        TextView _restaurantName = (TextView)findViewById(R.id.foodItemDetails_restaurantName);

        _name.setText(name);
        _price.setText(String.valueOf(price));
        _description.setText(description);
        _restaurantName.setText(restaurantName);

    }

}
