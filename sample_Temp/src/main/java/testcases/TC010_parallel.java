package testcases;

import org.testng.annotations.Test;

public class TC010_parallel extends reusable {

	@Test
	void testcase() {
		appPath = "https://www.gmail.com/";
		browser = "ie";
		appLaunch();
	}

}
