package ca.ualberta.cs.team07recipefinder.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.ualberta.cs.team07recipefinder.SqlClient;

import java.lang.reflect.Method;
import android.content.Context;
import android.test.ServiceTestCase;
import junit.framework.TestCase;

/* Test class for the SqlClient class. Tests adding, updating, and deleting
 * Recipes from the local SqlDatabase
 */
public class SqlClientTestCase extends TestCase {
	private SqlClient testClient = new SqlClient(getTestContext());
	
	public void testConnection() {
		
		assertTrue(testClient != null);
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