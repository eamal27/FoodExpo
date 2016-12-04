package com.example.eamal27.foodexpo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class PreferencesActivity extends AppCompatActivity {

    int radius;
    public TextView radiusValueText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        radiusValueText = (TextView) findViewById(R.id.lbl_RadiusValue);
        // get radius value from db
        radiusValueText.setText(radius+"");


        final SeekBar sk=(SeekBar) findViewById(R.id.seekBar2);
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

    }

    public void save(View view) {

        // save preferences

        finish();
    }
}
