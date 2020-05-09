package testcases;

import org.testng.annotations.Test;

public class TC008_parallel extends reusable {

	@Test
	void testcase() {
		appPath = "https://www.facebook.com/";
		browser = "ie";
		appLaunch();
	}

}
