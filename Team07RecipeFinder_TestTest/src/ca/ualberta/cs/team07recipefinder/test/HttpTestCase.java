package ca.ualberta.cs.team07recipefinder.test;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.ualberta.cs.team07recipefinder.HttpClient;
import ca.ualberta.cs.team07recipefinder.Recipe;

/**
 * @author Torboto
 *
 *	Tests HttpClient functionality
 */
public class HttpTestCase extends TestCase {
	HttpClient httpClient;
	Recipe recipe;
	
	@BeforeClass 
    public static void setUpClass() {      
        System.out.println("HttpTestCase Setup");
    }
	
	/**
	 * Use the Before tag to notify the suite to run Setup before running tests
	 */
	@Before
	public void setUp(){
		httpClient = new HttpClient();

		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("fish");
		ingredients.add("cats");
		ArrayList<String> quantities = new ArrayList<String>();
		ingredients.add("unit");
		ingredients.add("unit");
		ArrayList<String> amounts = new ArrayList<String>();
		ingredients.add("2");
		ingredients.add("1");
		
		recipe = new Recipe("test", "test_desc", ingredients, "DIRECTINOS",
				"ern@bleh.com", Recipe.Location.SERVER);
		recipe.setQuantities(quantities);
		recipe.setUnits(amounts);
		
		httpClient.writeRecipe(recipe);
	}

	/**
	 * Things denoted with Test will be run automatically by the suite
	 */
	@Test
	public void testWrite() {
		Recipe output = httpClient.readRecipe(recipe.getRecipeId());
		assertTrue(output == recipe);
	}
	
	/**
	 * Testing search by keyword.
	 */
	@Test
	public void testSearch() {
		ArrayList<Recipe> output = httpClient.searchRecipes(recipe.getTitle());
		assertTrue(output.get(0) == recipe);
	}
	
	/**
	 * Test deleting and rewriting a recipe
	 */
	@Test
	public void testUpdate() {
		recipe.setDescription("Test");
		httpClient.updateRecipe(recipe);
		ArrayList<Recipe> output = httpClient.searchRecipes(recipe.getTitle());
		assertTrue(output.get(0).getDescription() == recipe.getDescription());
	}
}
