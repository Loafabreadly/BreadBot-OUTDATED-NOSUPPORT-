package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.utility.DiscordUtility;

import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.events.guild.GuildJoinEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;

public class InviteEvent extends ListenerAdapter {

	public void onInviteReceived(InviteReceivedEvent event) { 
		if (DiscordUtility.isApprovedUser(event.getAuthor().getUsername())) {
			InviteUtil.join(event.getInvite(), DiscordMain.jda, null);
			DiscordMain.discordLog.info("Joined a new server named: " + event.getInvite().getGuildName());
		}
		
	}
	
	public void onGuildJoin(GuildJoinEvent event) {
		event.getJDA().getTextChannelById(event.getGuild().getPublicChannel().getId()).sendMessage("Hello World! I am **Bread Bot**");
	}

}
