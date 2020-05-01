package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

public class GenericReusbales {
	public final static int DEFAULT_MAX_TIMEOUT = 10;
	public static int MAX_TIMEOUT = DEFAULT_MAX_TIMEOUT;
	public static String reportTable = "";
	public static Logger log;
	public Properties prop = new Properties();
	public InputStream input;

	public String getConfig(String key) throws IOException {
		input = new FileInputStream("./config.properties");
		prop.load(input);
		return prop.getProperty(key);
	}

	public static String timeStamp() {
		// Create object of SimpleDateFormat class and decide the format
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		// get current date time with Date()
		Date date = new Date();

		// Now format the date
		String date1 = dateFormat.format(date);
		date1 = date1.replace("/", "");
		date1 = date1.replace(":", "");
		date1 = date1.replace(" ", "");
		return date1;
	}

	public static void createFolder(String path) {
		File directory = new File(path);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	public static Date getNow() {
		long millis = System.currentTimeMillis();
		java.util.Date date = new java.util.Date(millis);
		return date;
	}

	public String getRandomNumber(int noDigits) {
		int ll = (int) java.lang.Math.pow(10, noDigits - 1);
		int ul = (int) java.lang.Math.pow(10, noDigits) - ll;
		return "" + ((int) (ll + new java.util.Random().nextFloat() * ul));
	}

	public static String getTime() {
		// Create object of SimpleDateFormat class and decide the format
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		// get current date time with Date()
		Date date = new Date();

		// Now format the date
		String date1 = dateFormat.format(date);
		return date1;
	}

	public boolean ifFileExists(String filePath) {
		File f = new File(filePath);
		if (f.exists() && !f.isDirectory()) {
			return true;
		}
		return false;
	}

}
