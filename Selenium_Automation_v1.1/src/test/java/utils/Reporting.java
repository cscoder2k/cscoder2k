package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.google.common.io.Files;

public class Reporting extends GenericReusbales {

	static String testcasename;
	static String screenshotfolder;
	WebDriver driver;
	ExtentTest exreport;
	static HashMap<String, Integer> StatusCount;
	static String reportsummaryfolder;
	static String environment;

	public Reporting(WebDriver driver, String environment, String testcasename, ExtentTest exreport,
			String screenshotfolder, HashMap<String, Integer> StatusCount) {
		this.driver = driver;
		this.environment = environment;
		this.testcasename = testcasename;
		this.exreport = exreport;
		this.screenshotfolder = screenshotfolder;
		this.StatusCount = StatusCount;
	}

	/**
	 * Objects Creation
	 */
	static ReadConfig config = new ReadConfig();

	public void report(Status status, String description) throws Exception {
		String scpath = getScreenshot(screenshotfolder);
		if (status.equals(Status.INFO)) {
			System.out.println(status + " - " + description);
			exreport.log(status, description);
		} else {
			System.out.println(status + " - " + description);
			exreport.log(status, description, MediaEntityBuilder.createScreenCaptureFromPath(scpath).build());
		}

		StatusCount.put("totalCount", StatusCount.get("totalCount") + 1);
		switch (status) {
		case PASS:
			StatusCount.put("passCount", StatusCount.get("passCount") + 1);
			break;
		case INFO:
			StatusCount.put("passCount", StatusCount.get("passCount") + 1);
			break;
		case SKIP:
			StatusCount.put("passCount", StatusCount.get("passCount") + 1);
			break;
		case FAIL:
			StatusCount.put("failCount", StatusCount.get("failCount") + 1);
			break;
		case DEBUG:
			StatusCount.put("failCount", StatusCount.get("failCount") + 1);
			break;
		case ERROR:
			StatusCount.put("failCount", StatusCount.get("failCount") + 1);
			break;
		case FATAL:
			StatusCount.put("failCount", StatusCount.get("failCount") + 1);
			break;
		case WARNING:
			StatusCount.put("warnCount", StatusCount.get("warnCount") + 1);
			break;
		}
	}

	public String getScreenshot(String ScreenshotFolder) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		String path = ScreenshotFolder + "\\snap_" + timeStamp() + ".png";
		File destination = new File(path);
		try {
			Files.copy(src, destination);
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}
		return path;
	}

	public static String reportSummary(String fullReport) {
		try {
			String reporthsummaryPath = "";
			// Create Report Folder
			// String temp = testcasename + "/Screenshots";
			// String reportsummaryfolder = screenshotfolder.substring(1,
			// screenshotfolder.length() - temp.length());
			reportsummaryfolder = System.getProperty("user.dir") + "/extentreports/" + environment + "/TestSummary";

			String reportsummaryName = "TestSummary" + "_" + timeStamp() + ".html";
			reporthsummaryPath = reportsummaryfolder + "\\" + reportsummaryName;

			FileWriter fw = new FileWriter(reporthsummaryPath);
			fw.write(fullReport);
			fw.close();

			return reporthsummaryPath;
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
			return "";
		}
	}

	public static void summaryTable(String testName, String reportPath, String resultStatus, String exeTime) {
		String status;

		if (resultStatus.equalsIgnoreCase("pass"))
			status = "<font color=\"green\">" + resultStatus.toUpperCase() + "</font>";
		else
			status = "<font color=\"red\">" + resultStatus.toUpperCase() + "</font>";

		reportTable = reportTable + "<tr>  <td> <center> <a href=" + reportPath + ">" + testName
				+ "</center> </td> <td> <center>" + status + "</center> </td> <td> <center>" + exeTime
				+ "</center> </td> <td> <center>" + StatusCount.get("totalCount") + "</center> </td> <td> <center>"
				+ StatusCount.get("passCount") + "</center> </td> <td> <center>" + StatusCount.get("failCount")
				+ "</center> </td> <td> <center>" + StatusCount.get("warnCount") + "</center> </td> </tr>";
	}

	public static String frameReportSummaryHTML(String startTime, String endTime) {
		try {
			StatusCount.put("totalCount",
					StatusCount.get("passCount") + StatusCount.get("failCount") + StatusCount.get("warnCount"));

			String hostname = "Unknown";
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
			String user = System.getProperty("user.name");

			String part1 = "<html> <body> <table style=\"width:80%\" border=\"0\" bgcolor=\"#ffffe6\"> <tr bgcolor=\"#66ccff\"> <th>Project</th> <th> "
					+ config.getConfig("projectTitle")
					+ "</th>  </tr> <tr bgcolor=\"#66ccff\"> <th>Application</th> <th>"
					+ config.getConfig("application")
					+ "</th> </tr> <tr bgcolor=\"#66ccff\"> <th>Execution Start Time</th> <th>" + startTime
					+ "</th> </tr> <tr bgcolor=\"#66ccff\"> <th>Execution End Time</th> <th>" + endTime
					+ "</th> </tr> <tr bgcolor=\"#66ccff\"> <th>Executed Machine Username</th> <th>" + user
					+ "</th> </tr> </table>";
			String part2 = "<table style=\"width:80%\" border=\"1\" bgcolor=\"#ffffe6\"> <tr bgcolor=\"#ffd11a\"> <th>Test Case Name</th> <th>Execution status</th> <th>Execution time</th> <th>Total Execution Steps</th> <th>PASSED Steps</th> <th>FAILED Steps</th> <th>WARNING Steps</th> </tr>";
			String part3 = "</table> </body> </html>";
			String fullReport = part1 + part2 + reportTable + part3;
			return fullReport;
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
			return "";
		}
	}
}
