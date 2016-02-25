package com.github.birdgeek;


import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigFile {
	static String filename = "myConfig.cfg";
	static String defaultFileName = "target/config.cfg";
	static PropertiesConfiguration config;
	
	public ConfigFile () throws ConfigurationException {
		ConfigFile.config = new PropertiesConfiguration(filename);
	}
	
	public static String[] getApprovedUsers() {
		return config.getStringArray("Approved_Users");
	}
	
	public static String getEmail() throws ConfigurationException {
		return config.getString("Email");
	}
	public static String getPassword() throws ConfigurationException {
		return config.getString("Password");
	}
	public static BigInteger getHomeServer() {
		return config.getBigInteger("Home_Server_ID");
	}
	public static BigInteger getHomeChannel() {
		return config.getBigInteger("Home_Channel_ID");
	}
	static void startup() throws IOException {
		if (config ==  null) {
				FileWriter write = new FileWriter(defaultFileName);
				write.flush();
		}
		else {
			
			try {
				config.load(defaultFileName);
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
}
