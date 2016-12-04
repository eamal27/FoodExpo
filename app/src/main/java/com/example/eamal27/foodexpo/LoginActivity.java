package com.example.eamal27.foodexpo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private String loginType;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DatabaseHelper(this);

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

        TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
        EditText enterEmail = (EditText)findViewById(R.id.getUsername);
        EditText enterPassword =(EditText)findViewById(R.id.getPassword);
        String email = enterEmail.getText().toString();
        String password = enterPassword.getText().toString();


        if (loginType.equals("user")){
            if (verifyUsername(email)){
                if(verifyPassword(email, password)) {
                    Intent successfulLogin = new Intent(this, UserMainActivity.class);
                    successfulLogin.putExtra("email",email);
                    successfulLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(successfulLogin);
                } else {
                    // Incorrect password
                    errorMessage.setText(R.string.incorrectPassword);
                }
            } else {
                //  E-mail not found
                errorMessage.setText(R.string.emailNotFound);
            }
        }
        else if (loginType.equals("restaurant")){
            if (verifyRestaurantUsername(email)){
                if(verifyRestaurantPassword(email, password)) {
                    Intent successfulLogin = new Intent(this, RestaurantMainActivity.class);
                    successfulLogin.putExtra("email",email);
                    successfulLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(successfulLogin);
                } else {
                    // Incorrect password
                    errorMessage.setText(R.string.incorrectPassword);
                }
            } else {
                //  E-mail not found
                errorMessage.setText(R.string.emailNotFound);
            }
        }
    }

    public void goBack(View view){
        finish();
    }

    // Returns true if the username is found
    private boolean verifyUsername(String email){
        if (dbHelper.checkUsername(email)==-1){
            return false;
        }else{
            return true;
        }
    }

    // Returns true if the username and password combination is found
    private boolean verifyPassword(String email, String password){
        if (dbHelper.checkUserPassword(email, password)==-1){
            return false;
        }else{
            return true;
        }
    }

    // Returns true if the username is found
    private boolean verifyRestaurantUsername(String email){
        if (dbHelper.checkRestaurantUsername(email)==-1){
            return false;
        }else{
            return true;
        }
    }

    // Returns true if the username and password combination is found
    private boolean verifyRestaurantPassword(String email, String password){
        if (dbHelper.checkRestaurantPassword(email, password)==-1){
            return false;
        }else{
            return true;
        }
    }
}
