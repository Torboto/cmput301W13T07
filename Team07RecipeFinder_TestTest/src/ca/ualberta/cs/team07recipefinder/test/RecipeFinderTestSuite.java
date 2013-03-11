package ca.ualberta.cs.team07recipefinder.test;

import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TEST.class })
public class RecipeFinderTestSuite extends TestSuite {
	
	/**
	 * Add your test case to the suite
	 */
	RecipeFinderTestSuite(){
		TestSuite suite= new TestSuite(HttpTestCase.class);
		//suite.addTestSuite(RecipeTestCase.class);
	}
}
