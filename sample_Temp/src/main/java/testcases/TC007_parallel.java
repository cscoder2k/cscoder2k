package testcases;

import org.testng.annotations.Test;

public class TC007_parallel extends reusable {

	@Test
	void testcase() {
		appPath = "https://www.youtube.com/";
		browser = "chrome";
		appLaunch();
	}

}
