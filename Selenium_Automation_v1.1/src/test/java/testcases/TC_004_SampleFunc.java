package testcases;

import org.testng.annotations.Test;

import core.Base;
import pages.HomePage;
import utils.Reporting;

public class TC_004_SampleFunc extends Base {

	public TC_004_SampleFunc() {
		testcasename = this.getClass().getSimpleName();
		testcase.put("testcasename", testcasename);
		description = "To check the sample functionality of the application";
	}

	HomePage homepage;
	Reporting reporting;

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
