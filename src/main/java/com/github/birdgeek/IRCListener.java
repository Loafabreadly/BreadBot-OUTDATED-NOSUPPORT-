package com.github.birdgeek;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;


public class IRCListener extends ListenerAdapter {

	String[] ircCommands = {"help", "toggle", "h"};
	/*
	 * On IRC Message Received
	 * @see org.pircbotx.hooks.ListenerAdapter#onMessage(org.pircbotx.hooks.events.MessageEvent)
	 */
	public void onMessage(MessageEvent e) {
		
		/*
		 * Toggle between IRC relay on/off - Must be approved User
		 */
		if (e.getMessage().equalsIgnoreCase("#toggle") && isApprovedUser(e.getUser().getNick())) {
			boolean relay = ConfigFile.config.getBoolean("IRC_Relay");
			ConfigFile.config.setProperty("IRC_Relay", !relay);
			e.respondChannel("Now doing relay = " + doingRelay()); //DEBUG
			BotMain.ircLog.info("Relay is now: " + doingRelay());
		}
		
		/*
		 * Send the IRC message to discord
		 */
		if (doingRelay()) {
			if (!isCommand(e)) {
				BotMain.jda.getTextChannelById("" + ConfigFile.config.getBigInteger("IRC_Channel_ID"))
				.sendMessage(
						"{" + e.getChannel().getName() + "}" +
						" [" + e.getUser().getNick() + "] " + 
						e.getMessage());
			}
		}
	}
	
	//Should we relay the command?
	private boolean isCommand(MessageEvent e) {
		
		for (int i=0; i<ircCommands.length; i++) {
			
			if (e.getMessage().equalsIgnoreCase(ircCommands[i])) {
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
	public boolean doingRelay() {
		return ConfigFile.config.getBoolean("IRC_Relay");
	}

	/*
	 * Method for finding is a specific user is admin on Bot
	 */
	private boolean isApprovedUser(String username) {
		for (int i=0; i < ConfigFile.config.getStringArray("Approved_IRC_Users").length; i++) {
			if (username.equalsIgnoreCase(ConfigFile.config.getStringArray("Approved_IRC_Users")[i])) {
				return true;
			}
		}
			return false;
	}	
}
