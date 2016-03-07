package com.github.birdgeek;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.security.auth.login.LoginException;

import org.apache.commons.configuration.ConfigurationException;
import org.pircbotx.exception.IrcException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.utils.SimpleLog;

public class BotMain {
	static long start;
	static ConfigFile config;
	static StatsFile stats;
	static JDA jda;
	static String version;
	static SimpleLog discordLog;
	static SimpleLog ircLog;
	
	/*
	 * Main method  for all bots
	 */
	public static void main(String[] args) throws  FileNotFoundException, IOException, ConfigurationException, LoginException, IllegalArgumentException, InterruptedException {
		config = new ConfigFile();
		stats = new StatsFile();
		start = System.currentTimeMillis();
		version =  ConfigFile.config.getString("Version");
		discordLog = SimpleLog.getLog("Discord Log");
		ircLog = SimpleLog.getLog("IRC Log");
		discordLog.setLevel(SimpleLog.Level.DEBUG);
		discordLog.debug("test");
		
		jda = new JDABuilder(ConfigFile.getEmail(), ConfigFile.getPassword())
				.addListener(new APIReadyEvent())
				.addListener(new ChatEvent(jda))
				.addListener(new InviteEvent())
				.buildAsync(); //Builds the discord bot

		if (ConfigFile.config.getBoolean("Twitch_Enable")) { //Should we enable the IRC portion?
				try { //Tries to build IRC bot
					IRCMain.setup();
				} catch (IrcException e) {
					e.printStackTrace();
				}
		}
	}

	/*
	 * Sends on connect welcome to home discord channel
	 */
	public static void sendWelcome() {
		
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(new MessageBuilder()
				.appendCodeBlock("Welcome to Bread Bot! \n"
						+ "Version: " + ConfigFile.config.getString("Version")
						, "java")
				.build());
		
		if (ConfigFile.config.getBoolean("Send_Welcome_Mention")) { //Should we mention the Owner
			jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(new MessageBuilder()
					.appendString("I am being run by ")
					.appendMention(jda.getUsersByName(ConfigFile.config.getString("Owner")).get(0))
					.build());
		}
		
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage("You can read more about me here - http://birdgeek.github.io/BreadBot/");
	}
	
}
