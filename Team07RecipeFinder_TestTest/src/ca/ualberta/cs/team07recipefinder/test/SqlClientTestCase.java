package ca.ualberta.cs.team07recipefinder.test;

import org.junit.Before;
import org.junit.Test;
import ca.ualberta.cs.team07recipefinder.Recipe;
import ca.ualberta.cs.team07recipefinder.SqlClient;
import java.util.ArrayList;
import java.util.UUID;
import android.test.AndroidTestCase;

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

		testRecipe = new Recipe("Title", "Description", ingredients,
				"Directions", "Email", Recipe.Location.LOCAL);

		recipeId = String.valueOf(testRecipe.getRecipeId());
		uId = testRecipe.getRecipeId();
	}

	/*
	 * @Test public void testConnection() { testClient.deleteAllRows();
	 * assertTrue(testClient.checkRow(uId) == false);
	 * testClient.writeRecipe(testRecipe); assertTrue(testClient.checkRow(uId)
	 * == true); testClient.deleteAllRows(); }
	 */
	@Test
	public void testConnection() {
		assertTrue(testClient != null);
	}

	@Test
	public void testWriteDeleteRecipe() {
		testClient.deleteAllRows();
		assertTrue(testClient.checkRow(uId) == false);
		testClient.writeRecipe(testRecipe);
		assertTrue(testClient.checkRow(uId) == true);
		testClient.deleteRecipe(uId);
		assertTrue(testClient.checkRow(uId) == false);
		testClient.deleteAllRows();
	}

	@Test
	public void testReadRecipe() {
		testClient.deleteAllRows();
		Recipe tempRecipe;

		assertTrue(testClient.checkRow(uId) == false);
		testClient.writeRecipe(testRecipe);
		assertTrue(testClient.checkRow(uId) == true);

		tempRecipe = testClient.readRecipe(uId);

		assertEquals(tempRecipe.getTitle(), testRecipe.getTitle());
		assertEquals(tempRecipe.getDescription(), testRecipe.getDescription());
		assertEquals(tempRecipe.getIngredients().get(0), testRecipe
				.getIngredients().get(0));
		assertEquals(tempRecipe.getIngredients().get(1), testRecipe
				.getIngredients().get(1));
		assertEquals(String.valueOf(tempRecipe.getRecipeId()),
				String.valueOf(testRecipe.getRecipeId()));
		assertEquals(tempRecipe.getDirections(), testRecipe.getDirections());

		testClient.deleteRecipe(uId);
		assertTrue(testClient.checkRow(uId) == false);
		testClient.deleteAllRows();
	}

	@Test
	public void testUpdateRecipe() {
		testClient.deleteAllRows();
		assertTrue(testClient.checkRow(uId) == false);
		testClient.writeRecipe(testRecipe);
		assertTrue(testClient.checkRow(uId) == true);

		Recipe newRecipe = testRecipe;
		newRecipe.setDescription("Description2");
		newRecipe.setDirections("Directions2");
		newRecipe.setName("Title2");

		testClient.updateRecipe(uId, newRecipe);

		assertTrue(testClient.checkRow(uId) == true);

		Recipe tempRecipe = testClient.readRecipe(uId);

		assertTrue(tempRecipe.getTitle().equals(newRecipe.getTitle()));
		assertTrue(tempRecipe.getDescription().equals(
				newRecipe.getDescription()));
		assertTrue(tempRecipe.getIngredients().get(0)
				.equals(newRecipe.getIngredients().get(0)));
		assertTrue(tempRecipe.getIngredients().get(1)
				.equals(newRecipe.getIngredients().get(1)));
		assertTrue(String.valueOf(tempRecipe.getRecipeId()).equals(
				String.valueOf(newRecipe.getRecipeId())));
		assertTrue(tempRecipe.getDirections().equals(newRecipe.getDirections()));

		testClient.deleteRecipe(uId);
		assertTrue(testClient.checkRow(uId) == false);
		testClient.deleteAllRows();
	}

	@Test
	public void testGetAllRecipe() {
		testClient.deleteAllRows();
		ArrayList<Recipe> recipes = null;

		assertTrue(testClient.checkRow(uId) == false);
		recipes = testClient.getAllRecipes();
		assertTrue(recipes.size() == 0);
		testClient.writeRecipe(testRecipe);
		assertTrue(testClient.checkRow(uId) == true);
		recipes = testClient.getAllRecipes();
		assertTrue(recipes.size() == 1);
		testClient.deleteRecipe(uId);
		assertTrue(testClient.checkRow(uId) == false);
		testClient.deleteAllRows();
	}

}