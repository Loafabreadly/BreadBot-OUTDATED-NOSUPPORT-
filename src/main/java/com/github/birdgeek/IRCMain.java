package com.github.birdgeek;;

public class IRCMain {
	
	public static void setup() {
		
		IRCBot irc = new IRCBot();
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
	
	public static void main(String[] args) throws Exception {
		
		IRCBot irc = new IRCBot();
		irc.setVerbose(true);
		irc.connect("irc.twitch.tv", 6667, ConfigFile.config.getString("Twitch_OAuth"));
		irc.joinChannel("#" + ConfigFile.config.getString("Twitch_Channel"));
	}

	public static boolean shouldEnable() {
		return ConfigFile.getShouldEnable();
	}
}
