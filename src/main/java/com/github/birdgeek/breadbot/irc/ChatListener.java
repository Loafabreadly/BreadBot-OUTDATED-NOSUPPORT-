package com.github.birdgeek.breadbot.irc;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import com.github.birdgeek.breadbot.discord.DiscordMain;
import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.IrcUtility;


public class ChatListener extends ListenerAdapter {

	
	/*
	 * On IRC Message Received
	 * @see org.pircbotx.hooks.ListenerAdapter#onMessage(org.pircbotx.hooks.events.MessageEvent)
	 */
	public void onMessage(MessageEvent e) {
		
		if (!IrcUtility.isIgnored(e.getUser().getNick())) {
		/*
		 * Toggle between IRC relay on/off - Must be approved User
		 */
			if (e.getMessage().equalsIgnoreCase("#toggle") && IrcUtility.isApprovedUser(e.getUser().getNick())) {
				boolean relay = ConfigFile.shouldIrcRelay();
				ConfigFile.setIrcRelay(!relay);
				e.respondChannel("Now doing relay = " + IrcUtility.isDoingRelay()); //DEBUG Test this out
				IrcMain.ircLog.info("Relay is now: " + IrcUtility.isDoingRelay());
			}
		
		/*
		 * Send the IRC message to discord
		 */
			if (IrcUtility.isDoingRelay()) {
				if (!IrcUtility.isCommand(e)) {
					DiscordMain.jda.getTextChannelById(ConfigFile.getTwitchDiscordChannelID())
					.sendMessage(
							"{" + e.getChannel().getName() + "}" +
							" [" + e.getUser().getNick() + "] " + 
							e.getMessage());
				}	
			}
		}
	}
	}
