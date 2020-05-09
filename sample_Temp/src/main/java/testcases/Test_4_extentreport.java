package testcases;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

public class Test_4_extentreport {

	String WBPath;
	XSSFWorkbook wb;
	XSSFSheet ws;

	String testcasename = this.getClass().toString();

	@Test
	public void extentreport() {

		String reportPath = "E:\\OfficeWork\\EclipseWorkspace2\\sample_Temp\\ExtentReports";

		// start reporters
		ExtentHtmlReporter htmlreporter = new ExtentHtmlReporter(reportPath);

		// create extentReports and attach reporter(s)
		ExtentReports extent = new ExtentReports();
		extent.attachReporter(htmlreporter);

		ExtentTest test = extent.createTest(testcasename, "Automation Test");

		test.log(Status.PASS, "Sample report done");
	}

}
