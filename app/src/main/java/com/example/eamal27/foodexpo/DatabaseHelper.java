package com.example.eamal27.foodexpo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "FoodExpoDB";
    private static final int DATABASE_VERSION = 3;

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
    private static final String descriptionCol = "description";
	private static final String latitudeCol = "latitude";
	private static final String longitudeCol = "longitude";
    private static final String radiusCol = "radius";

    // Table Creation Statements

    private static final String locationTableCreate = "create table " + locationTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        addressOneCol + " TEXT NOT NULL, " +
                                                        addressTwoCol + " TEXT, " +
                                                        cityCol + " TEXT NOT NULL, " +
                                                        provCol + " TEXT, " +
                                                        countryCol + " TEXT NOT NULL, " +
                                                        postalCol + " TEXT NOT NULL, " +
														latitudeCol + " REAL NOT NULL, " +
														longitudeCol + " REAL NOT NULL);";

    private static final String usersTableCreate = "create table " + usersTable + "(" +
                                                        idCol + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        firstNameCol + " TEXT NOT NULL, " +
                                                        lastNameCol + " TEXT NOT NULL, " +
                                                        emailCol + " TEXT NOT NULL, " +
                                                        passwordCol + " TEXT NOT NULL, " +
                                                        phoneCol + " TEXT NOT NULL, " +
                                                        locationCol + " INTEGER NOT NULL, " +
                                                        radiusCol + " INTEGER NOT NULL, " +
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
                                                        descriptionCol + " TEXT NOT NULL, " +
                                                        restaurantCol + " INTEGER NOT NULL, " +
                                                        "FOREIGN KEY (" + restaurantCol + ") REFERENCES " +
                                                                        restaurantTable + "(" + idCol + "));";

    private static final String enableForeignKeys = "PRAGMA foreign_keys=ON";

	// DB upgrade statements
	// Upgrade from version 1 to version 2
	// String upgradeDBOne = "ALTER TABLE " + foodItemsTable + " ADD COLUMN " + descriptionCol + " TEXT NOT NULL DEFAULT ''";

	// Upgrade from version 2 to version 3
	String upgradeDBTwoA = "ALTER TABLE " + locationTable + " ADD COLUMN " + latitudeCol + " REAL NOT NULL DEFAULT 0.0";
	String upgradeDBTwoB = "ALTER TABLE " + locationTable + " ADD COLUMN " + longitudeCol + " REAL NOT NULL DEFAULT 0.0";

    private Locale locale;

    DatabaseHelper(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
        locale = context.getResources().getConfiguration().locale;
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
        // If we run into errors fill this in
        // Drop old data (or move it over)
        // Recreate DB

	/*	if (oldVersion < 2){
			db.execSQL(upgradeDBOne);
		}
		*/
		if (oldVersion < 3){
			db.execSQL(upgradeDBTwoA);
			db.execSQL(upgradeDBTwoB);
		}
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
        values.put(radiusCol, 2);
        long returnVal = db.insert(usersTable, null, values);
        db.close();
        return returnVal;
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
        long returnVal = db.insert(restaurantTable, null, values);
        db.close();
        return returnVal;
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
		values.put(latitudeCol, address.getLatitude());
		values.put(longitudeCol, address.getLongitude());
        long returnVal = db.insert(locationTable,null,values);
        db.close();
        return returnVal;
    }

    // This function returns the id value of the address in the database, or -1 if it doesn't exist yet
    long checkLocation(Address address){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {idCol};

		// Ignoring the latitude and longitude
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
        db.close();
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
        db.close();
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
        db.close();
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
        db.close();
        return returnVal;
    }

    User getUserInfo(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {firstNameCol, lastNameCol, phoneCol, locationCol};
        String whereClause = emailCol + " = ? ";
        String[]whereArgs = {email};
        Cursor data = db.query(usersTable, columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();
        User user;
        if (data.getCount()!=0){
            String firstName = data.getString(0);
            String lastName = data.getString(1);
            String phone = data.getString(2);
            Long addressId = data.getLong(3);
            Address address = getAddress(addressId);
            user = new User(firstName, lastName, email, phone, address);
        } else {
            user = new User();
        }
        data.close();
        db.close();
        return user;
    }

    long updateUserRadius(int rad, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        enableForeignKeys(db);
        String whereClause = emailCol + " = ? ";
        String[]whereArgs = {email};
        ContentValues values = new ContentValues();
        values.put(radiusCol, rad);
        long returnVal = db.update(usersTable, values, whereClause, whereArgs);
        db.close();
        return returnVal;
    }

    int getUserRadius(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {radiusCol};
        String whereClause = emailCol + " = ? ";
        String[]whereArgs = {email};
        Cursor data = db.query(usersTable, columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();
        int radius;
        if (data.getCount()!=0){
            radius = data.getInt(0);
        } else {
            radius = -1;
        }
        data.close();
        db.close();
        return radius;
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
        db.close();
        return returnVal;
    }

    Restaurant getRestaurantInfo(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {nameCol, businessIdCol, phoneCol, locationCol};
        String whereClause = emailCol + " = ? ";
        String[]whereArgs = {email};
        Cursor data = db.query(restaurantTable, columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();
        Restaurant restaurant;
        if (data.getCount()!=0){
            String name = data.getString(0);
            String businessId = data.getString(1);
            String phone = data.getString(2);
            Long addressId = data.getLong(3);
            Address address = getAddress(addressId);
            restaurant = new Restaurant(name, businessId, email, phone, address);
        } else {
            restaurant = new Restaurant();
        }
        data.close();
        db.close();
        return restaurant;
    }

    // Returns an address object given its id value
    Address getAddress(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        enableForeignKeys(db);
        String[] columns = {addressOneCol, addressTwoCol, cityCol, provCol, countryCol, postalCol, latitudeCol, longitudeCol};
        String whereClause = idCol + " = ? ";
        String[] whereArgs = {Long.toString(id)};
        Cursor data = db.query(locationTable, columns, whereClause, whereArgs, null, null, null);
        data.moveToFirst();

        Address address = new Address(locale);
        if (data.getCount()!=0) {
            String addressOne = data.getString(0);
            String addressTwo = data.getString(1);
            String city = data.getString(2);
            String prov = data.getString(3);
            String country = data.getString(4);
            String postal = data.getString(5);
			float latitude = data.getFloat(6);
			float longitude = data.getFloat(7);

            address.setAddressLine(0,addressOne);
            address.setAddressLine(1,addressTwo);
            address.setLocality(city);
            address.setAdminArea(prov);
            address.setCountryName(country);
            address.setPostalCode(postal);
			address.setLatitude(latitude);
			address.setLongitude(longitude);
        }
        data.close();
        db.close();
        return address;
    }

	long addFoodItem(FoodItem item, String email){
		long restaurantId = checkRestaurantUsername(email);
		String itemName = item.getName();
		Float itemPrice = item.getPrice();
		String description = item.getDescription();

		SQLiteDatabase db = getWritableDatabase();
		enableForeignKeys(db);
		ContentValues values = new ContentValues();
		values.put(nameCol,itemName);
		values.put(priceCol,itemPrice.toString());
		values.put(descriptionCol,description);
		values.put(restaurantCol, restaurantId);
		long returnVal = db.insert(foodItemsTable, null, values);
		db.close();
		return returnVal;
	}

    int editFoodItem(FoodItem oldItem, FoodItem newItem, String email){
		long restaurantId = checkRestaurantUsername(email);
		SQLiteDatabase db = getWritableDatabase();
		enableForeignKeys(db);
		ContentValues values = new ContentValues();
		values.put(nameCol, newItem.getName());
		values.put(priceCol, newItem.getPrice());
		values.put(descriptionCol, newItem.getDescription());
		String whereClause = nameCol + " = ? AND " +
							priceCol + " = ? AND " +
							descriptionCol + " = ? AND " +
							restaurantCol + " = ? ";

		String[] whereArgs = {oldItem.getName(),
								Float.toString(oldItem.getPrice()),
								oldItem.getDescription(),
								Long.toString(restaurantId)};

		int returnVal = db.update(foodItemsTable,values, whereClause, whereArgs);
		db.close();
		return returnVal;
    }

	public ArrayList<FoodItem> getMenu(String email){

		ArrayList<FoodItem> menu = new ArrayList<FoodItem>();
		long restaurantId = checkRestaurantUsername(email);
		SQLiteDatabase db = getReadableDatabase();
		if (restaurantId!=-1){
			String[] columns = {nameCol, priceCol, descriptionCol};
			String whereClause = restaurantCol + " = ? ";
			String[] whereArgs = {Long.toString(restaurantId)};
			Cursor data = db.query(foodItemsTable,columns,whereClause,whereArgs,null,null,null);
			while (data.moveToNext()){
				String name = data.getString(0);
				Float price = Float.parseFloat(data.getString(1));
				String description = data.getString(2);
				FoodItem item = new FoodItem(name, price, description);
				menu.add(item);
			}
			data.close();
		}
		db.close();
		return menu;
	}

    private final String restaurantLocationsQuery = "SELECT " +
            restaurantTable+"."+emailCol+", "+
            locationTable+"."+latitudeCol+", "+
            locationTable+"."+longitudeCol +
            " FROM " + restaurantTable + " INNER JOIN " +
            locationTable +
            " ON "+restaurantTable+"."+locationCol + "="+locationTable+"."+idCol;

    public ArrayList<Restaurant> getRestaurantsWithinRadius(Address address, int radius) {
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        ArrayList<Integer> restaurantIDs = new ArrayList<Integer>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor data = db.rawQuery(restaurantLocationsQuery, null);

        Location userLoc = new Location("User Location");
        userLoc.setLatitude(address.getLatitude());
        userLoc.setLongitude(address.getLongitude());

        while (data.moveToNext()){
            Location restLoc = new Location("Restaurant");
            restLoc.setLatitude(data.getFloat(1));
            restLoc.setLongitude(data.getFloat(2));
            float distanceInMeters = userLoc.distanceTo(restLoc);
            if (distanceInMeters < (radius*1000)) {
                // if within radius, add restaurant
                Log.i("Restaurants in Radius:",data.getString(0));
                Restaurant restaurant = getRestaurantInfo(data.getString(0));
                restaurants.add(restaurant);
            }
        }
        data.close();
        db.close();

        return restaurants;
    }

}
