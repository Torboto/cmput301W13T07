package ca.ualberta.cs.team07recipefinder;

import java.util.ArrayList;

import android.widget.EditText;

/**
 * @author ajstarna
 * 
 * 			Contains methods used by NewRecipeActivity and ViewRecipeActivity
 * 			for altering and displaying Recipe information.
 *
 */
public class RecipeView {
	/**
	 * This method takes in three edit texts(the ingredient, the quantity,
	 * and the name) and adds the information to recipe.
	 * 
	 * @param ingredientET
	 *            the edit text with ingredient
	 * @param unitET
	 *            the edit text with unit of measurement
	 * @param quantityET
	 *            the edit text with quantity
	 * @param recipe
	 * 			  the recipe which gets updated
	 */
	public static void addIngredient(EditText ingredientET, EditText unitET,
			EditText quantityET, Recipe recipe) {
		String ingredient = ingredientET.getText().toString();
		String unit = unitET.getText().toString();
		String quantity = quantityET.getText().toString();

		ArrayList <String> ingredients = recipe.getIngredients();
		ArrayList <String> quantities = recipe.getQuantities();
		ArrayList <String> units = recipe.getUnits();
		
		ingredients.add(ingredient);
		units.add(unit);
		quantities.add(quantity);
		
		recipe.setIngredients(ingredients);
		recipe.setQuantities(quantities);
		recipe.setUnits(units);
	}
	
	/**
	 * This method takes in three edit texts for a new ingredient (the
	 * ingredient, the quantity, and the name) and an array index. It adds the
	 * information to currentRecipe at index.
	 * 
	 * @param ingredientET
	 *            the edit text with ingredient
	 * @param unitET
	 *            the edit text with unit of measurement
	 * @param quantityET
	 *            the edit text with quantity
	 * @param index
	 * 	          the position in the recipe's arrays which gets edited
	 * @param recipe
	 * 			  the recipe which gets edited
	 */
	public static void editIngredient(EditText ingredientET, EditText unitET,
			EditText quantityET, int index, Recipe recipe) {
		String ingredient = ingredientET.getText().toString();
		String unit = unitET.getText().toString();
		String quantity = quantityET.getText().toString();
		
		ArrayList <String> ingredients = recipe.getIngredients();
		ArrayList <String> quantities = recipe.getQuantities();
		ArrayList <String> units = recipe.getUnits();
		
		ingredients.set(index, ingredient);
		units.set(index, unit);
		quantities.set(index, quantity);
		
		recipe.setIngredients(ingredients);
		recipe.setQuantities(quantities);
		recipe.setUnits(units);
	}
	
	/**
	 * This method deletes the ingredient, quantity, and unit
	 * entry at index in each array list.
	 * 
	 * @param index
	 * 		      the position that gets deleted in recipe's arrays
	 * @param recipe
	 * 			  the recipe being updated
	 */
	public static void deleteIngredient(int index, Recipe recipe) {
		ArrayList <String> ingredients = recipe.getIngredients();
		ArrayList <String> quantities = recipe.getQuantities();
		ArrayList <String> units = recipe.getUnits();
		
		ingredients.remove(index);
		units.remove(index);
		quantities.remove(index);
		
		recipe.setIngredients(ingredients);
		recipe.setQuantities(quantities);
		recipe.setUnits(units);
	}
	
	/**
	 * This method combines the quantities, units, and ingredients of a given
	 * Recipe and returns them as an Array List.
	 * 
	 * @param recipe
	 * 			   The Recipe that;s information is used
	 * @return
	 * 		       The combined ArrayList
	 */
	public static ArrayList<String> formCombinedArray(Recipe recipe) {
		ArrayList<String> ingredients = recipe.getIngredients();
		ArrayList<String> quantities = recipe.getQuantities();
		ArrayList<String> units = recipe.getUnits();
		ArrayList<String> combined = new ArrayList<String>();

		for (int index = 0; index < ingredients.size(); index++) {
			combined.add(quantities.get(index) + " " + units.get(index) + "  "
					+ ingredients.get(index));
		}
		return combined;
	}
		
}
