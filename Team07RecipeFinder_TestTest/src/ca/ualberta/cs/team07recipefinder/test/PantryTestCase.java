/**
 * 
 */
package ca.ualberta.cs.team07recipefinder.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import android.test.AndroidTestCase;
import ca.ualberta.cs.team07recipefinder.Pantry;

/**
 * @author xiaohuim
 * 
 */
public class PantryTestCase extends AndroidTestCase{

  private Pantry aPantry = new Pantry();

	
	public void setUp() throws Exception {
		aPantry.clearPantry();
	}

	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#addIngredient(java.lang.String)}
	 * .
	 */
	@Test
	public void testAddIngredient() {
		aPantry.addIngredient("Tomato");
		assertEquals(aPantry.getIngredient(0), "Tomato");
	}

	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#removeIngredients(java.util.List)}
	 * .
	 */
	@Test
	public void testRemoveIngredients() {
		aPantry.addIngredient("Tomato");
		aPantry.addIngredient("Chicken");
		aPantry.addIngredient("Beef");
		List<Integer> aList = new ArrayList<Integer>();
		aList.add(0);
		aList.add(1);
		aPantry.removeIngredients(aList);
		assertEquals(aPantry.getAllIngredients(), Arrays.asList("Beef"));
	}

	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#removeIngredient(int)}.
	 */
	@Test
	public void testRemoveIngredient() {
		aPantry.addIngredient("Tomato");
		aPantry.addIngredient("Chicken");
		aPantry.addIngredient("Beef");
		aPantry.removeIngredient(1);
		assertEquals(aPantry.getAllIngredients(), Arrays.asList("Tomato", "Beef"));
	}
	
	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#removeNullIngredients()}.
	 */
	@Test
	public void testRemoveNUllIngredients() {
		aPantry.addIngredient(null);
		aPantry.addIngredient("Chicken");
		aPantry.addIngredient(null);
		aPantry.removeNullIngredients();
		assertEquals(aPantry.getAllIngredients(), Arrays.asList("Chicken"));
	}

	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#getAllIngredients()}.
	 */
	@Test
	public void testGetAllIngredients() {
		aPantry.addIngredient("Tomato");
		aPantry.addIngredient("Chicken");
		aPantry.addIngredient("Beef");
		assertEquals(aPantry.getAllIngredients(), Arrays.asList("Tomato", "Chicken", "Beef"));
	}

	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#getIngredient(int)}.
	 */
	@Test
	public void testGetIngredient() {
		aPantry.addIngredient("Tomato");
		aPantry.addIngredient("Chicken");
		aPantry.addIngredient("Beef");
		assertEquals(aPantry.getIngredient(1), "Chicken");
	}

	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#updateIngredient(int, java.lang.String)}
	 * .
	 */
	@Test
	public void testUpdateIngredient() {
		aPantry.addIngredient("Tomato");
		aPantry.addIngredient("Chicken");
		aPantry.addIngredient("Beef");
		aPantry.updateIngredient(1, "Duck");
		assertEquals(aPantry.getAllIngredients(), Arrays.asList("Tomato", "Duck", "Beef"));
	}

	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#getSelectedIngedients(java.util.List)}
	 * .
	 */
	@Test
	public void testGetSelectedIngedients() {
		aPantry.addIngredient("Tomato");
		aPantry.addIngredient("Chicken");
		aPantry.addIngredient("Beef");
		List<Integer> aList = new ArrayList<Integer>();
		aList.add(1);
		aList.add(2);
		assertEquals(aPantry.getSelectedIngedients(aList), Arrays.asList("Chicken", "Beef"));
	}

	/**
	 * Test method for
	 * {@link ca.ualberta.cs.team07recipefinder.Pantry#clearPantry()}.
	 */
	@Test
	public void testClearPantry() {
		aPantry.addIngredient("Tomato");
		aPantry.addIngredient("Chicken");
		aPantry.addIngredient("Beef");
		aPantry.clearPantry();
		assertEquals(aPantry.getAllIngredients(), Arrays.asList());
	}

}
