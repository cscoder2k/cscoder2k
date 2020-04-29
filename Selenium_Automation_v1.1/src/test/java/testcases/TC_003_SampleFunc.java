package testcases;

import org.testng.annotations.Test;

import core.Base;
import pages.HomePage;
import utils.Reporting;

public class TC_003_SampleFunc extends Base {

	public TC_003_SampleFunc() {
		testcasename = this.getClass().getSimpleName();
		description = "To check the login functionality of the application";
	}

	HomePage homepage;
	Reporting reporting;

	@Test
	public void testcase() {
		try {
			homepage = new HomePage(driver, environment, workbook, testcasename, exreport, screenshotfolder,
					StatusCounter);
			reporting = new Reporting(driver, environment, testcasename, exreport, screenshotfolder, StatusCounter);

			homepage.launchApp();
			homepage.closeApp();

		} catch (Exception e) {
			teardownexception(reporting, e);
		}
	}
}
