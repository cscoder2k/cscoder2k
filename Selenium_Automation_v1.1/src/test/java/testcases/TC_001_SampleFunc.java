package testcases;

import java.util.HashMap;

import org.testng.annotations.Test;

import core.Base;
import pages.HomePage;
import utils.Reporting;

public class TC_001_SampleFunc extends Base {
	HomePage homepage;
	Reporting reporting;
	HashMap<String, HashMap<String, Integer>> testcasestatuscounter;

	public TC_001_SampleFunc() {
		testcasename = this.getClass().getSimpleName();
		testcase.put("testcasename", testcasename);
		description = "To check the sample functionality of the application";
	}

	@Test
	public void testcase() {
		try {
			homepage = new HomePage(testcase);
			reporting = new Reporting(testcase);

			homepage.launchApp();

			homepage.sample_pass();

			homepage.sample_fail();

			homepage.sample_fatal();

			homepage.sample_skip();

			homepage.sample_err();

			homepage.sample_debug();

			homepage.sample_warn();

			homepage.sample_info();

			homepage.closeApp();

		} catch (Exception e) {
			teardownexception(reporting, e);
		}
	}

}
