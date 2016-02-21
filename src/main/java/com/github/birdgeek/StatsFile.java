package com.github.birdgeek;


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import net.dv8tion.jda.events.message.MessageReceivedEvent;


public class StatsFile {
	//static PropertiesConfiguration statsFile = new PropertiesConfiguration();
	String fileName = "myStats.cfg";
	static PropertiesConfiguration config;
	
	public StatsFile () throws ConfigurationException {
		StatsFile.config = new PropertiesConfiguration(fileName);
	}
	
	
	public int getStats(String cmd) {
		return config.getInt(cmd);
	}
	static public void readKeys() {
		for (int i=0; i < ChatEvent.availableCommands.length; i++) {
			System.out.println(ChatEvent.availableCommands[i].toString() + " = " +config.getInt(ChatEvent.availableCommands[i]));
		}
	}
	static public void readKeys(MessageReceivedEvent e) {
		for (int i=0; i < ChatEvent.availableCommands.length; i++) {
			e.getAuthor().getPrivateChannel().sendMessage(ChatEvent.availableCommands[i].toString() + " = " +config.getInt(ChatEvent.availableCommands[i]));
		}
	}
	static public void updateCount(String cmd) throws ConfigurationException {
		int i = config.getInt(cmd);
		config.setProperty(cmd, i + 1);
		config.save();
	}

}
