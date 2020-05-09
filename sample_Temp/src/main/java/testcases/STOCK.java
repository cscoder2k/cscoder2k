package testcases;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class STOCK extends reusable {

	@Test
	void testcase() {

		browser = "chrome";
		appPath = "https://www.moneyworks4me.com/best-index/nse-stocks/top-nifty50-companies-list/";
		appLaunch();

		List<WebElement> list = driver.findElements(By.xpath("//table[@id='stock-list-table']//td/div/span"));
		for (WebElement ele : list) {
			System.out.println(ele.getText());
		}

	}
}
