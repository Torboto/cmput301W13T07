package ca.ualberta.cs.team07recipefinder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaohuim
 * 
 *         This is the Pantry class has only one variable, ingredients, which is
 *         a array list of strings.
 */
@SuppressWarnings("serial")
public class Pantry implements Serializable {
	ArrayList<String> ingredients;

	/**
	 * Constructor to take in a list of ingredients.
	 */
	public Pantry() {
		this.ingredients = new ArrayList<String>();
	}

	/**
	 * Takes in the new ingredient and appends it to the current ArrayList of
	 * ingredients.
	 * 
	 * @param ingredient
	 */
	public void addIngredient(String ingredient) {

		this.ingredients.add(ingredient);
	}

	/**
	 * When user selects a set of ingredients the list of indexes is passed to
	 * this function which then removes ingredients at those indexes.
	 * 
	 * @param ingredientIndexes
	 */
	public void removeIngredients(List<Integer> ingredientIndexes) {

		for (int i = 0; i < ingredientIndexes.size(); i++) {
			this.ingredients.set(ingredientIndexes.get(i), null);
		}
		removeNullIngredients();
	}

	/**
	 * This method will remove all ingredients that is null.
	 * 
	 */
	public void removeNullIngredients() {

		for (int i = 0; i < this.ingredients.size(); i++)
			if (this.ingredients.get(i) == null) {
				this.removeIngredient(i);
				i--;
			}
	}

	/**
	 * This function will remove ingredient at the given index.
	 * 
	 * @param index
	 */
	public void removeIngredient(int index) {

		this.ingredients.remove(index);
	}

	/**
	 * Returns all ingredients as an array list of strings.
	 * 
	 * @return All Ingredients
	 */
	public ArrayList<String> getAllIngredients() {

		return this.ingredients;
	}

	/**
	 * Returns the ingredient at the given index as a string.
	 * 
	 * @param index
	 * @return ingredient
	 */
	public String getIngredient(int index) {

		return this.ingredients.get(index);
	}

	/**
	 * Returns the ingredient at the given index as a string.
	 * 
	 * @param index
	 * 		Location of ingredient in the array.
	 * @param ingredient
	 * 		Updated ingredient information.
	 */
	public void updateIngredient(int index, String ingredient) {

		this.ingredients.set(index, ingredient);
	}

	/**
	 * When user selects a set of ingredients the list of indexes is passed to
	 * this function which then returns ingredients as strings at those indexes.
	 * 
	 * @param ingredientIndexes
	 *            List of indexes of selected ingredients that needs to be
	 *            returned to search function.
	 * @return Selected Ingredients
	 */
	public ArrayList<String> getSelectedIngedients(
			List<Integer> ingredientIndexes) {

		ArrayList<String> selecedIngredients = new ArrayList<String>();
		for (int i = 0; i < ingredientIndexes.size(); i++) {
			selecedIngredients.add(ingredients.get(ingredientIndexes.get(i)));
		}
		return selecedIngredients;
	}

	/**
	 * Set Ingredients member variable to empty.
	 */
	public void clearPantry() {

		this.ingredients.clear();
	}

}
