package com.github.birdgeek;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;

public class BotMain {
	static long start;
	public static final Logger discordLogger = LogManager.getLogger("Discord Bot");
	public static final Logger ircLogger = LogManager.getLogger("IRC Bot");
	static SettingsFile config;
	static StatsFile stats;
	
	public static void main(String[] args) throws  FileNotFoundException, IOException, ConfigurationException, LoginException, IllegalArgumentException {
		
		config = new SettingsFile();
		stats = new StatsFile();
		JDA jda = new JDABuilder(SettingsFile.getEmail(), SettingsFile.getPassword()).build();
		jda.addEventListener(new ChatEvent(jda));
		start = System.currentTimeMillis();
		discordLogger.info("Bot Booted succesfully and logged in");
		StatsFile.readKeys();
	}

}
