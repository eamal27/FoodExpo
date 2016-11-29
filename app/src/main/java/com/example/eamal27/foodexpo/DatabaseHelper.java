package com.example.eamal27.foodexpo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;

class DatabaseHelper extends SQLiteOpenHelper {

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
                                                        postalCol + " TEXT NOT NULL);";

    private static final String usersTableCreate = "create table " + usersTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        firstNameCol + " TEXT NOT NULL, " +
                                                        lastNameCol + " TEXT NOT NULL, " +
                                                        emailCol + " TEXT NOT NULL, " +
                                                        passwordCol + " TEXT NOT NULL, " +
                                                        phoneCol + " TEXT NOT NULL, " +
                                                        locationCol + " INTEGER NOT NULL, " +
                                                        "FOREIGN KEY (" + locationCol + ") REFERENCES " +
                                                                        locationTable + "(" + idCol + "));";

    private static final String restaurantTableCreate = "create table " + restaurantTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        nameCol + " TEXT NOT NULL, " +
                                                        businessIdCol + " TEXT NOT NULL, " +
                                                        emailCol + " TEXT NOT NULL, " +
                                                        passwordCol + " TEXT NOT NULL, " +
                                                        phoneCol + " TEXT NOT NULL, " +
                                                        locationCol + " INTEGER NOT NULL, " +
                                                        "FOREIGN KEY (" + locationCol + ") REFERENCES " +
                                                                        locationTable + "(" + idCol + "));";

    private static final String foodItemsTableCreate = "create table " + foodItemsTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        nameCol + " TEXT NOT NULL, " +
                                                        priceCol + " TEXT NOT NULL, " +
                                                        restaurantCol + " INTEGER NOT NULL, " +
                                                        "FOREIGN KEY (" + restaurantCol + ") REFERENCES " +
                                                                        restaurantTable + "(" + idCol + "));";

    private static final String enableForeignKeys = "PRAGMA foreign_keys=ON";

    DatabaseHelper(Context context){
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //  If we run into errors fill this in
        // Drop old data (or move it over)
        // Recreate DB
    }

    private void enableForeignKeys(SQLiteDatabase database){
        database.execSQL(enableForeignKeys);
    }

    // Takes a user and a password and creates a new user
    // Password is needed since it's not saved in the user class (it's already unsecure enough >_>)
    // Returns the user id if successful, or -1 if not
    long addUser(User user, String password){
        long locationVal = checkLocation(user.getAddress());
        if (locationVal==-1){
            locationVal = addLocation(user.getAddress());
        }

        SQLiteDatabase db = this.getWritableDatabase();
        enableForeignKeys(db);
        ContentValues values = new ContentValues();
        values.put(firstNameCol, user.getFirstName());
        values.put(lastNameCol, user.getLastName());
        values.put(emailCol, user.getEmail());
        values.put(passwordCol, password);
        values.put(phoneCol, user.getPhone());
        values.put(locationCol, locationVal);
        return db.insert(usersTable, null, values);
    }

    // Takes a restaurant and a password and creates a new restaurant
    // Password is needed since it's not saved in the restaurant class
    // Returns the restaurant id if successful, or -1 if not
    long addRestaurant(Restaurant restaurant, String password){
        long locationVal = checkLocation(restaurant.getAddress());
        if (locationVal==-1){
            locationVal = addLocation(restaurant.getAddress());
        }

        SQLiteDatabase db = this.getWritableDatabase();
        enableForeignKeys(db);
        ContentValues values = new ContentValues();
        values.put(nameCol, restaurant.getName());
        values.put(businessIdCol, restaurant.getBusinessId());
        values.put(emailCol, restaurant.getEmail());
        values.put(passwordCol, password);
        values.put(phoneCol, restaurant.getPhone());
        values.put(locationCol, locationVal);
        return db.insert(restaurantTable, null, values);
    }

    // Insert a new location into the database and return the ID
    // Returns -1 if insert failed
    long addLocation(Address address){
        SQLiteDatabase db = this.getWritableDatabase();
        enableForeignKeys(db);
        ContentValues values = new ContentValues();
        values.put(addressOneCol, address.getAddressLine(0));
        values.put(addressTwoCol, address.getAddressLine(1));
        values.put(cityCol, address.getLocality());
        values.put(provCol, address.getAdminArea());
        values.put(countryCol, address.getCountryName());
        values.put(postalCol, address.getPostalCode());
        return db.insert(locationTable,null,values);
    }

    // This function returns the id value of the address in the database, or -1 if it doesn't exist yet
    long checkLocation(Address address){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {idCol};
        String whereClause = addressOneCol + " = ? AND " +
                             addressTwoCol + " = ? AND " +
                             cityCol + " = ? AND " +
                             provCol + " = ? AND " +
                             countryCol + " = ? AND " +
                             postalCol + " = ?" ;

        String[] whereArgs = {address.getAddressLine(0),
                               address.getAddressLine(1),
                               address.getLocality(),
                               address.getAdminArea(),
                               address.getCountryName(),
                               address.getPostalCode()};

        Cursor data = db.query(locationTable, columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();
        long returnVal = -1;
        if (data.getCount()!=0) {
            returnVal = data.getInt(0);
        }
        data.close();
        return returnVal;
    }

    // Checks if an email is registered already in the User table
    // Returns the id if found, -1 otherwise
    long checkUsername(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {idCol};
        String whereClause = emailCol + " = ? ";
        String[] whereArgs = {email};
        Cursor data = db.query(usersTable, columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();
        long returnVal = -1;
        if (data.getCount()!=0){
            returnVal = data.getInt(0);
        }
        data.close();
        return returnVal;
    }

    // Checks if an email is registered already in the Restaurant table
    // Returns the id if found, -1 otherwise
    long checkRestaurantUsername(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {idCol};
        String whereClause = emailCol + " = ? ";
        String[] whereArgs = {email};
        Cursor data = db.query(restaurantTable, columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();
        long returnVal = -1;
        if (data.getCount()!=0){
            returnVal = data.getInt(0);
        }
        data.close();
        return returnVal;
    }

    long checkUserPassword(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {idCol};
        String whereClause = emailCol + " = ? AND " + passwordCol + " = ? " ;
        String[] whereArgs = {email, password};
        Cursor data = db.query(usersTable,columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();
        long returnVal = -1;
        if (data.getCount()!=0){
            returnVal = data.getInt(0);
        }
        data.close();
        return returnVal;
    }

    long checkRestaurantPassword(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {idCol};
        String whereClause = emailCol + " = ? AND " + passwordCol + " = ? " ;
        String[] whereArgs = {email, password};
        Cursor data = db.query(restaurantTable,columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();
        long returnVal = -1;
        if (data.getCount()!=0){
            returnVal = data.getInt(0);
        }
        data.close();
        return returnVal;
    }
}
