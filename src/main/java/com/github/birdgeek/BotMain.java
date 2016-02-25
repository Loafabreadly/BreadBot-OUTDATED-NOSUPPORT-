package com.github.birdgeek;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;

public class BotMain {
	static long start;
	public static final Logger discordLogger = LogManager.getLogger("Discord Bot");
	public static final Logger ircLogger = LogManager.getLogger("IRC Bot");
	static ConfigFile config;
	static StatsFile stats;
	static JDA jda;
	static String version;
	
	public static void main(String[] args) throws  FileNotFoundException, IOException, ConfigurationException, LoginException, IllegalArgumentException {
		config = new ConfigFile();
		stats = new StatsFile();
		jda = new JDABuilder(ConfigFile.getEmail(), ConfigFile.getPassword()).buildAsync();
		jda.addEventListener(new ChatEvent(jda));
		jda.addEventListener(new APIReadyEvent());
		start = System.currentTimeMillis();
		discordLogger.error("Bot Booted succesfully and logged in");
		IRCMain.setup();
		version =  ConfigFile.config.getString("Version");

	}

	public static void sendWelcome() {
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(new MessageBuilder()
				.appendString("Welcome to Bread Bot!")
				.appendCodeBlock("Version: " + ConfigFile.config.getString("Version"), "java")
				.build());
		if (ConfigFile.config.getBoolean("Send_Welcome_Mention")) {
			jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(new MessageBuilder()
					.appendString("I am being written by ")
					.appendMention(jda.getUsersByName("LoafaBread").get(0))
					.build());
		}
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage("You can read more about me here - http://birdgeek.github.io/BreadBot/");
		//jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage("" + ConfigFile.config.getBoolean("Console_Out"));
		}
	static void setupConsoleOut() {
		if (ConfigFile.config.getBoolean("Console_Out")) {
			new DiscordConsoleStream(jda.getTextChannelById("" + ConfigFile.getHomeChannel()), true);
			DiscordConsoleStream.println("" + ConfigFile.config.getBoolean("Console_Out"));
		}
	}
}
