package ca.ualberta.cs.team07recipefinder.test;

import java.lang.reflect.Method;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import android.content.Context;
import android.test.ServiceTestCase;
import ca.ualberta.cs.team07recipefinder.Pantry;
import ca.ualberta.cs.team07recipefinder.Recipe;
import ca.ualberta.cs.team07recipefinder.User;

public class UserTestCase extends TestCase {
	Recipe recipe;
	User user;

	/**
	 * Use the Before tag to notify the suite to run Setup before running tests
	 */
	@Before
	public void setUp() {
		user = User.getInstance();
		user.setName("Test Bob");
		user.setEmail("Test@test.com");
		Pantry pantry = new Pantry();
		pantry.addIngredient("test fish");
		user.setPantry(pantry);
	}

	@Test
	public void testWrite() {
		user.Write(getTestContext());
		User returnedUser = User.getInstance();
		assert(returnedUser == user);
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
