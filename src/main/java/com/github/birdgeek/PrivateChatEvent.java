package com.github.birdgeek;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class PrivateChatEvent extends ListenerAdapter {
	
	JDA api;
	
	public PrivateChatEvent(JDA jda) {
		this.api = jda;
	}
	
	public void onPrivateMessageReceivedEvent(PrivateMessageReceivedEvent e) {
		if (e.getMessage().isPrivate()) {
			if (e.getAuthor().getUsername().equalsIgnoreCase(SettingsFile.config.getString("Owner"))) {
				String message = e.getMessage().getContent();
				
				switch (message) {
				case "#config edit":
					startEditing(e);
					break;
				case "#displayStats":
					displayStats(e);
					break;
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void displayStats(PrivateMessageReceivedEvent e) {
		e.getChannel().sendMessage("Bread Bot Stats:");
		e.getChannel().sendMessage("----------------------------------------");
	
		StatsFile.config.getKeys().forEachRemaining(new MyConsumer(e));
	}

	private void startEditing(PrivateMessageReceivedEvent e) {
		
	}

}
