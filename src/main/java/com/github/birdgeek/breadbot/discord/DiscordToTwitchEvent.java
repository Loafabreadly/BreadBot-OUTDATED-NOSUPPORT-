package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.BotMain;
import com.github.birdgeek.breadbot.irc.IrcMain;
import com.github.birdgeek.breadbot.utility.ConfigFile;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class DiscordToTwitchEvent extends ListenerAdapter {
	
	public DiscordToTwitchEvent() {
		
	}
	
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		if (ConfigFile.shouldEnableTwitch()) {
		if (e.getChannel().getId().equalsIgnoreCase(ConfigFile.getTwitchDiscordChannelID())) {
				if (e.getMessage().getContent().charAt(0) == '^') {
					if (e.getAuthor().getId().equalsIgnoreCase(ConfigFile.getOwnerID())) {
			
						String contents = e.getMessage().getContent().substring(1);
						IrcMain.sendMessage(contents);
						BotMain.systemLog.trace("Should have sent: " + contents);
					}
					else {
					BotMain.discordLog.warn("Failed owner");		
					}
				}
		}
		else {
			BotMain.discordLog.warn("Failed Channel");
		}		
	}
	}
}
