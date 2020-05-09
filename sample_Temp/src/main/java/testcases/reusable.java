package testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class reusable {

	public String appPath;
	WebDriver driver;
	String WebdriverPath;
	String browser;

	By EDT_USERNAME = By.cssSelector("input[id='txtUsername']");
	By EDT_PASSWORD = By.cssSelector("input[id='txtPassword']");
	By BTN_LOGIN = By.cssSelector("input[id='btnLogin']");

	public void appLaunch() {

		if (browser.equalsIgnoreCase("chrome")) {
			WebdriverPath = "./drivers/chromedriver.exe";
			System.setProperty("webdriver.chrome.driver", WebdriverPath);
			driver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("ie")) {
			WebdriverPath = "./drivers/IEDriverServer.exe";
			System.setProperty("webdriver.ie.driver", WebdriverPath);
			driver = new InternetExplorerDriver();
		}

		driver.get(appPath);
		System.out.println(appPath + "-------> Launched in " + browser);

	}

	public void loginHRM() {
		driver.findElement((EDT_USERNAME)).sendKeys("Admin");
		driver.findElement((EDT_PASSWORD)).sendKeys("admin123");
		driver.findElement(BTN_LOGIN).click();
	}

	
	public void closeBrowser() {
		driver.close();
	}
}
