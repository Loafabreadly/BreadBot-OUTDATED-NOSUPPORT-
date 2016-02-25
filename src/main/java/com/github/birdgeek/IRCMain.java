package com.github.birdgeek;

import java.io.IOException;

import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

public class IRCMain {
	/*
	public static void setup() {
		
		IRCListener irc = new IRCListener();
		if (shouldEnable()) {
			
			irc.doingRelay = true;
			irc.setVerbose(true);
			
			try {
				irc.connect("irc.twitch.tv", 6667, ConfigFile.config.getString("Twitch_OAuth"));
			} catch (Exception e) {
				e.printStackTrace();
			}
			irc.joinChannel(ConfigFile.config.getString("Twitch_Channel"));
		}
		else {
			return;
		}
	}
	*/
	public static void main(String[] args) throws IOException, IrcException {
		
		@SuppressWarnings("deprecation")
		Configuration config = new Configuration.Builder()
				.setName("Birdy_Bot")
				.setServerHostname("irc.twitch.tv")
				.setServerPort(6667)
				.setServerPassword("oauth:ivguqiyw1o9buvsrwybgnk44fr22m8")
				.addAutoJoinChannel("#Birdgeek3")//ConfigFile.config.getString("Twitch_Channel"))
				.addListener(new IRCListener())
				.buildConfiguration();
				
		PircBotX irc = new PircBotX(config);
		irc.startBot();
		
	}

	public static boolean shouldEnable() {
		return ConfigFile.getShouldEnable();
	}
}
