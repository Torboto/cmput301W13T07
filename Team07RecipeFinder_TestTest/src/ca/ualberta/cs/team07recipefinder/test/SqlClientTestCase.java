package ca.ualberta.cs.team07recipefinder.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cs.team07recipefinder.Recipe;
import ca.ualberta.cs.team07recipefinder.SqlClient;

import java.lang.reflect.Method;
import java.util.ArrayList;

import android.content.Context;
import android.test.ServiceTestCase;
import junit.framework.TestCase;

/* Test class for the SqlClient class. Tests adding, updating, and deleting
 * Recipes from the local SqlDatabase
 */
public class SqlClientTestCase extends TestCase {
	private SqlClient testClient;
	Recipe testRecipe;
	String recipeId;
	
	@Before
	public void setUp() throws Exception {
		testClient = new SqlClient(getTestContext());
		
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("ingredient1");
		ingredients.add("ingredient2");
		
		testRecipe = new Recipe("Title", "Description", 
				ingredients, "Directions", "Email");
		
		recipeId = String.valueOf(testRecipe.getRecipeId());
	}
	
	// Test if the Client has been instantiated.
	public void testConnection() {
		assertTrue(testClient != null);
	}

	
	public void testWriteRecipe() {
		
		assertTrue(testClient != null);
	}

	private Context getTestContext() {
		try {
			Method getTestContext = ServiceTestCase.class
					.getMethod("getTestContext");
			return (Context) getTestContext.invoke(this);
		} catch (final Exception exception) {
			exception.printStackTrace();
			return null;
		}
	}
}