package testcases;

import org.testng.annotations.Test;

public class TC011_CI extends reusable {

	@Test
	void testcase() {

		appPath = "https://opensource-demo.orangehrmlive.com/";
		browser = "chrome";
		appLaunch();

		loginHRM();

		closeBrowser();
	}

}
