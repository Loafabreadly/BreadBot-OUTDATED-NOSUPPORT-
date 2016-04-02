package com.github.birdgeek.breadbot;


import java.math.BigInteger;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ConfigFile {
	static String filename = "myConfig.cfg";
	public static PropertiesConfiguration config;
	
	public ConfigFile () throws ConfigurationException {
		ConfigFile.config = new PropertiesConfiguration(filename);
	}
	
	/*
	 * String Arrays
	 */
	public static String[] getApprovedUsers() {
		return config.getStringArray("Approved_Users");
	}
	
	public static String[] getApprovedIRCUsers() {
		return config.getStringArray("Approved_IRC_Users");
	}
	
	public static String[] getIgnoredIrcUsers() {
		return config.getStringArray("Ignored_IRC_Users");
	}
	/*
	 * Strings
	 */
	public static String getEmail() throws ConfigurationException {
		return config.getString("Email");
	}
	
	public static String getPassword() throws ConfigurationException {
		return config.getString("Password");
	}
	
	public static String getVersion() {
		return config.getString("Version");
	}
	
	public static String getTwitchChannel() {
		return config.getString("Twitch_Channel");
	}
	
	public static String getOAuth() {
		return config.getString("Twitch_OAuth");
	}
	
	public static String getTwitchLoginUser() {
		return config.getString("Twitch_Login_User");
	}
	/*
	 * Booleans
	 */
	public static boolean shouldEnableTwitch() {
		return config.getBoolean("Twitch_Enable");
	}
	
	public static boolean shouldDelete() {
		return config.getBoolean("delcmd");
	}
	
	public static boolean shouldSendWelcomeMention() {
		return config.getBoolean("Send_Welcome_Mention");
	}	
	public static boolean shouldIrcRelay() {
		return config.getBoolean("IRC_Relay");
	}
	public static void setIrcRelay(boolean value) {
		config.setProperty("IRC_Relay", value);
	}
	
	/*
	 * Ints
	 */
	public static BigInteger getHomeGuild() {
		return config.getBigInteger("Home_Guild_ID");
	}
	
	public static BigInteger getHomeChannel() {
		return config.getBigInteger("Home_Channel_ID");
	}	
	
	public static BigInteger getOwnerID() {
		return config.getBigInteger("Owner_ID");
	}

}
