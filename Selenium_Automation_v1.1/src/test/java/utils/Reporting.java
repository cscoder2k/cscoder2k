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
	static HashMap<String, Integer> StatusCounter;
	String reportsummaryfolder;
	String environment;
	GenericReusbales generic;

	public Reporting(HashMap testcase) {
		this.driver = (WebDriver) testcase.get("driver");
		this.environment = (String) testcase.get("environment");
		this.exreport = (ExtentTest) testcase.get("exreport");
		this.screenshotfolder = (String) testcase.get("screenshotfolder");
		this.StatusCounter = (HashMap<String, Integer>) testcase.get("StatusCounter");
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

		StatusCounter.put("totalCount", StatusCounter.get("totalCount") + 1);
		switch (status) {
		case PASS:
			StatusCounter.put("passCount", StatusCounter.get("passCount") + 1);
			break;
		case INFO:
			StatusCounter.put("passCount", StatusCounter.get("passCount") + 1);
			break;
		case SKIP:
			StatusCounter.put("passCount", StatusCounter.get("passCount") + 1);
			break;
		case FAIL:
			StatusCounter.put("failCount", StatusCounter.get("failCount") + 1);
			break;
		case DEBUG:
			StatusCounter.put("failCount", StatusCounter.get("failCount") + 1);
			break;
		case ERROR:
			StatusCounter.put("failCount", StatusCounter.get("failCount") + 1);
			break;
		case FATAL:
			StatusCounter.put("failCount", StatusCounter.get("failCount") + 1);
			break;
		case WARNING:
			StatusCounter.put("warnCount", StatusCounter.get("warnCount") + 1);
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
			reportsummaryfolder = System.getProperty("user.dir") + "/extentreports/" + "/TestSummary";
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

	public void summaryTable(String testName, String reportPath, String resultStatus, String exeTime) {
		String status;

		if (resultStatus.equalsIgnoreCase("pass"))
			status = "<font color=\"green\">" + resultStatus.toUpperCase() + "</font>";
		else
			status = "<font color=\"red\">" + resultStatus.toUpperCase() + "</font>";

		reportTable = reportTable + "<tr>  <td> <center> <a href=" + reportPath + ">" + testName
				+ "</center> </td> <td> <center>" + status + "</center> </td> <td> <center>" + exeTime
				+ "</center> </td> <td> <center>" + StatusCounter.get("totalCount") + "</center> </td> <td> <center>"
				+ StatusCounter.get("passCount") + "</center> </td> <td> <center>" + StatusCounter.get("failCount")
				+ "</center> </td> <td> <center>" + StatusCounter.get("warnCount") + "</center> </td> </tr>";
	}

	public String frameReportSummaryHTML(String startTime, String endTime) {
		try {
			StatusCounter.put("totalCount",
					StatusCounter.get("passCount") + StatusCounter.get("failCount") + StatusCounter.get("warnCount"));

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
