package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;

/**
 * A helper class for the Sqlite database that stores the user's locally
 * cached recipes. Recipes can be added to the database, deleted, and
 * updated.
 * @author gcoomber
 *
 */
public class SqlClient extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RecipeCache.db";
    public static final String TABLE_NAME = "LocalRecipes";
    private static final String COLUMN_NAME_ID = "recipe_id";
    private static final String COLUMN_NAME_CONTENT = "recipe_text";
	private Gson gson = new Gson();
    
    // GC: Statement for creating a table.
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + TABLE_NAME + " (" +
        COLUMN_NAME_ID + " VARCHAR PRIMARY KEY," + COLUMN_NAME_CONTENT 
        + " TEXT" + " )";
    
    // GC: Statement for deleting a table.
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;
    
    public SqlClient(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
	public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    
    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    
    @Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    
    /**
     * Write a Recipe to the local database (the cache).
     * @param recipe
     */
    public void writeRecipe(Recipe recipe) {
    	String json;
    	
    	// GC: Gets the data repository in write mode
    	SQLiteDatabase db = this.getWritableDatabase();
    	ContentValues values = new ContentValues();
    	
    	// GC: Convert Recipe data to json string.
    	json = gson.toJson(recipe);
    	
    	values.put(COLUMN_NAME_ID, String.valueOf(recipe.getRecipeId()));
    	values.put(COLUMN_NAME_CONTENT, json);
    	
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
   
    /**
     * Retrieve a recipe from the local database using recipeId as
     * the key.
     * @param id
     * @return
     */
    public Recipe readRecipe(UUID id) {
    	Recipe recipe;
    	String json;
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// we will use in the query
    	String[] projection = {
    	    COLUMN_NAME_ID,
    	    COLUMN_NAME_CONTENT
    	    };
    	
    	// Query the database for the rows and columns we want.
    	Cursor c = db.query(TABLE_NAME, projection, COLUMN_NAME_ID + "=?", 
    			new String[] {String.valueOf(id)}, null, null, null, null);
    			
    	if(c != null) {
    		c.moveToFirst();
    	}
    	
    	json = c.getString(1);
    	recipe = gson.fromJson(json, Recipe.class);
    	
    	db.close();
		c.close();
    	
    	// Return entry that has been read from database.
    	return recipe;
    }
    
    /**
     * Update an existing Recipe in the local database by overwriting 
     * the Recipe.
     * @param recipeId
     * @param temp_recipe
     */
    public void updateRecipe( UUID recipeId, Recipe temp_recipe ) {
    	String json;
    	boolean isExists;
    	Gson gson = new Gson();
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	json = gson.toJson( temp_recipe );
    	
    	// GC: Only query the database if the row exists.
    	isExists = checkRow( recipeId );
    	
		if (isExists == true) {
			// New value for one column
			ContentValues values = new ContentValues();

			values.put(COLUMN_NAME_ID, String.valueOf( recipeId ));
			values.put(COLUMN_NAME_CONTENT, json);

			// Which row to update, based on the ID
			String selection = COLUMN_NAME_ID + " LIKE ?";
			String[] selectionArgs = { String.valueOf( recipeId ) };

			db.update(TABLE_NAME, values, selection, selectionArgs);
		}
		
		db.close();
    }

    /**
     * Query the database to check if a Recipe exits with the recipeId.
     * Return true if row exists, false if row does not exist.
     * @param recipeId
     * @return
     */
    public boolean checkRow(UUID recipeId) {
		boolean isExist = false;
		SQLiteDatabase db = this.getReadableDatabase();

		/* GC: Define a projection that specifies which columns from the
		 *  database we will use in the query*/
		String[] projection = { COLUMN_NAME_ID };
		// Query the database for the rows and columns we want.
    	Cursor c = db.query(TABLE_NAME, projection, COLUMN_NAME_ID + "=?", 
    			new String[] {String.valueOf(recipeId)},
    			null, null, null, null);
    	
    	// GC: if the cursor not null, a rows exist with the given recipeId
    	if(c != null) {
    		isExist = true;
    	}
    	
    	db.close();
		c.close();
    	
		return isExist;
    }
    
    /**
     * Delete a Recipe from the local database, if the Recipe exists.
     * @param recipeId
     */
    public void deleteRecipe(UUID recipeId) {
    	boolean isExists = false;
    	isExists = checkRow(recipeId);
    	
    	SQLiteDatabase db = this.getReadableDatabase();
    	
		// GC: Only delete the recipe from the database if the row exists.
		if (isExists == true) {
			String selection = COLUMN_NAME_ID + " LIKE ?";
			String[] selectionArgs = { String.valueOf(recipeId) };

			// GC: Delete the row with recipeId from the database.
			db.delete(TABLE_NAME, selection, selectionArgs);
		}
		
		db.close();
    }
    
    /**
     * Find and return all recipes that are saved in the local database.
     * @return
     */
    public ArrayList<Recipe> getAllRecipes() {
    	String json;
    	Recipe tempRecipe = null;
    	
    	ArrayList<Recipe> recipeList = new ArrayList<Recipe>();
    	SQLiteDatabase db = this.getReadableDatabase();
    	
    	// GC: Query the database for all the rows.
    	Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    			
    	c.moveToFirst();

		// Add each id to the array
		while (!c.isAfterLast()) {
			json = c.getString(1);
	    	tempRecipe = gson.fromJson(json, Recipe.class);
			recipeList.add(tempRecipe);
			c.moveToNext();
		}
    	
		db.close();
		c.close();
		
    	return recipeList;
    }

}
