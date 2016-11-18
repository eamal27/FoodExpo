package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private String loginType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent getLoginType = getIntent();
        loginType = getLoginType.getStringExtra("loginType");
        String table;   //sets the table that we will query (ie. usersTable/restaurantsTable),
                        // or if we have all id's in the same table, then the clause value for that column

        if (loginType.equals("user")){
            //display the correct login type
            TextView displayLoginType = (TextView)findViewById(R.id.loginType);
            displayLoginType.setText(R.string.login_user);

            //display the correct go back message
            TextView goBack = (TextView)findViewById(R.id.go_back);
            goBack.setText(R.string.not_a_user);
        }
        else if (loginType.equals("restaurant")){
            //display the correct login type
            TextView displayLoginType = (TextView)findViewById(R.id.loginType);
            displayLoginType.setText(R.string.login_restaurant);

            //display the correct go back message
            TextView goBack = (TextView)findViewById(R.id.go_back);
            goBack.setText(R.string.not_a_restaurant);
        }
    }

    public void checkLogin(View view){

        if (loginType.equals("user")){

            //todo: validate user login
            //For now just send them on through!

            Intent successfulLogin = new Intent(this, UserMainActivity.class);
            startActivity(successfulLogin);

        }
        else if (loginType.equals("restaurant")){

            //todo: validate restaurant login
            //For now just send them on through!

            Intent successfulLogin = new Intent(this, RestaurantMainActivity.class);
            startActivity(successfulLogin);
        }

    }

    public void goBack(View view){
        finish();
    }
}
