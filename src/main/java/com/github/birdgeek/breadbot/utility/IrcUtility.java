package com.github.birdgeek.breadbot.utility;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.output.OutputIRC;

public class IrcUtility {
	static PircBotX irc;
	static OutputIRC output;
	static String targetChannel;
	
	static String[] ircCommands = {"help", "toggle"};
	
	public IrcUtility(PircBotX irc, OutputIRC output) {
		IrcUtility.irc = irc;
		IrcUtility.output = output;
		targetChannel = "#" + ConfigFile.getTwitchChannel();
	}

	public static boolean isIgnored(String nick) {
		String[] ignoredUsers = ConfigFile.getIgnoredIrcUsers();
		
		for (int i = 0 ; i < ignoredUsers.length; i++) {
			if (ignoredUsers[i].equalsIgnoreCase(nick))
				return true;
		}
		
		return false;
	}

	//Should we relay the command?
	public static boolean isCommand(MessageEvent e) {
		
		for (String ircCommand : ircCommands) {
			
			if (e.getMessage().equalsIgnoreCase(ircCommand)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	/*
	 * Should we be relaying the IRC chat to the discord channel?
	 */
	public static boolean isDoingRelay() {
		return ConfigFile.shouldIrcRelay();
	}

	/*
	 * Method for finding is a specific user is admin on Bot
	 */
	public static boolean isApprovedUser(String username) {
		for (int i=0; i < ConfigFile.getApprovedIRCUsers().length; i++) {
			if (username.equalsIgnoreCase(ConfigFile.getApprovedIRCUsers()[i])) {
				return true;
			}
		}
			return false;
	}	

	public static void sendMessage(String contents) {
		
		//irc.getUserBot().getBot().sendIRC().message("#" + ConfigFile.getTwitchChannel(), contents); //DEBUG Old Message using IRC Direct
		output.message(targetChannel, contents);
		}
}
 