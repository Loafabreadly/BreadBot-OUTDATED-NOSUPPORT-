package com.github.birdgeek;

import org.pircbotx.hooks.ListenerAdapter;

public class IRCListener extends ListenerAdapter {
	public boolean doingRelay = false;

	public IRCListener() {
	}
	/*
	@Override
	public void onMessage(String channel, String sender ,String login, String hostname, String message) {
		sendMessage(channel, "test");
	
		BotMain.ircLogger.error("Message Found!");
		if (doingRelay) {
			BotMain.jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(message);
			BotMain.ircLogger.error("Message sent!");
		}
	}
	*/
}
