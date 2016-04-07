package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.IrcUtility;

import net.dv8tion.jda.events.message.MessageReceivedEvent;

public class DiscordToTwitchEvent {
	
	public DiscordToTwitchEvent() {
		
	}
	
	public void onMessageReceived(MessageReceivedEvent e) {
		//TODO Test the Reverse Relay
		if (e.getMessage().getContent().startsWith("^") 
			&& e.getTextChannel().getName().equalsIgnoreCase(ConfigFile.getTwitchDiscordChannelID())
			&& e.getAuthor().getId().equalsIgnoreCase(ConfigFile.getOwnerID())) {
				String contents = e.getMessage().getContent().substring(1);
				IrcUtility.sendMessage(contents);
		}
	}

}
