package com.github.birdgeek;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;


public class StatsFile {
	//static PropertiesConfiguration statsFile = new PropertiesConfiguration();
	String fileName = "myStats.cfg";
	static PropertiesConfiguration stats;
	
	public StatsFile () throws ConfigurationException {
		StatsFile.stats = new PropertiesConfiguration(fileName);
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
	
	static public void updateCount(String cmd) throws ConfigurationException {
		int i = stats.getInt(cmd);
		stats.setProperty(cmd, i + 1);
		stats.save();
	}

}
