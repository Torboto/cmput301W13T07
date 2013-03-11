package ca.ualberta.cs.team07recipefinder.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cs.team07recipefinder.HttpClient;
import ca.ualberta.cs.team07recipefinder.Recipe;

public class HttpTestCase extends TestCase {
	HttpClient httpClient;
	Recipe recipe;
	
	
	/**
	 * Use the Before tag to notify the suite to run Setup before running tests
	 */
	@Before
	public void setUp(){
		httpClient = new HttpClient();

		ArrayList<String> ingredients = new ArrayList<String>();
		ingredients.add("fish");
		ingredients.add("cats");
		recipe = new Recipe("test", "test_desc", ingredients, "DIRECTINOS",
				"ern@bleh.com");
	}

	/**
	 * Things denoted with Test will be run automatically by the suite
	 */
	@Test
	public void testWrite() {
		httpClient.writeRecipe(recipe);
		Recipe output = httpClient.readRecipe(recipe.getRecipeId());
		assertTrue(output == recipe);
	}
	
	@Test
	public void testSearch() {
		ArrayList<Recipe> output = httpClient.searchRecipes(recipe.getName(), null);
		assertTrue(output.get(0) == recipe);
	}
	
	public void testUpdate() {
		recipe.setDescription("Test");
		httpClient.updateRecipe(recipe);
		ArrayList<Recipe> output = httpClient.searchRecipes(recipe.getName(), null);
		assertTrue(output.get(0).getDescription() == recipe.getDescription());
	}
}
