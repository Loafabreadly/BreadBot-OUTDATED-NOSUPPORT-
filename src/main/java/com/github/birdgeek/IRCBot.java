package com.github.birdgeek;

import org.jibble.pircbot.PircBot;

public class IRCBot extends PircBot {
	public boolean doingRelay = false;

	public IRCBot() {
		this.setName("Birdy_Bot");
	}
	
	public void onMessage(String channel, String sender ,String login, String hostname, String message) {
		sendMessage(channel, "test");
	
		BotMain.ircLogger.error("Message Found!");
		if (doingRelay) {
			BotMain.jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(message);
			BotMain.ircLogger.error("Message sent!");
		}
	}
}
