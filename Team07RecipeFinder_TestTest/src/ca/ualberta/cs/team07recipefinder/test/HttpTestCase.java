package ca.ualberta.cs.team07recipefinder.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ca.ualberta.cs.team07recipefinder.HttpClient;
import ca.ualberta.cs.team07recipefinder.Recipe;

public class HttpTestCase {
	HttpClient httpClient;
	Recipe recipe;
	
	
	/**
	 * Use the Before tag to notify the suite to run Setup before running tests
	 */
	@Before
	public void setUp(){
		httpClient = new HttpClient();
		recipe = new Recipe("", null, null, null, null);
	}

	/**
	 * Things denoted with Test will be run authomatically by the suite
	 */
	@Test
	public void writeTest() {
		fail("Not yet implemented");
	}

}
