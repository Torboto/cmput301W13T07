package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;
import java.util.UUID;

public class RecipeController {

	private RecipeController() {
	}

	/*
	 * Writes to SQL local database, and if it has internet access also writes
	 * to HTTP.
	 */
	public static void writeRecipe(Recipe recipe) {
	}

	/*
	 * Only deletes from SQL local database
	 */
	public void deleteRecipe(UUID uuid) {
	}

	/*
	 * Write over old recipe with the same UUID
	 */
	public void updateRecipe(Recipe recipe) {
	}

	/*
	 * Only searches HTTP server
	 */
	public RecipeBook searchRecipeIngredients(ArrayList<String> ingredients) {
		return null;
	}

	/*
	 * Only searches HTTP server
	 */
	public RecipeBook searchRecipeTitles(String title) {
		return null;
	}
}
