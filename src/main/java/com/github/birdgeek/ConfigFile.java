package com.github.birdgeek;


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
	
	public static boolean getShouldEnable() {
		return config.getBoolean("Twitch_Enable");
	}
}
