package utils;

import java.util.HashMap;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

public class ObjectHandlers extends GenericReusbales {

	String screenshotfolder, testcasename;
	WebDriver driver;
	ExtentTest exreport;
	Reporting reporting;
	String environment;
	HashMap testcase;

	public ObjectHandlers(HashMap testcase) {
		this.testcase = testcase;
		this.driver = (WebDriver) testcase.get("driver");
		instantiate();
	}

	public void instantiate() {
		reporting = new Reporting(testcase);
	}

	/*********************************************
	 * Element Actions
	 *********************************************/
	public void click(By by, String fieldName) throws Exception {
		if (isElementPresent(by)) {
			find(by).click();
			// TestNG report
			Reporter.log("Clicked on " + by.toString());
			// Log4j Report
			// log.info("Clicked on " + by.toString());
			// Extent Report
			reporting.report(Status.PASS, "Clicked on " + fieldName);
		} else
			reporting.report(Status.FAIL, fieldName + " Object Not Present");
	}

	public void clickIfExists(By by, String fieldName) throws Exception {
		if (isElementPresent(by)) {
			find(by).click();
			reporting.report(Status.PASS, "Clicked on " + fieldName);
		}
	}

	public void clickIfDataExists(By by, String data, String fieldName) throws Exception {
		if (data.trim() != "") {
			if (isElementPresent(by)) {
				find(by).click();
				reporting.report(Status.PASS, "Clicked on " + fieldName);
			} else
				reporting.report(Status.FAIL, fieldName + " Object Not Present");
		}
	}

	public void clickByJs(By by, String fieldName) throws Exception {
		if (isElementPresent(by)) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", find(by));
			reporting.report(Status.PASS, "Clicked on " + fieldName);
		} else
			reporting.report(Status.FAIL, fieldName + " Object Not Present");
	}

	public void doubleclick(By by, String fieldName) throws Exception {
		try {
			Actions act = new Actions(driver);
			act.doubleClick(find(by)).build().perform();
			reporting.report(Status.PASS, "Double Clicked on element " + fieldName);
		} catch (Exception e) {
			reporting.report(Status.FAIL, "Caught Exception: " + e.getMessage());
		}
	}

	public void doubleClickByJS(By by, String fieldName) {
		try {
			if (isElementPresent(by)) {
				WebElement element = driver.findElement(by);
				((JavascriptExecutor) driver).executeScript("arguments[0].dblclick();", element);
				reporting.report(Status.PASS, "Double Clicked on element " + fieldName);
			} else {
				throw new RuntimeException("Element not present");
			}
		} catch (Exception ex) {
			throw new RuntimeException("Error in doubleClickByJS " + by, ex);
		}
	}

	public void set(By by, String txt, String fieldName) throws Exception {
		if (isElementPresent(by)) {
			find(by).sendKeys(txt);
			reporting.report(Status.PASS, txt + " value entered in " + fieldName + " editbox");
		} else
			reporting.report(Status.FAIL, fieldName + " Object Not Present");
	}

	public void setByJs(By by, String txt, String fieldName) throws Exception {
		if (isElementPresent(by)) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('value', '" + txt + "')", find(by));
			reporting.report(Status.PASS, txt + " value entered in " + fieldName + " editbox");
		} else
			reporting.report(Status.FAIL, fieldName + " Object Not Present");
	}

	public void selectByVisibleText(By by, String txt, String fieldName) throws Exception {
		if (isElementPresent(by)) {
			Select lst = new Select(find(by));
			lst.selectByVisibleText(txt);
			reporting.report(Status.PASS, txt + " value selected in " + fieldName + " dropdown");
		} else
			reporting.report(Status.FAIL, fieldName + " Object Not Present");
	}

	public void selectByValue(By by, String txt, String fieldName) throws Exception {
		if (isElementPresent(by)) {
			Select lst = new Select(find(by));
			lst.selectByValue(txt);
			reporting.report(Status.PASS, txt + " value selected in " + fieldName + " dropdown");
		} else
			reporting.report(Status.FAIL, fieldName + " Object Not Present");
	}

	public void selectByIndex(By by, int index, String fieldName) throws Exception {
		if (isElementPresent(by)) {
			Select lst = new Select(find(by));
			lst.selectByIndex(index);
			reporting.report(Status.PASS,
					"List value  with index " + index + " is selected in " + fieldName + " dropdown");
		} else
			reporting.report(Status.FAIL, fieldName + " Object Not Present");
	}

	/*********************************************
	 * Generic Actions
	 *********************************************/

	public static void setWaitTime(int seconds) {
		MAX_TIMEOUT = seconds;
		log.info("waitTime changed to " + seconds);
	}

	public static int getWaitTime() {
		return MAX_TIMEOUT;
	}

	public static void resetWaitTime() {
		MAX_TIMEOUT = DEFAULT_MAX_TIMEOUT;
	}

	public void highlight(By by) {
		try {
			// draw a border around the found element
			((JavascriptExecutor) driver).executeScript("arguments[0].style.border='3px solid red'", find(by));
		} catch (Exception ex) {
			throw new RuntimeException("Error in highlight element " + by, ex);
		}
	}

	public void Msgbox(String msg) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("alert(arguments[0]);", msg);
	}

	public WebElement find(By locator) {
		return driver.findElement(locator);
	}

	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}

	}

	/*********************************************
	 * Get Methods
	 *********************************************/

	public String getElementText(By by) {
		try {
			WebElement ele = driver.findElement(by);
			if (ele.getTagName().equalsIgnoreCase("input")) {
				return ele.getAttribute("value");
			} else if (ele.getTagName().equalsIgnoreCase("select")) {
				return new Select(ele).getFirstSelectedOption().getText();
			} else {
				return ele.getText();
			}
		}

		catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getElementTextifExists(By by) {
		try {
			WebElement element = find(by);
			if (element.getTagName().equalsIgnoreCase("input")) {
				return getTextForInput(by, element);
			} else if (element.getTagName().equalsIgnoreCase("a")) {
				return getTextForAnchor(by, element);
			} else if (element.getTagName().equalsIgnoreCase("select")) {
				return getTextOrLocator(by, new Select(element).getFirstSelectedOption());
			} else {
				return getTextOrLocator(by, element);
			}
		} catch (Exception e) {
			return by.toString();
		}
	}

	private String getTextForInput(By locator, WebElement input) {
		try {
			return getTextOrLocator(locator,
					By.cssSelector(String.format("label[for='%s']", input.getAttribute("id"))).findElement(driver));
		} catch (Exception e) {
			return locator.toString();
		}
	}

	private String getTextOrLocator(By locator, WebElement element) {
		try {
			String text = element.getText();
			if (!"".equals(text))
				return text;
		} catch (Exception e) {
			return locator.toString();
		}
		return locator.toString();
	}

	private String getTextForAnchor(By locator, WebElement anchor) {
		try {
			return Objects
					.toString(((JavascriptExecutor) driver).executeScript("return arguments[0].innerText", anchor),
							locator.toString())
					.trim();
		} catch (Exception e) {
			return locator.toString();
		}
	}

	/*********************************************
	 * Element Presence
	 *********************************************/

	public boolean isElementPresent(By by) {
		try {
			waitForElementToBePresent(by);
			WebElement Element = find(by);
			return Element != null;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean isElementEnabled(By by) {
		try {
			if (isElementPresent(by)) {
				WebElement Element = driver.findElement(by);
				return Element.isEnabled();
			} else
				return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean isElementDisplayed(By by) {
		try {
			if (isElementPresent(by)) {
				WebElement Element = driver.findElement(by);
				return Element.isDisplayed();
			} else
				return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean isElementSelected(By by) {
		try {
			if (isElementPresent(by)) {
				WebElement Element = driver.findElement(by);
				return Element.isSelected();
			} else
				return false;
		} catch (Exception ex) {
			return false;
		}
	}

	public boolean isAlertPresent(By by) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT /* timeout in seconds */);
		if (wait.until(ExpectedConditions.alertIsPresent()) == null)
			return false;
		else
			return true;
	}

	/*********************************************
	 * Switch To
	 *********************************************/
	public void switchToWindow(String nameOrHandle) throws Exception {
		try {
			driver.switchTo().window(nameOrHandle);
			reporting.report(Status.PASS, "Switched to Window " + nameOrHandle);
		} catch (NoSuchWindowException e) {
			reporting.report(Status.FAIL, "Window " + nameOrHandle + " is not present");
		}
	}

	public void switchToFrame(String nameOrId) throws Exception {
		try {
			driver.switchTo().frame(nameOrId);
			reporting.report(Status.PASS, "Switched to Frame " + nameOrId);
		} catch (NoSuchFrameException e) {
			reporting.report(Status.FAIL, "Frame " + nameOrId + " is not present");
		}
	}

	public void switchToFrame(int index) throws Exception {
		try {
			driver.switchTo().frame(index);
			reporting.report(Status.PASS, "Switched to Frame with index" + index);
		} catch (NoSuchFrameException e) {
			reporting.report(Status.FAIL, "Frame with index " + index + " is not present");
		}
	}

	public void switchToAlert() throws Exception {
		try {
			driver.switchTo().alert();
			reporting.report(Status.PASS, "Switched to Alert window");
		} catch (NoAlertPresentException e) {
			reporting.report(Status.FAIL, "Alert Window is not present");
		}
	}

	/*********************************************
	 * Handling Alerts
	 * 
	 * @throws Exception
	 *********************************************/
	public void acceptAlert() throws Exception {
		try {
			waitForAlertToBePresent();
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			reporting.report(Status.PASS, "Alert is accepted");
		} catch (NoAlertPresentException e) {
			reporting.report(Status.FAIL, "Alert Window is not present");
		}
	}

	public void acceptAlertifExists() throws Exception {
		try {
			waitForAlertToBePresent();
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
			reporting.report(Status.PASS, "Alert is accepted");
		} catch (Exception e) {
			reporting.report(Status.SKIP, "Alert Window is not present");
		}
	}

	public void dismissAlert() throws Exception {
		try {
			waitForAlertToBePresent();
			driver.switchTo().alert().dismiss();
			driver.switchTo().defaultContent();
			reporting.report(Status.PASS, "Alert is dismissed");
		} catch (NoAlertPresentException e) {
			reporting.report(Status.FAIL, "Alert Window is not present");
		}
	}

	/*********************************************
	 * Wait For
	 *********************************************/

	public void waitForElementToBePresent(By by) {
		WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public void waitForAlertToBePresent() {
		WebDriverWait wait = new WebDriverWait(driver, MAX_TIMEOUT);
		wait.until(ExpectedConditions.alertIsPresent());
	}

	/*********************************************
	 * Keyboard/Mouse Actions
	 *********************************************/

	public void moveToElement(By by, String fieldName) throws Exception {
		try {
			Actions act = new Actions(driver);
			act.moveToElement(find(by)).build().perform();
			reporting.report(Status.PASS, "Mouse Hovered on element " + fieldName);
		} catch (Exception e) {
			reporting.report(Status.FAIL, "Caught Exception: " + e.getMessage());
		}
	}

	public void KeyDown(Keys key) throws Exception {
		try {
			Actions act = new Actions(driver);
			act.keyDown(key).build().perform();
			reporting.report(Status.PASS, "KeyDown Keyboard action " + key.toString());
		} catch (Exception e) {
			reporting.report(Status.FAIL, "Caught Exception: " + e.getMessage());
		}
	}

	public void keyUp(Keys key) throws Exception {
		try {
			Actions act = new Actions(driver);
			act.keyUp(key).build().perform();
			reporting.report(Status.PASS, "KeyUp Keyboard action " + key.toString());
		} catch (Exception e) {
			reporting.report(Status.FAIL, "Caught Exception: " + e.getMessage());
		}
	}

	public void PressKey(Keys key) throws Exception {
		try {
			Actions act = new Actions(driver);
			act.keyDown(key).build().perform();
			Thread.sleep(200);
			act.keyUp(key).build().perform();
			reporting.report(Status.PASS, "Pressed Keyboard action " + key.toString());
		} catch (Exception e) {
			reporting.report(Status.FAIL, "Caught Exception: " + e.getMessage());
		}
	}
}
