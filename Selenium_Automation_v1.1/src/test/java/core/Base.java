package core;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import junit.framework.Assert;
import utils.GenericReusbales;
import utils.Reporting;

public class Base extends GenericReusbales {

	public final String frameworkpath = System.getProperty("user.dir");
	public final String defaultbrowser = "chrome";
	public final String defaultenvironment = "01";

	public WebDriver driver;
	public Logger logger;
	public ExtentReports extent;
	public ExtentHtmlReporter htmlreporter;
	public String reportPath, screenshotfolder;

	public String WebdriverPath;
	public String projectPath;
	public ExtentTest exreport;
	public Date TestCaseStartTime, TestCaseEndTime;
	public int passCount, failCount, warnCount, totalCount;
	public String datatablePath, environment, browser;
	public XSSFWorkbook workbook;
	public String testcasename, description;
	public String startTime, endTime, duration;
	public HashMap<String, Integer> StatusCounter = new HashMap<String, Integer>();
	public HashMap testcase = new HashMap();

	/**
	 * Object Creation
	 */
	GenericReusbales generic = new GenericReusbales();
	Reporting reporting = new Reporting(testcase);

	@BeforeSuite
	public void beforeSuite() throws IOException {
		startTime = getTime();
		// Initialize Logger
		initLogger("TestLogger");
	}

	@BeforeClass
	@Parameters({ "browser", "environment" })
	public void beforeClass(@Optional(defaultbrowser) String browsername, @Optional(defaultenvironment) String env)
			throws IOException {

		environment = env;
		browser = browsername;

		// Initialize status count
		initStatusCount();

		// Initialize WebDriver
		initWebDriver(browsername);

		// Get appropriate Datatable based on environment
		workbook = getDatatable(env);

		// Initialize Extent Reporting
		initExtentReport(env, browsername);

		// Assign the test case details into hashmap
		testcase.put("driver", driver);
		testcase.put("environment", environment);
		testcase.put("browser", browsername);
		testcase.put("workbook", workbook);
		testcase.put("exreport", exreport);
		testcase.put("screenshotfolder", screenshotfolder);
		testcase.put("StatusCounter", StatusCounter);
	}

	@AfterClass
	public void afterClass() {
		// Flush Extent Reporting
		flush_ExtentReport();

	}

	@AfterSuite
	@Parameters({ "browser" })
	public void afterSuite(@Optional(defaultbrowser) String browsername) throws IOException {
		endTime = getTime();
		String reporthPath = reporting.reportSummary(reporting.frameReportSummaryHTML(startTime, endTime));

		// Initialize WebDriver
		initWebDriver(browsername);
		driver.get(reporthPath);
		driver.manage().window().maximize();
	}

	public void initStatusCount() {
		StatusCounter.put("passCount", 0);
		StatusCounter.put("failCount", 0);
		StatusCounter.put("warnCount", 0);
		StatusCounter.put("totalCount", 0);
	}

	public void initWebDriver(String browsername) throws IOException {
		if (browsername.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", generic.getConfig("chromedriver"));
			driver = new ChromeDriver();
		} else if (browsername.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", generic.getConfig("iedriver"));
//			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
//			capabilities.setCapability(CapabilityType.BROWSER_NAME, "IE");
//			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			InternetExplorerOptions options = new InternetExplorerOptions();
			options.introduceFlakinessByIgnoringSecurityDomains();
			options.takeFullPageScreenshot();
			driver = new InternetExplorerDriver(options);

		} else if (browsername.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", generic.getConfig("operadriver"));
			driver = new FirefoxDriver();
		} else if (browsername.equalsIgnoreCase("opera")) {
			System.setProperty("webdriver.opera.driver", generic.getConfig("operadriver"));
			driver = new OperaDriver();
		}

	}

	public XSSFWorkbook getDatatable(String env) throws IOException {
		datatablePath = frameworkpath + "/datatable/" + env + "/datatable.xlsx";
		if (ifFileExists(datatablePath)) {
			XSSFWorkbook wb = new XSSFWorkbook(datatablePath);
			System.out.println("datatable path = " + datatablePath);
			return wb;
		} else {
			System.out.println("Datatable file not found. Given path: " + datatablePath);
			Assert.assertEquals(true, false);
			return null;
		}

	}

	public void initExtentReport(String env, String browsername) throws IOException {
		String reportFolder = frameworkpath + "/extentreports/" + env + "/";
		// Create Report Folder
		String testreportfolder = reportFolder + testcasename + "/report_" + timeStamp();
		createFolder(testreportfolder);
		String reportName = testcasename + ".html";
		reportPath = testreportfolder + "/" + reportName;
		// Screenshot path
		screenshotfolder = testreportfolder + "/Screenshots";
		createFolder(screenshotfolder);

		// start reporters
		htmlreporter = new ExtentHtmlReporter(reportPath);
		htmlreporter.config().setReportName(generic.getConfig("projectTitle"));

		// create extentReports and attach reporter(s)
		extent = new ExtentReports();
		extent.attachReporter(htmlreporter);
		extent.setSystemInfo("Project", generic.getConfig("projectTitle"));
		extent.setSystemInfo("Application", generic.getConfig("application"));
		extent.setSystemInfo("Environment", env);
		extent.setSystemInfo("Browser", browsername);
		exreport = extent.createTest(testcasename, description);

		TestCaseStartTime = getNow();
		exreport.getModel().setStartTime(getNow());
	}

	public void initLogger(String desc) {
		logger = Logger.getLogger(desc);
		PropertyConfigurator.configure("Log4J.properties");
	}

	public void flush_ExtentReport() {
		exreport.getModel().setEndTime(getNow());
		TestCaseEndTime = getNow();
		String duration = exreport.getModel().getRunDuration();
		extent.flush();
		reporting.summaryTable(testcasename, reportPath, exreport.getStatus().toString(), duration, environment,
				browser);
	}

	public void teardownexception(Reporting reporting, Exception e) {
		try {
			reporting.report(Status.FAIL, testcasename + " - Exception Occured.  " + e.getMessage());
		} catch (Exception e1) {
			System.out.println(testcasename + " - Exception Occured.  " + e.getMessage());
			e1.printStackTrace();
			driver.close();
			driver.quit();
		}
		Assert.assertEquals(true, false);
	}

}
