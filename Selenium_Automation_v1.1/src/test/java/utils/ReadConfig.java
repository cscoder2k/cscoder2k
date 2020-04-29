package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadConfig {

	public Properties prop = new Properties();
	public InputStream input;

	public String getConfig(String key) throws IOException {
		input = new FileInputStream("./config.properties");
		prop.load(input);
		return prop.getProperty(key);
	}
}
