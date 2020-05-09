package testcases;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sikuli.script.App;
import org.sikuli.script.Key;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.testng.annotations.Test;

public class test_3_sikuli {

	String WBPath;
	XSSFWorkbook wb;
	XSSFSheet ws;
	int waitTime = 5;

	Screen screen = new Screen();

	Pattern notepadlogo = new Pattern("E:\\OfficeWork\\EclipseWorkspace2\\sample_Temp\\images\\notepadlogo.png");
	Pattern editArea = new Pattern("E:\\OfficeWork\\EclipseWorkspace2\\sample_Temp\\images\\EditArea.png");

	@Test
	public void testcase() throws Exception {

		String appName = "E:\\Softwares\\Notepad ++\\notepad++.exe";

		App.open(appName);
		screen.wait(notepadlogo, 5000);

		String input = "hello...!!!";

		click(editArea);
		type(editArea, input);

	}

	public void click(Pattern pattern) throws Exception {
		waitForElement(pattern, waitTime);
		screen.click(pattern);
		Thread.sleep(1000);
		System.out.println("Clicked on " + pattern.getFilename() + " successfully");

	}

	public void type(Pattern pattern, String text) {
		try {
			if (text.trim() != "") {
				waitForElement(pattern, waitTime);
				screen.type(pattern, text + Key.ENTER);
				Thread.sleep(1000);
				System.out.println("Typed " + text + " in object " + pattern.getFilename() + " successfully");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				System.out.println("Unable to type " + text + " in object " + pattern.getFilename()
						+ " due to exception:" + e.getMessage());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public void waitForElement(Pattern pattern, int seconds) {
		try {
			screen.wait(pattern, seconds);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
