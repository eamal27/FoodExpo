package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PreferencesActivity extends AppCompatActivity {

    int radius;
    public TextView radiusValueText;
    DatabaseHelper dbHelper;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        dbHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        radiusValueText = (TextView) findViewById(R.id.lbl_RadiusValue);
        // get radius value from db
        radius = dbHelper.getUserRadius(email);

        if (radius > 0) {
            radiusValueText.setText(radius + "");
        } else {
            Toast.makeText(this, "Database error: could not retrieve radius", Toast.LENGTH_LONG)
                    .show();
        }

        final SeekBar sk = (SeekBar) findViewById(R.id.seekBar2);
        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                radius = progress + 1;
                radiusValueText.setText(radius+"");
                // save radius value to db

            }

        });

        // set progress bar to current value
        sk.setProgress(radius-1);
    }

    public void save(View view) {

        // save preferences
        dbHelper.updateUserRadius(radius, email);
        finish();
    }
}
