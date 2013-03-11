package ca.ualberta.cs.team07recipefinder.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cs.team07recipefinder.Recipe;
import ca.ualberta.cs.team07recipefinder.SqlClient;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.ServiceTestCase;
import junit.framework.TestCase;

/* Test class for the SqlClient class. Tests adding, updating, and deleting
 * Recipes from the local SqlDatabase
 */
public class SqlClientTestCase extends AndroidTestCase {
	private SqlClient testClient;
	Recipe testRecipe;
	String recipeId;
	UUID uId;
	
	@Before
	public void setUp() throws Exception {
		testClient = new SqlClient(getContext());
		
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("ingredient1");
		ingredients.add("ingredient2");
		
		testRecipe = new Recipe("Title", "Description", 
				ingredients, "Directions", "Email");
		
		recipeId = String.valueOf(testRecipe.getRecipeId());
		uId = testRecipe.getRecipeId();
	}
	
	@Test
	public void testConnection() {
		assertTrue(testClient != null);
	}

	@Test
	public void testWriteRecipe() {
		assertTrue(testClient.checkRow(uId) == false);
		testClient.writeRecipe(testRecipe);
		assertTrue(testClient.checkRow(uId) == true);
	}
	
	@Test
	public void testReadRecipe() {
		Recipe tempRecipe;
		
		assertTrue(testClient.checkRow(uId) == true);
		
		tempRecipe = testClient.readRecipe(uId);
		
		assertTrue(tempRecipe.getName() == testRecipe.getName());
		assertTrue(tempRecipe.getDescription() == testRecipe.getDescription());
		assertTrue(tempRecipe.getIngredients().get(0) 
				== testRecipe.getIngredients().get(0));
		assertTrue(tempRecipe.getIngredients().get(1) 
				== testRecipe.getIngredients().get(1));
		assertTrue(String.valueOf(tempRecipe.getRecipeId()) ==
				String.valueOf(testRecipe.getRecipeId()));
		assertTrue(tempRecipe.getDirections() == testRecipe.getDirections());
	}
	
	@Test
	public void testDeleteRecipe() {
		assertTrue(testClient.checkRow(uId) == true);
		testClient.deleteRecipe(uId);
		assertTrue(testClient.checkRow(uId) == false);
	}
	
	@Test
	public void testUpdateRecipe() {
		assertTrue(testClient.checkRow(uId) == false);
		testClient.writeRecipe(testRecipe);
		assertTrue(testClient.checkRow(uId) == true);
		
		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("ingredient1");
		ingredients.add("ingredient2");
		
		Recipe newRecipe = new Recipe("Title2", "Description2", 
				ingredients, "Directions2", "Email2");
		
		testClient.updateRecipe(uId, testRecipe);
		
		assertTrue(testClient.checkRow(uId) == true);
		
		Recipe tempRecipe = testClient.readRecipe(uId);
		
		assertTrue(tempRecipe.getName() == newRecipe.getName());
		assertTrue(tempRecipe.getDescription() == newRecipe.getDescription());
		assertTrue(tempRecipe.getIngredients().get(0) 
				== newRecipe.getIngredients().get(0));
		assertTrue(tempRecipe.getIngredients().get(1) 
				== newRecipe.getIngredients().get(1));
		assertTrue(String.valueOf(tempRecipe.getRecipeId()) ==
				String.valueOf(newRecipe.getRecipeId()));
		assertTrue(tempRecipe.getDirections() == newRecipe.getDirections());
		
		testClient.deleteRecipe(uId);
		assertTrue(testClient.checkRow(uId) == false);	
	}
	
	@Test
	public void testGetAllRecipe() {
		ArrayList<Recipe> recipes = null;
		
		assertTrue(testClient.checkRow(uId) == false);
		recipes = testClient.getAllRecipes();
		assertTrue(recipes == null);
		
		testClient.writeRecipe(testRecipe);
		assertTrue(testClient.checkRow(uId) == true);
		recipes = testClient.getAllRecipes();
		assertTrue(recipes != null);
		assertTrue(recipes.size() == 1);
	}
}