package com.github.birdgeek;

import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.utils.InviteUtil;

public class InviteEvent {
	
	public void onInviteReceived(InviteReceivedEvent event) {
		if (isApprovedUser(event.getAuthor().getUsername())) {
			InviteUtil.join(event.getInvite(), BotMain.jda);
		}
		
	}

	
	private boolean isApprovedUser(String username) {
		for (int i=0; i < ConfigFile.config.getStringArray("Owner").length; i++) {
		
			if (username.equalsIgnoreCase(ConfigFile.config.getStringArray("Owner")[i])) {
				return true;
			}
		}
			return false;
	}
}
