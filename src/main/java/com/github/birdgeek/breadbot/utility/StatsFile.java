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
	
	String fileName = "myStats.cfg";
	static PropertiesConfiguration stats;
	static StringBuilder sb;
	
	public StatsFile ()  {
		try {
			StatsFile.stats = new PropertiesConfiguration(fileName);
		} catch (ConfigurationException e) {
			BotMain.systemLog.warn(e.getMessage());
		}
	}
	
	/**
	 * 
	 * @param cmd
	 * @return Returns int stat number of given cmd
	 */
	public int getStats(String cmd) {
		return stats.getInt(cmd);
	}
	/**
	 * @return Prints all known stats to discord log
	 */
	static public void readKeysToConsole() {
		for (int i=0; i < ChatEvent.availableCommands.length; i++) {
			BotMain.discordLog.info(ChatEvent.availableCommands[i].toString() + " = " +stats.getInt(ChatEvent.availableCommands[i]));
		}
	}
	/**
	 * @return Reads keys to Text channel
	 * @param messageEvent
	 */
	static public void readKeys(MessageReceivedEvent messageEvent) {
		for (int i=0; i < ChatEvent.availableCommands.length; i++) {
			new MessageBuilder().appendString(ChatEvent.availableCommands[i].toString() + " = " +stats.getInt(ChatEvent.availableCommands[i])).build();
			sb.append(ChatEvent.availableCommands[i].toString() + "=" + stats.getInt(ChatEvent.availableCommands[i] + "\n"));
		}
		messageEvent.getChannel().sendMessage(sb.toString());
	}
	/**
	 * @return Reads keys to Private Channel
	 * @param privateMessageEvent
	 */
	public static void readKeys(PrivateMessageReceivedEvent privateMessageevent) {
		Message mess = null;
		for (int i=0; i < ChatEvent.availableCommands.length; i++) {
			mess  = new MessageBuilder().appendString(ChatEvent.availableCommands[i].toString() + " = " +stats.getInt(ChatEvent.availableCommands[i])).build();
		}
		privateMessageevent.getChannel().sendMessage(mess);
	}
	/**
	 * @return Updates stat count of cmd in "Stats.cfg"
	 * @param cmd
	 */
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
