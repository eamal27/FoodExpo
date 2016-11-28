package com.example.eamal27.foodexpo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 100484424 on 11/28/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodExpoDB";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String locationTable = "Location";
    private static final String usersTable = "Users";
    private static final String restaurantTable = "Restaurant";
    private static final String foodItemsTable = "FoodItems";

    // Column names

    private static final String idCol = "_id";
    private static final String firstNameCol = "firstName";
    private static final String lastNameCol = "lastName";
    private static final String emailCol = "email";
    private static final String passwordCol = "password";
    private static final String phoneCol = "phone";
    private static final String locationCol = "location";
    private static final String addressOneCol = "addressOne";
    private static final String addressTwoCol = "addressTwo";
    private static final String cityCol = "city";
    private static final String provCol = "prov";
    private static final String countryCol = "country";
    private static final String postalCol = "postal";
    private static final String nameCol = "name";
    private static final String businessIdCol = "businessId";
    private static final String restaurantCol = "restaurant";
    private static final String priceCol = "price";

    // Table Creation Statements

    private static final String locationTableCreate = "create table " + locationTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        addressOneCol + " TEXT NOT NULL, " +
                                                        addressTwoCol + " TEXT, " +
                                                        cityCol + " TEXT NOT NULL, " +
                                                        provCol + " TEXT, " +
                                                        countryCol + " TEXT NOT NULL, " +
                                                        postalCol + " TEXT NOT NULL)";

    private static final String usersTableCreate = "create table " + usersTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        firstNameCol + " TEXT NOT NULL, " +
                                                        lastNameCol + " TEXT NOT NULL, " +
                                                        emailCol + " TEXT NOT NULL, " +
                                                        passwordCol + " TEXT NOT NULL " +
                                                        phoneCol + " TEXT NOT NULL, " +
                                                        locationCol + " INTEGER NOT NULL, " +
                                                        "FOREIGN KEY (" + locationCol + ") REFERENCES " +
                                                                        locationTable + "(" + idCol + ")";

    private static final String restaurantTableCreate = "create table " + restaurantTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        nameCol + " TEXT NOT NULL, " +
                                                        businessIdCol + " TEXT NOT NULL, " +
                                                        emailCol + " TEXT NOT NULL, " +
                                                        passwordCol + " TEXT NOT NULL " +
                                                        phoneCol + " TEXT NOT NULL, " +
                                                        locationCol + " INTEGER NOT NULL, " +
                                                        "FOREIGN KEY (" + locationCol + ") REFERENCES " +
                                                                        locationTable + "(" + idCol + ")";

    private static final String foodItemsTableCreate = "create table " + foodItemsTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        nameCol + " TEXT NOT NULL, " +
                                                        priceCol + " TEXT NOT NULL, " +
                                                        restaurantCol + " INTEGER NOT NULL, " +
                                                        "FOREIGN KEY (" + restaurantCol + ") REFERENCES " +
                                                                        restaurantTable + "(" + idCol + ")";

    private static final String enableForeignKeys = "PRAGMA foreign_keys=ON";

    public DatabaseHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){

        // Create all the tables

        // Location has to go first, restaurant before foodItems
        enableForeignKeys(db);
        db.execSQL(locationTableCreate);
        db.execSQL(usersTableCreate);
        db.execSQL(restaurantTableCreate);
        db.execSQL(foodItemsTableCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){}

    private void enableForeignKeys(SQLiteDatabase database){
        database.execSQL(enableForeignKeys);
    }
}
