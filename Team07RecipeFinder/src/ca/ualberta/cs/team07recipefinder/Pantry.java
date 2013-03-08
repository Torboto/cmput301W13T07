package ca.ualberta.cs.team07recipefinder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 * MA
 */
public class Pantry implements Serializable {
	ArrayList<String> ingredients;
	
	/**
	 * Takes in the new ingredient and appends it to the current ArrayList
	 * of ingredients
	 */
	public void addIngredient(String ingredient){
		
		this.ingredients.add(ingredient);
	}
	
	/**
	 * When user selects a set of ingredients the list of indexes is 
	 * passed to this function which then removes ingredients at those indexes.
	 * @param ingredientIndexes
	 */
	public void removeIngredients(List<Integer> ingredientIndexes){
		
		for (int i = 0; i < ingredientIndexes.size(); i++) {
			ingredients.remove(ingredientIndexes.get(i));
		}
	}
	
	/**
	 * Returns all ingredients as a string.
	 * 
	 * @return All Ingredients
	 */
	public ArrayList<String> getAllIngredients() {
		
		return this.ingredients;
	}

	/**
	 * When user selects a set of ingredients the list of indexes is passed to
	 * this function which then returns ingredients as strings at those indexes.
	 * 
	 * @param ingredientsIndexes
	 * @return Selected Ingredients
	 */
	public ArrayList<String> getSelectedIngedients(List<Integer> ingredientIndexes) {
		
		ArrayList<String> selecedIngredients = null;
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
