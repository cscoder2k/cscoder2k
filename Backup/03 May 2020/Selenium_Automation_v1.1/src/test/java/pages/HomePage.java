package pages;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.Status;

import utils.Datatable;
import utils.GenericReusbales;
import utils.ObjectHandlers;
import utils.Reporting;

public class HomePage extends GenericReusbales {
	String sheetName = this.getClass().getSimpleName();
	String environment;
	WebDriver driver;

	Datatable datatable;
	ObjectHandlers object;
	Reporting reporting;
	HashMap testcase;

	public HomePage(HashMap testcase) {
		this.testcase = testcase;
		this.testcase.put("sheetName", sheetName);
		this.driver = (WebDriver) testcase.get("driver");
		this.environment = (String) testcase.get("environment");
		instantiate();
	}

	public void instantiate() {
		datatable = new Datatable(testcase);
		object = new ObjectHandlers(testcase);
		reporting = new Reporting(testcase);
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
		String url = getConfig(URL_VAR);
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
		System.out.println(testcase.get("testcasename") + " - Browser Closed");
	}

	public void sample_pass() throws Exception {
		reporting.report(Status.PASS, "Sample desc");
	}

	public void sample_fail() throws Exception {
		reporting.report(Status.FAIL, "Sample desc");
	}

	public void sample_fatal() throws Exception {
		reporting.report(Status.FATAL, "Sample desc");
	}

	public void sample_skip() throws Exception {
		reporting.report(Status.SKIP, "Sample desc");
	}

	public void sample_err() throws Exception {
		reporting.report(Status.ERROR, "Sample desc");
	}

	public void sample_debug() throws Exception {
		reporting.report(Status.DEBUG, "Sample desc");
	}

	public void sample_warn() throws Exception {
		reporting.report(Status.WARNING, "Sample desc");
	}

	public void sample_info() throws Exception {
		reporting.report(Status.INFO, "Sample desc");
	}

}
