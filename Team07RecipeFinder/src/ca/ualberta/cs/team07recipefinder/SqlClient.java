package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/*
 * GC:
 * A helper class for the Sqlite database that stores the user's locally
 * cached recipes. Recipes can be added to the database, deleted, and
 * updated.
 */

public class SqlClient extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RecipeCache.db";
    public static final String TABLE_NAME = "LocalRecipes";
    private static final String COLUMN_NAME_ID = "recipe_id";
    private static final String COLUMN_NAME_TEXT = "recipe_text";
    
    // GC: Statement for creating a table.
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + TABLE_NAME + " (" +
        COLUMN_NAME_ID + " VARCHAR PRIMARY KEY," + COLUMN_NAME_TEXT 
        + " TEXT" + " )";
    
    // GC: Statement for deleting a table.
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    
    public SqlClient(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    // GC: Add recipe to database.
    public String add(Recipe recipe) {
    	String test_string = "";
    	
    	Gson gson = new Gson();
    	
    	// GC: Gets the data repository in write mode
    	SQLiteDatabase db = this.getWritableDatabase();
    	/*
    	// GC: Create a new map of values, where column names are the keys
    	ContentValues values = new ContentValues();
    	
    	values.put(COLUMN_NAME_ID, recipe.getRecipeId());
    	

        db.insert(TABLE_NAME, null, values);
        db.close(); */
    	String json = gson.toJson(recipe);
    	return json;
    }
/*    
    // GC: Get entry from database based on recipe ID.
    public CalorieEntry getEntry(int id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// we will use in the query
    	String[] projection = {
    	    COLUMN_NAME_ID,
    	    COLUMN_NAME_DESC,
    	    COLUMN_NAME_TOTCAL,
    	    COLUMN_NAME_CALPS,
    	    COLUMN_NAME_SERVS,
    	    COLUMN_NAME_NUMS,
    	    COLUMN_NAME_HOUR,
    	    COLUMN_NAME_DAY,
    	    COLUMN_NAME_MONTH,
    	    COLUMN_NAME_YEAR,
    	    COLUMN_NAME_CUSTOM
    	    };
    	
    	// Query the database for the rows and columns we want.
    	Cursor c = db.query(TABLE_NAME, projection, COLUMN_NAME_ID + "=?", 
    			new String[] {String.valueOf(id)}, null, null, null, null);
    			
    	if(c != null) {
    		c.moveToFirst();
    	}
    	
    	// Create a new time variable to pass to the new CalorieEntry
    	Time read_time = new Time(Integer.parseInt(c.getString(6)),
    			Integer.parseInt(c.getString(7)), Integer.parseInt(c.getString(8)),
    			Integer.parseInt(c.getString(9)));
    	
    	CalorieEntry read_entry = new CalorieEntry(Integer.parseInt(c.getString(0)),
    			c.getString(1), read_time, Double.parseDouble(c.getString(2)),
    			Double.parseDouble(c.getString(3)), Double.parseDouble(c.getString(4)),
    			Double.parseDouble(c.getString(5)), Boolean.parseBoolean(c.getString(10)));
    	// Return entry that has been read from database.
    	return read_entry;
    }
    
    // Edit an existing entry
    public void editEntry(CalorieEntry temp_entry, int id) {
    	SQLiteDatabase db = this.getReadableDatabase();

    	// New value for one column
    	ContentValues values = new ContentValues();
    	
    	values.put(COLUMN_NAME_ID, id);
    	values.put(COLUMN_NAME_DESC, temp_entry.getDesc());
    	values.put(COLUMN_NAME_TOTCAL, temp_entry.getTotalCal());
    	values.put(COLUMN_NAME_CALPS, temp_entry.getCalPerServ());
    	values.put(COLUMN_NAME_SERVS, temp_entry.getServSize());
    	values.put(COLUMN_NAME_NUMS, temp_entry.getNumServ());
    	values.put(COLUMN_NAME_HOUR, temp_entry.getETime().getHour());
    	values.put(COLUMN_NAME_DAY, temp_entry.getETime().getDay());
    	values.put(COLUMN_NAME_MONTH, temp_entry.getETime().getMonth());
    	values.put(COLUMN_NAME_YEAR, temp_entry.getETime().getYear());
    	values.put(COLUMN_NAME_CUSTOM, temp_entry.getisCustom());

    	// Which row to update, based on the ID
    	String selection = COLUMN_NAME_ID + " LIKE ?";
    	String[] selectionArgs = { String.valueOf(id) };

    	int count = db.update(
    	    TABLE_NAME,
    	    values,
    	    selection,
    	    selectionArgs);
    }
    
    // Delete an existing entry
    public void deleteEntry(int id) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	// Define 'where' part of query.
    	String selection = COLUMN_NAME_ID + " LIKE ?";
    	// Specify arguments for the where query.
    	String[] selectionArgs = { String.valueOf(id) };
    	// Issue SQL statement.
    	db.delete(TABLE_NAME, selection, selectionArgs);
    }
    
    // Find and return all the ids in the database
    public ArrayList<Integer> findAllID() {
    	ArrayList<Integer> id_array = new ArrayList<Integer>();
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	// Query the database for all the rows.
    	Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    			
    	c.moveToFirst();
    	
    	// Add each id to the array
    	while (!c.isAfterLast()) {
    	     id_array.add(Integer.parseInt(c.getString(0)));
    	     c.moveToNext();
    	}
    	
    	return id_array;
    }
    
    //Find the user's total calories over all entries.
    public double findAllCals() {
    	double tot_sum = 0;
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	// Query the database for the rows and columns we want.
    	Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    			
    	c.moveToFirst();
    	
    	
    	// Add up all the calorie values.
    	while (!c.isAfterLast()) {
    	     tot_sum += Double.parseDouble(c.getString(2));
    	     c.moveToNext();
    	}
    	
    	// Return entry that has been read from database.
    	return tot_sum;
    }
    
    // Find the user's total comsumption time by adding the number
    // distinct days in the database
    public double findTotTime() {
    	int num_days = 0;
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	// Get all distrinct days from the database.
    	Cursor c = db.rawQuery("SELECT DISTINCT " + COLUMN_NAME_DAY + " FROM " + TABLE_NAME, null);
    			
    	c.moveToFirst();
    	
    	// Add up all the calorie values.
    	while (!c.isAfterLast()) {
    	     num_days++;
    	     c.moveToNext();
    	}
    	
    	// Return entry that has been read from database.
    	return (double) num_days;
    }
    
    // Find the user's average calories consumed per day.
    public double findAveCals() {
    	double num_days;
    	double tot_cals;
    	
    	tot_cals = this.findAllCals();
    	num_days = this.findTotTime();
    	
    	// If the number of days is zero, set the number of days to 1
    	// in order to avoid dividing by zero.
    	if (num_days < 0.001)
    		num_days = 1;
    	
    	// Return entry that has been read from database.
    	return tot_cals/num_days;
    }
*/
}
