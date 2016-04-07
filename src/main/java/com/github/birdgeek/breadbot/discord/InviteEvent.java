package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.utility.DiscordUtility;

import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;

public class InviteEvent extends ListenerAdapter {

	public void onInviteReceived(InviteReceivedEvent event) { //TODO Test this out
		if (DiscordUtility.isApprovedUser(event.getAuthor().getUsername())) {
			InviteUtil.join(event.getInvite(), DiscordMain.jda, null); //ERROR May error out on Consumable

		}
		
	}

}
