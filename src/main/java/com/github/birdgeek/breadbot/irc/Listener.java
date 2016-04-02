package com.github.birdgeek.breadbot.irc;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import com.github.birdgeek.breadbot.BotMain;
import com.github.birdgeek.breadbot.ConfigFile;


public class Listener extends ListenerAdapter {

	String[] ircCommands = {"help", "toggle"};
	/*
	 * On IRC Message Received
	 * @see org.pircbotx.hooks.ListenerAdapter#onMessage(org.pircbotx.hooks.events.MessageEvent)
	 */
	public void onMessage(MessageEvent e) {
		
		/*
		 * Toggle between IRC relay on/off - Must be approved User
		 */
		if (e.getMessage().equalsIgnoreCase("#toggle") && isApprovedUser(e.getUser().getNick())) {
			boolean relay = ConfigFile.shouldIrcRelay();
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
	public boolean doingRelay() {
		return ConfigFile.shouldIrcRelay();
	}

	/*
	 * Method for finding is a specific user is admin on Bot
	 */
	private boolean isApprovedUser(String username) {
		for (int i=0; i < ConfigFile.getApprovedIRCUsers().length; i++) {
			if (username.equalsIgnoreCase(ConfigFile.getApprovedIRCUsers()[i])) {
				return true;
			}
		}
			return false;
	}	
}
