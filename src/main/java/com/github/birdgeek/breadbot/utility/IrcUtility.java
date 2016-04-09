package com.github.birdgeek.breadbot.utility;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class IrcUtility extends ListenerAdapter  {
	
	static String targetChannel;
	
	static String[] ircCommands = {"help", "toggle"};
	
	public IrcUtility() {
		targetChannel = "#" + ConfigFile.getTwitchChannel();
	}
	
	/**
	 * Check to see if user is on ignored list
	 * @param String of user checking for ignored
	 * @return t/f depending on if user is ignored
	 */
	public static boolean isIgnored(String nick) {
		String[] ignoredUsers = ConfigFile.getIgnoredIrcUsers();
		
		for (int i = 0 ; i < ignoredUsers.length; i++) {
			if (ignoredUsers[i].equalsIgnoreCase(nick))
				return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param messageEvent
	 * @return t/f depending on if the line read is a cmd
	 */
	public static boolean isCommand(MessageEvent messageEvent) {
		
		for (String ircCommand : ircCommands) {
			
			if (messageEvent.getMessage().equalsIgnoreCase(ircCommand)) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @return Should we be doing the relay?
	 */
	public static boolean isDoingRelay() {
		return ConfigFile.shouldIrcRelay();
	}

	/**
	 * @return Method for finding is a specific user is admin on Bot
	 */
	public static boolean isApprovedUser(String username) {
		for (int i=0; i < ConfigFile.getApprovedIRCUsers().length; i++) {
			if (username.equalsIgnoreCase(ConfigFile.getApprovedIRCUsers()[i])) {
				return true;
			}
		}
			return false;
	}	
}
 