package com.github.birdgeek.breadbot.discord;

import java.io.IOException;
import java.util.Random;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;

import com.github.birdgeek.breadbot.BotMain;
import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import com.github.birdgeek.breadbot.utility.GoogleSearch;
import com.github.birdgeek.breadbot.utility.StatsFile;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class ChatEvent extends ListenerAdapter {


	JDA jda;
	Random random = new Random();
	long start = BotMain.start;
	String[] approvedUsers = DiscordUtility.getApprovedUsers();
	Logger discordLog;
	
	
	public static String[] availableCommands = {
			"help", "globalhelp", "dev", "ping", "stats", 
			"kill", "flip", "uptime", "currenttime"
			, "reload", "config", "attach", "getChannel", "getServer"
			, "google"
			};
	
	public ChatEvent(JDA jda, Logger discordLog) {
		this.jda = jda;
		this.discordLog = discordLog;
	}
	
	/**
	 * On Discord Message Received
	 * @see net.dv8tion.jda.hooks.ListenerAdapter#onMessageReceived(net.dv8tion.jda.events.message.MessageReceivedEvent)
	 */
	public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
		String username = DiscordUtility.getUsername(e);
					
		switch (e.getMessage().getContent()) {
		
		case "#ping":
			DiscordUtility.delMessage(e);
			e.getChannel().sendMessage("PONG");
			StatsFile.updateCount("ping");
			break;
		
		case "#disconnect":
		case "#kill":
			if (DiscordUtility.isApprovedUser(username)) {

				discordLog.info(e.getAuthor().getUsername() + " has killed the bot!");

				DiscordUtility.delMessage(e);
				StatsFile.updateCount("kill");
				try {
					ConfigFile.config.save();
				} catch (ConfigurationException e1) {
					discordLog.error(e1.getMessage());
				}
				/*
				if (IrcMain.isRunning) {
					IrcMain.irc.close();
				}
				*/
				//jda.shutdown();
				System.exit(0);

			}
			else {
				e.getAuthor().getPrivateChannel().sendMessage("You are not authorized to do that!");
			}			
			break;
			
		case "#flip":
			boolean choice = random.nextBoolean();
			DiscordUtility.delMessage(e);
			StatsFile.updateCount("flip");			
			 if (choice == true) {
				 e.getChannel().sendMessage("Heads!");
			 }
			 else if (choice == false) {
				 e.getChannel().sendMessage("Tails!");
			 }
			 else {
				 e.getChannel().sendMessage( "You don fucked up");
			 }			
			break;
			
		case "#?":
		case "#help":
			DiscordUtility.delMessage(e);
			StatsFile.updateCount("help");			
			DiscordUtility.sendHelp(e);			
			break;
			
		case "#globalhelp":
			DiscordUtility.delMessage(e);
			StatsFile.updateCount("globalhelp");			
			DiscordUtility.sendGlobalHelp(e);			
			break;
			
		case "#uptime":
			DiscordUtility.delMessage(e);
			StatsFile.updateCount("uptime");			
			DiscordUtility.sendUptime(e);		
			break;
			
		case "#currenttime":
			if (DiscordUtility.isApprovedUser(username)) {
				DiscordUtility.delMessage(e);
				StatsFile.updateCount("currenttime");				
				e.getChannel().sendMessage("" + System.nanoTime());
			}
			else {
				e.getAuthor().getPrivateChannel().sendMessage("You are not authorized to do that!");
				}			
			break;
			
		case "#dev":
			DiscordUtility.delMessage(e);
			StatsFile.updateCount("dev");			
			e.getChannel().sendMessage("BreadBot is developed by LoafaBread and all the code can be found at http://birdgeek.github.io/BreadBot/");			
			break;
			
		case "#reload":
			if (DiscordUtility.isApprovedUser(username)) {
				ConfigFile.config.reload();
				StatsFile.updateCount("reload");				
				try {
					
					ConfigFile.config.load();
				} catch (ConfigurationException e1) {

					discordLog.warn(e1.getMessage());
				}
			}
			break;
			
		case "#d":
		case "#debug":
			if (DiscordUtility.isApprovedUser(username)) {
				
				DiscordUtility.printDiagnostics();
				discordLog.trace(e.getAuthor().getUsername() + "  issued the debug command!");
			}
			break;
			
		case "#getchan":
		case "#getchannel":
		case "#getChannel":
				e.getChannel().sendMessage("ID For : '" + e.getChannel().getName() + "' is: " + e.getChannel().getId());
				StatsFile.updateCount("getChannel");
			break;
			
		case "#getserv":
		case "#getserver":
		case "#getServer":
			e.getChannel().sendMessage("ID For : " + e.getGuild().getName() + " is :" + e.getGuild().getId());
			StatsFile.updateCount("getServer");
			break;
		
		case "#attach":
			if (DiscordUtility.isOwner(DiscordUtility.getUsernameID(e))) { //Is it the owner
				e.getChannel().sendMessage("Trying to switch the home channel");
				if (ConfigFile.getHomeChannel().toString().equalsIgnoreCase(e.getChannel().getId())) {
					e.getChannel().sendMessage("**This is already the home channel!**");
				}
				else {
					String oldID = ConfigFile.getHomeChannel();
					jda.getTextChannelById(oldID).sendMessage("*woosh*");
					ConfigFile.config.setProperty("Home_Channel_ID",  e.getChannel().getId());
					if (ConfigFile.getHomeChannel().equalsIgnoreCase(e.getChannel().getId())) {
					e.getChannel().sendMessage("Success! Home channel is now: " + e.getChannel().getName());
					e.getChannel().sendMessage("**Hello world!**");
					discordLog.trace("The owner changed the home channel to: " + e.getChannel().getName());
					}
					else {
					e.getChannel().sendMessage("Failed the check, try setting it manually in the .cfg");
					discordLog.warn("Changing the home channel failed! Tried to change it to: (" + e.getChannel().getId() + "/" 
							+ e.getChannel().getName()
							+ ") and current is: (" + ConfigFile.getHomeChannel() + "/" 
							+ jda.getTextChannelById("" + ConfigFile.getHomeChannel()).getName() + ")");
					}
				}
				StatsFile.updateCount("attach");
			}
			break;
		}
		if (e.getMessage().getContent().length() > 3 && e.getMessage().getContent().substring(0, 3).contentEquals("#g ")) { //Top result
			StatsFile.updateCount("google");
			String search_query = e.getMessage().getContent().substring(3);
			try {
				GoogleSearch.search(search_query, e, false);
			} catch (IOException e1) {
				discordLog.error(e1.getMessage());
			}
			discordLog.trace("A google search was performed!");
		}
		if (e.getMessage().getContent().length() > 4 && e.getMessage().getContent().substring(0, 4).contentEquals("#gs ")) { //Search results
			StatsFile.updateCount("google");
			String search_query = e.getMessage().getContent().substring(4);
			try {
				GoogleSearch.search(search_query, e, true);
			} catch (IOException e1) {
			discordLog.error(e1.getMessage());
			}
		}
	}
	
}
