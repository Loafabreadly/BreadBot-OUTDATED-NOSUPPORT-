package com.github.birdgeek;

import java.io.IOException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

public class IRCMain {
	static PircBotX irc;
	
	/*
	 * Main method for creation of IRC Bot
	 */
	public static void setup() throws IOException, IrcException {
		@SuppressWarnings("deprecation")
		Configuration config = new Configuration.Builder()
				.setName("Birdy_Bot")
				.setServerHostname("irc.twitch.tv")
				.setServerPort(6667)
				.setServerPassword(ConfigFile.config.getString("Twitch_OAuth"))
				.addAutoJoinChannel("#" + ConfigFile.config.getString("Twitch_Channel"))
				.addListener(new IRCListener())
				.buildConfiguration();
				
		irc = new PircBotX(config);
		irc.startBot();
	}
	
	/*
	 * Debug main method for running just the IRC Bot (Should never be used)
	 */
	public static void main(String[] args) throws IOException, IrcException {
		
		@SuppressWarnings("deprecation")
		Configuration config = new Configuration.Builder()
				.setName("Birdy_Bot")
				.setServerHostname("irc.twitch.tv")
				.setServerPort(6667)
				.setServerPassword(ConfigFile.config.getString("Twitch_OAuth"))
				.addAutoJoinChannel("#" + ConfigFile.config.getString("Twitch_Channel"))
				.addListener(new IRCListener())
				.buildConfiguration();
				
		irc = new PircBotX(config);
		irc.startBot();
		
	}

	public static boolean shouldEnable() {
		return ConfigFile.getShouldEnable();
	}

}
