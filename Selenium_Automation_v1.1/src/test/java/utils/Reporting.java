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

	String screenshotfolder;
	WebDriver driver;
	ExtentTest exreport;
	static HashMap<String, HashMap<String, Integer>> AllTestStatusCounter = new HashMap<String, HashMap<String, Integer>>();
	final String reportsummaryfolder = System.getProperty("user.dir") + "/extentreports/" + "/TestSummary";
	String environment, testcasename;
	GenericReusbales generic;
	Object testobject;

	public Reporting(HashMap testcase) {
		this.driver = (WebDriver) testcase.get("driver");
		this.environment = (String) testcase.get("environment");
		this.exreport = (ExtentTest) testcase.get("exreport");
		this.screenshotfolder = (String) testcase.get("screenshotfolder");
		this.testcasename = (String) testcase.get("testcasename");
		AllTestStatusCounter.put(testcasename, (HashMap<String, Integer>) testcase.get("StatusCounter"));
		instantiate();
	}

	public void instantiate() {
		generic = new GenericReusbales();
	}

	public void report(Status status, String description) throws Exception {

		String scpath = getScreenshot(screenshotfolder);
		if (status.equals(Status.INFO)) {
			System.out.println(status + " - " + description);
			exreport.log(status, description);
		} else {
			System.out.println(status + " - " + description);
			exreport.log(status, description, MediaEntityBuilder.createScreenCaptureFromPath(scpath).build());
		}

		AllTestStatusCounter.get(testcasename).put("totalCount",
				AllTestStatusCounter.get(testcasename).get("totalCount") + 1);
		switch (status) {
		case PASS:
			AllTestStatusCounter.get(testcasename).put("passCount",
					AllTestStatusCounter.get(testcasename).get("passCount") + 1);
			break;
		case INFO:
			AllTestStatusCounter.get(testcasename).put("passCount",
					AllTestStatusCounter.get(testcasename).get("passCount") + 1);
			break;
		case SKIP:
			AllTestStatusCounter.get(testcasename).put("passCount",
					AllTestStatusCounter.get(testcasename).get("passCount") + 1);
			break;
		case FAIL:
			AllTestStatusCounter.get(testcasename).put("failCount",
					AllTestStatusCounter.get(testcasename).get("failCount") + 1);
			break;
		case DEBUG:
			AllTestStatusCounter.get(testcasename).put("failCount",
					AllTestStatusCounter.get(testcasename).get("failCount") + 1);
			break;
		case ERROR:
			AllTestStatusCounter.get(testcasename).put("failCount",
					AllTestStatusCounter.get(testcasename).get("failCount") + 1);
			break;
		case FATAL:
			AllTestStatusCounter.get(testcasename).put("failCount",
					AllTestStatusCounter.get(testcasename).get("failCount") + 1);
			break;
		case WARNING:
			AllTestStatusCounter.get(testcasename).put("warnCount",
					AllTestStatusCounter.get(testcasename).get("warnCount") + 1);
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

	public String reportSummary(String fullReport) {
		try {
			String reporthsummaryPath = "";

			// Create summary Folder
			createFolder(reportsummaryfolder);

			String reportsummaryName = "TestSummary" + "_" + timeStamp() + ".html";
			reporthsummaryPath = reportsummaryfolder + "/" + reportsummaryName;

			FileWriter fw = new FileWriter(reporthsummaryPath);
			fw.write(fullReport);
			fw.close();

			return reporthsummaryPath;
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
			return "";
		}
	}

	public static void summaryTable(String testName, String reportPath, String resultStatus, String exeTime, String env,
			String browser) {
		String status;

		if (resultStatus.equalsIgnoreCase("pass"))
			status = "<font color=\"green\">" + resultStatus.toUpperCase() + "</font>";
		else
			status = "<font color=\"red\">" + resultStatus.toUpperCase() + "</font>";

		reportTable = reportTable + "<tr>  <td> <center> <a href=" + reportPath + ">" + testName
				+ "</center> </td> <td> <center>" + status + "</center> </td> <td> <center>" + exeTime
				+ "</center> </td> <td> <center>" + env + "</center> </td>" + "<td> <center>" + browser
				+ "</center> </td>" + "<td> <center>" + AllTestStatusCounter.get(testName).get("totalCount")
				+ "</center> </td> <td> <center>" + AllTestStatusCounter.get(testName).get("passCount")
				+ "</center> </td> <td> <center>" + AllTestStatusCounter.get(testName).get("failCount")
				+ "</center> </td> <td> <center>" + AllTestStatusCounter.get(testName).get("warnCount")
				+ "</center> </td> </tr>";
	}

	public String frameReportSummaryHTML_OLD(String startTime, String endTime) {
		try {
			String hostname = "Unknown";
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
			String user = System.getProperty("user.name");

			String part1 = "<html> <body> <table style=\"width:80%\" border=\"0\" bgcolor=\"#ffffe6\"> <tr bgcolor=\"#66ccff\"> <th>Project</th> <th> "
					+ generic.getConfig("projectTitle")
					+ "</th>  </tr> <tr bgcolor=\"#66ccff\"> <th>Application</th> <th>"
					+ generic.getConfig("application")
					+ "</th> </tr> <tr bgcolor=\"#66ccff\"> <th>Execution Start Time</th> <th>" + startTime
					+ "</th> </tr> <tr bgcolor=\"#66ccff\"> <th>Execution End Time</th> <th>" + endTime
					+ "</th> </tr> <tr bgcolor=\"#66ccff\"> <th>Executed Machine Username</th> <th>" + user
					+ "</th> </tr> </table>";
			String part2 = "<table style=\"width:80%\" border=\"1\" bgcolor=\"#ffffe6\"> <tr bgcolor=\"#ffd11a\"> <th>Test Case Name</th> <th>Execution status</th> <th>Execution time</th> <th>Environment</th>  <th>Browser</th> <th>Total Execution Steps</th> <th>PASSED</th> <th>FAILED</th> <th>WARNING</th> </tr>";
			String part3 = "</table> </body> </html>";
			String fullReport = part1 + part2 + reportTable + part3;
			return fullReport;
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
			return "";
		}
	}

	public String frameReportSummaryHTML(String startTime, String endTime) {
		try {
			String hostname = "Unknown";
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
			String user = System.getProperty("user.name");

			String TABLE_BGCOLOR = "#ffffe6";
			String TABLE_TOPHEADBGCOLOR = "#030953";
			String TABLE_HEADBGCOLOR = "#ffd11a";

			String part1 = "<html> <body> <table style=\"width:80%\"  border=\"0\" bgcolor=" + TABLE_BGCOLOR + "> "
					+ " <tr style=\"color:White\" bgcolor=" + TABLE_TOPHEADBGCOLOR + "> <th>Project</th> <th> "
					+ generic.getConfig("projectTitle") + "</th>  </tr> " + "<tr style=\"color:White\"bgcolor="
					+ TABLE_TOPHEADBGCOLOR + "> <th>Application</th> <th>" + generic.getConfig("application")
					+ "</th> </tr> <tr style=\"color:White\" bgcolor=" + TABLE_TOPHEADBGCOLOR
					+ "> <th>Execution Start Time</th> <th>" + startTime
					+ "</th> </tr> <tr style=\"color:White\" bgcolor=" + TABLE_TOPHEADBGCOLOR + "> "
					+ " <th>Execution End Time</th> <th>" + endTime + "</th> </tr> <tr style=\"color:White\" bgcolor="
					+ TABLE_TOPHEADBGCOLOR + "> " + " <th>Executed Machine Username</th> <th>" + user
					+ "</th> </tr> </table>";

			String part2 = "<table style=\"width:80%\" border=\"1\" bgcolor=" + TABLE_BGCOLOR + "> <tr bgcolor="
					+ TABLE_HEADBGCOLOR
					+ "> <th>Test Case Name</th> <th>Execution status</th> <th>Execution time</th> <th>Environment</th>  <th>Browser</th> <th>Total Execution Steps</th> <th>PASSED</th> <th>FAILED</th> <th>WARNING</th> </tr>";
			String part3 = "</table> </body> </html>";
			String fullReport = part1 + part2 + reportTable + part3;
			return fullReport;
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
			return "";
		}
	}
}
