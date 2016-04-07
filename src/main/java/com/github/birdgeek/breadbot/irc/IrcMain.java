package com.github.birdgeek.breadbot.irc;

import java.io.IOException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.output.OutputIRC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.birdgeek.breadbot.utility.ConfigFile;

public class IrcMain {
	public static PircBotX irc;
	static Logger ircLog;
	public static boolean isRunning;
	static OutputIRC output;
	
	
	/*
	 * Main method for creation of IRC Bot
	 */
	public static void setup(Logger log) {
		ircLog = log;
		
		Configuration config = new Configuration.Builder()
				.setName(ConfigFile.getTwitchLoginUser())
				.addServer("irc.twitch.tv", 6667)
				.setServerPassword(ConfigFile.getOAuth())
				.addAutoJoinChannel("#" + ConfigFile.getTwitchChannel())
				.addListener(new ChatListener())
				.buildConfiguration();
				
		irc = new PircBotX(config);
		try {
			irc.startBot();
			output = new OutputIRC(irc);
			isRunning = true;
		} catch (IOException | IrcException e) {
			ircLog.error(e.getMessage());
			isRunning = false;
			
		}
	}
	
	/*
	 * Debug main method for running just the IRC Bot (Should never be used)
	 */
	public static void main(String[] args) {
		
		ircLog = LoggerFactory.getLogger("IRC");
		Configuration config = new Configuration.Builder()
				.setName(ConfigFile.getTwitchLoginUser())
				.addServer("irc.twitch.tv", 6667)
				.setServerPassword(ConfigFile.getOAuth())
				.addAutoJoinChannel("#" + ConfigFile.getTwitchChannel())
				.addListener(new ChatListener())
				.buildConfiguration();
				
		irc = new PircBotX(config);
		try {
			
			irc.startBot();
			output = new OutputIRC(irc);
		} catch (IOException | IrcException e) {
			ircLog.error(e.getMessage());
		}
		
	}

	public static boolean shouldEnable() {
		return ConfigFile.shouldEnableTwitch();
	}
	
	public static void kill() {
		ircLog.trace("Trying to close IRC connection");
		output.quitServer();
		
		if (!irc.isConnected()) 
			ircLog.trace("Succesfully closed IRC connection");
		else 
			ircLog.warn("Didn't close out - force shutting down program");
		System.exit(2);
	}

}
