package org.ozsoft.secs4j.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.ozsoft.secs4j.SecsEquipment;

/**
 * 
 * @author caifenglei
 *
 */
public class ApplicationProperties {

	private static final Logger LOG = Logger.getLogger(SecsEquipment.class);

	private static String PROP_FILE = "app.properties";

	private static Properties props = new Properties();

	public static final String DB_CONNECTION_URL = "db.connectionURL";
	public static final String DB_USER = "db.username";
	public static final String DB_PASSWORD = "db.password";

	public static String getProperty(String propName) {
		
		loadProperties();
		return props.getProperty(propName);
	}

	public static String getDBConnectionURL() {
		return getProperty(DB_CONNECTION_URL);
	}
	
	public static String getDBUser() {
		return getProperty(DB_USER);
	}
	
	public static String getDBPassword() {
		return getProperty(DB_PASSWORD);
	}

	private static void loadProperties() {

		if (props.size() == 0) {

			try {
				ApplicationProperties self = new ApplicationProperties();
				InputStream resourceAsStream = self.getClass().getClassLoader().getResourceAsStream(PROP_FILE);
				props.load(resourceAsStream);
			} catch (IOException e) {
				LOG.error("Unable to load properties file : " + PROP_FILE);
			}
		}
	}

}
