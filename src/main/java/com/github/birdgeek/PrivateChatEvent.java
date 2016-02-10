package com.github.birdgeek;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class PrivateChatEvent extends ListenerAdapter {
	
	JDA api;
	
	public PrivateChatEvent(JDA jda) {
		this.api = jda;
	}
	
	public void onPrivateMessageReceivedEvent(PrivateMessageReceivedEvent e) {
			if (e.getAuthor().getUsername().equalsIgnoreCase(SettingsFile.config.getString("Owner"))) {
				String message = e.getMessage().getContent();
				
				if (message.equalsIgnoreCase("#stats")) {
					displayStats(e);
				}
				/*
				switch (message) {
				case "#config edit":
					startEditing(e);
					break;
				case "#displayStats":
					e.getAuthor().getPrivateChannel().sendMessage("Test");
					displayStats(e);
					break;
				}
			}
			*/
			e.getAuthor().getPrivateChannel().sendMessage("Sorry you are not the owner");
		}
	}

	@SuppressWarnings("unchecked")
	private static void displayStats(PrivateMessageReceivedEvent e) {
		e.getChannel().sendMessage("Bread Bot Stats:");
		e.getChannel().sendMessage("----------------------------------------");
	
		StatsFile.config.getKeys().forEachRemaining((Consumer) e.getAuthor().getPrivateChannel().sendMessage(StatsFile.config.getKeys().toString()));
	}

	private void startEditing(PrivateMessageReceivedEvent e) {
		
	}

}
