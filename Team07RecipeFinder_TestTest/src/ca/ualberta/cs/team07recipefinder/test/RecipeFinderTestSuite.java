package ca.ualberta.cs.team07recipefinder.test;

import junit.framework.TestResult;
import junit.framework.TestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value = Suite.class)
@SuiteClasses(value = { HttpTestCase.class })
public class RecipeFinderTestSuite extends TestSuite {
	
	/**
	 * Add your test case to the suite
	 */
	RecipeFinderTestSuite(){
		TestSuite suite = new TestSuite(HttpTestCase.class);
		//TestResult result = null;
		//suite.addTestSuite(RecipeTestCase.class);
		
		//suite.run(result);
	}
}
