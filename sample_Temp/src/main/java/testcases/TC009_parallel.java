package testcases;

import org.testng.annotations.Test;

public class TC009_parallel extends reusable {

	@Test
	void testcase() {
		appPath = "https://www.google.com/maps";
		browser = "chrome";
		appLaunch();
	}

}
