package com.github.birdgeek.breadbot.utility;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.github.birdgeek.breadbot.BotMain;
import com.github.birdgeek.breadbot.discord.ChatEvent;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;


public class StatsFile {
	//static PropertiesConfiguration statsFile = new PropertiesConfiguration();
	String fileName = "myStats.cfg";
	static PropertiesConfiguration stats;
	
	public StatsFile ()  {
		try {
			StatsFile.stats = new PropertiesConfiguration(fileName);
		} catch (ConfigurationException e) {
			BotMain.systemLog.warn(e.getMessage());
		}
	}
	
	
	public int getStats(String cmd) {
		return stats.getInt(cmd);
	}
	
	static public void readKeysToConsole() {
		for (int i=0; i < ChatEvent.availableCommands.length; i++) {
			BotMain.discordLog.info(ChatEvent.availableCommands[i].toString() + " = " +stats.getInt(ChatEvent.availableCommands[i]));
		}
	}
	
	static public void readKeys(MessageReceivedEvent e) {
		Message mess = null;
		for (int i=0; i < ChatEvent.availableCommands.length; i++) {
			mess  = new MessageBuilder().appendString(ChatEvent.availableCommands[i].toString() + " = " +stats.getInt(ChatEvent.availableCommands[i])).build();
		}
		e.getChannel().sendMessage(mess);
	}

	public static void readKeys(PrivateMessageReceivedEvent e) {
		Message mess = null;
		for (int i=0; i < ChatEvent.availableCommands.length; i++) {
			mess  = new MessageBuilder().appendString(ChatEvent.availableCommands[i].toString() + " = " +stats.getInt(ChatEvent.availableCommands[i])).build();
		}
		e.getChannel().sendMessage(mess);
	}

	static public void updateCount(String cmd)  {
		int i = stats.getInt(cmd);
		stats.setProperty(cmd, i + 1);
		try {
			stats.save();
		} catch (ConfigurationException e) {
			BotMain.systemLog.warn(e.getMessage());
		}
	}



}
