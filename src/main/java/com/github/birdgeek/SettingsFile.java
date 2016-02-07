package com.github.birdgeek;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class SettingsFile {
	static String filename = "config.cfg";
	static PropertiesConfiguration config;
	
	public SettingsFile () throws ConfigurationException {
		SettingsFile.config = new PropertiesConfiguration(filename);
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
}
