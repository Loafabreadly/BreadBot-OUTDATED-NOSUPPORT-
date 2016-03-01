package com.github.birdgeek;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;


public class IRCListener extends ListenerAdapter {

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
		}
		
		/*
		 * Send the IRC message to discord
		 */
		if (doingRelay()) {
			//e.respondChannel("Message Relayed"); //DEBUG
			BotMain.jda.getTextChannelById("" + ConfigFile.config.getBigInteger("Home_Channel_ID"))
			.sendMessage(
					"{" + e.getChannel().getName() + "}" +
					" [" + e.getUser().getNick() + "] " + 
					e.getMessage());
		}
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
