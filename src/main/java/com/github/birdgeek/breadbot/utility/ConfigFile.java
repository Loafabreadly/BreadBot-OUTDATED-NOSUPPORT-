package com.github.birdgeek.breadbot.utility;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;

import com.github.birdgeek.breadbot.BotMain;

public class ConfigFile {
	static String filename = "myConfig.cfg"; //TODO Set this different for releases
	public static PropertiesConfiguration config;
	static Logger systemLog;
	
	public ConfigFile (Logger log)  {
		ConfigFile.systemLog = log;
		try {
			ConfigFile.config = new PropertiesConfiguration(filename);
		} catch (ConfigurationException e) {
			BotMain.systemLog.warn(e.getMessage());
		}
	}
	
	public static void save() {
		try {
			config.save();
		} catch (ConfigurationException e) {
			systemLog.error(e.getMessage());
		}
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
	public static String getHomeGuild() {
		return config.getBigInteger("Home_Guild_ID").toString();
	}
	
	public static String getHomeChannel() {
		return config.getBigInteger("Home_Channel_ID").toString();
	}	
	
	public static String getOwnerID() {
		return config.getBigInteger("Owner_ID").toString();
	}
	
	public static String getTwitchDiscordChannelID() {
		return config.getBigInteger("Twitch_Discord_Channel_ID").toString();
	}

}
