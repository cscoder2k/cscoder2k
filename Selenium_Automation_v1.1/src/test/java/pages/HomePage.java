package pages;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;

import utils.Datatable;
import utils.GenericReusbales;
import utils.ObjectHandlers;
import utils.ReadConfig;
import utils.Reporting;

interface HomepageElements {

}

public class HomePage extends GenericReusbales implements HomepageElements {
	String sheetName = this.getClass().getSimpleName();
	String testcasename, environment, screenshotfolder;
	WebDriver driver;
	ExtentTest exreport;
	XSSFWorkbook workbook;
	static HashMap<String, Integer> StatusCount;

	Datatable datatable;
	ObjectHandlers object;
	Reporting reporting;
	ReadConfig config;

	public HomePage(WebDriver driver, String environment, XSSFWorkbook workbook, String testcasename,
			ExtentTest exreport, String screenshotfolder, HashMap<String, Integer> StatusCount) {
		this.driver = driver;
		this.workbook = workbook;
		this.testcasename = testcasename;
		this.environment = environment;
		this.exreport = exreport;
		this.screenshotfolder = screenshotfolder;
		this.StatusCount = StatusCount;
		instantiate();

	}

	public void instantiate() {
		datatable = new Datatable(workbook, testcasename, sheetName);
		object = new ObjectHandlers(driver, environment, testcasename, exreport, screenshotfolder, StatusCount);
		reporting = new Reporting(driver, environment, testcasename, exreport, screenshotfolder, StatusCount);
		config = new ReadConfig();
	}

	By EDT_USERNAME = By.cssSelector("input[id='txtUsername']");
	By EDT_PASSWORD = By.cssSelector("input[id='txtPassword']");
	By BTN_LOGIN = By.cssSelector("input[id='btnLogin']");

	/**
	 * To launch application and login
	 * 
	 * @throws Exception
	 */
	public void launchApp() throws Exception {
		String user = datatable.getCellData("Username");
		String password = datatable.getCellData("Password");

		// Launch url
		String URL_VAR = "url_" + environment;
		String url = config.getConfig(URL_VAR);
		driver.get(url);

		driver.manage().window().maximize();

		// Implicit Wait
		driver.manage().timeouts().implicitlyWait(MAX_TIMEOUT, TimeUnit.SECONDS);

		// Enter Username and password
		object.set(EDT_USERNAME, user, "Username");
		object.set(EDT_PASSWORD, password, "Password");
		object.click(BTN_LOGIN, "Login");
	}

	/**
	 * To Close Application
	 * 
	 * @throws Exception
	 */
	public void closeApp() throws Exception {
		// Close browser
		driver.close();
		System.out.println(testcasename + " - Browser Closed");
	}

}
