package com.github.birdgeek;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.apache.commons.configuration.ConfigurationException;
import org.pircbotx.exception.IrcException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;

public class BotMain {
	static long start;
	static ConfigFile config;
	static StatsFile stats;
	static JDA jda;
	static String version;
	
	/*
	 * Main method  for all bots
	 */
	public static void main(String[] args) throws  FileNotFoundException, IOException, ConfigurationException, LoginException, IllegalArgumentException {
		config = new ConfigFile();
		stats = new StatsFile();
		jda = new JDABuilder(ConfigFile.getEmail(), ConfigFile.getPassword()).buildAsync(); //Builds the discord bot
		jda.addEventListener(new ChatEvent(jda));
		jda.addEventListener(new APIReadyEvent());
		start = System.currentTimeMillis();
		try { //Tries to build IRC bot
			IRCMain.setup();
		} catch (IrcException e) {
			e.printStackTrace();
		}
		version =  ConfigFile.config.getString("Version");

	}

	/*
	 * Sends on connect welcome to home discord channel
	 */
	public static void sendWelcome() {
		
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(new MessageBuilder()
				.appendString("Welcome to Bread Bot!")
				.appendCodeBlock("Version: " + ConfigFile.config.getString("Version"), "java")
				.build());
		
		if (ConfigFile.config.getBoolean("Send_Welcome_Mention")) { //Should we mention the Owner
			jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(new MessageBuilder()
					.appendString("I am being written by ")
					.appendMention(jda.getUsersByName(ConfigFile.config.getString("Owner")).get(0))
					.build());
		}
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage("You can read more about me here - http://birdgeek.github.io/BreadBot/");
		}
	/*
	 * Redirects all System.out to home discord channel
	 */
	static void setupConsoleOut() {
		if (ConfigFile.config.getBoolean("Console_Out")) {
			new DiscordConsoleStream(jda.getTextChannelById("" + ConfigFile.getHomeChannel()), true);
			DiscordConsoleStream.println("" + ConfigFile.config.getBoolean("Console_Out"));
		}
	}
}
