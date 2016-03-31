package com.github.birdgeek;

import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.InviteReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.InviteUtil;

public class InviteEvent extends ListenerAdapter {
	
<<<<<<< HEAD
	public void onInviteReceived(InviteReceivedEvent event) {
		if (isApprovedUser(event.getAuthor().getUsername())) {
			InviteUtil.join(event.getInvite(), BotMain.jda, null);
=======
	public void onInviteReceived(InviteReceivedEvent event) { //TODO Test this out
		if (isApprovedUser(event.getAuthor().getUsername())) {
			InviteUtil.join(event.getInvite(), BotMain.jda, null); //ERROR May error out on Consumable
>>>>>>> 83ccf321d3a72923b2cc63704369724a738d8c88
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


	@Override
	public void onEvent(Event arg0) {
		// TODO Auto-generated method stub
		
	}
}
