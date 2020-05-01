package testcases;

import org.testng.annotations.Test;

import core.Base;
import pages.HomePage;
import utils.Reporting;

public class TC_001_SampleFunc extends Base {
	HomePage homepage;
	Reporting reporting;

	public TC_001_SampleFunc() {
		testcasename = this.getClass().getSimpleName();
		testcase.put("testcasename", testcasename);
		description = "To check the login functionality of the application";
	}

	@Test
	public void testcase() {
		try {
			homepage = new HomePage(testcase);
			reporting = new Reporting(testcase);

			homepage.launchApp();
			homepage.closeApp();

		} catch (Exception e) {
			teardownexception(reporting, e);
		}
	}

}
