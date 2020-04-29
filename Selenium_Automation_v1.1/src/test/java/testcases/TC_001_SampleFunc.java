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
		description = "To check the login functionality of the application";
	}

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
