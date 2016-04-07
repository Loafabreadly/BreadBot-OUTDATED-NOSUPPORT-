package com.github.birdgeek.breadbot.discord;

import java.util.Random;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;

import com.github.birdgeek.breadbot.BotMain;
import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import com.github.birdgeek.breadbot.utility.StatsFile;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class ChatEvent extends ListenerAdapter {


	JDA jda;
	Random random = new Random();
	long start = BotMain.start;
	String[] approvedUsers = DiscordUtility.getApprovedUsers();
	Logger discordLog;
	
	
	public static String[] availableCommands = {
			"help", "globalhelp", "dev", "ping", "stats", 
			"disconnect", "kill", "flip", "uptime", "currenttime"
			, "reload", "config", "attach", "getChannel"
			};
	
	public ChatEvent(JDA jda, Logger discordLog) {
		this.jda = jda;
		this.discordLog = discordLog;
	}
	
	/*
	 * On Discord Message Received
	 * @see net.dv8tion.jda.hooks.ListenerAdapter#onMessageReceived(net.dv8tion.jda.events.message.MessageReceivedEvent)
	 */
	public void onMessageReceived(MessageReceivedEvent e) {
		String username = DiscordUtility.getUsername(e);
					
		switch (e.getMessage().getContent()) {
		
		case "#ping":
			DiscordUtility.delMessage(e);
			e.getTextChannel().sendMessage("PONG");
			StatsFile.updateCount("ping");
			break;
		
		case "#disconnect":
			if (DiscordUtility.isApprovedUser(username)) {

				discordLog.info(e.getAuthor().getUsername() + " has stopped the bot!");

				DiscordUtility.delMessage(e);
				StatsFile.updateCount("disconnect");
				jda.shutdown();
			}
			else {
				e.getAuthor().getPrivateChannel().sendMessage("You are not authorized to do that!");
				}			
			break;
			
		case "#kill":
			if (DiscordUtility.isApprovedUser(username)) {

				discordLog.info(e.getAuthor().getUsername() + " has killed the bot!");

				DiscordUtility.delMessage(e);
				StatsFile.updateCount("kill");
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
				 e.getTextChannel().sendMessage("Heads!");
			 }
			 else if (choice == false) {
				 e.getTextChannel().sendMessage("Tails!");
			 }
			 else {
				 e.getTextChannel().sendMessage( "You don fucked up");
			 }			
			break;
			
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
				e.getTextChannel().sendMessage("" + System.nanoTime());
			}
			else {
				e.getAuthor().getPrivateChannel().sendMessage("You are not authorized to do that!");
				}			
			break;
			
		case "#dev":
			DiscordUtility.delMessage(e);
			StatsFile.updateCount("dev");			
			e.getTextChannel().sendMessage("BreadBot is developed by LoafaBread and all the code can be found at http://birdgeek.github.io/BreadBot/");			
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
			
		case "#debug":
			if (DiscordUtility.isApprovedUser(username)) {
				
				DiscordUtility.printDiagnostics();
				discordLog.trace(e.getAuthor().getUsername() + "  issued the debug command!");
			}
			break;
		
		case "#getChannel":
				e.getAuthor().getPrivateChannel().sendMessage("ID For : '" + e.getTextChannel().getName() + "' is: " + e.getTextChannel().getId());
			break;
		
		case "#attach":
			if (DiscordUtility.isOwner(DiscordUtility.getUsernameID(e))) {
				e.getChannel().sendMessage("Trying to switch the home channel");
				if (ConfigFile.getHomeChannel().toString().equalsIgnoreCase(e.getTextChannel().getId())) {
					e.getTextChannel().sendMessage("**This is already the home channel!**");
				}
				else {
					ConfigFile.config.setProperty("Home_Channel_ID",  e.getTextChannel().getId());
					if (ConfigFile.getHomeChannel().toString().equalsIgnoreCase(e.getTextChannel().getId())) {
					e.getChannel().sendMessage("Success! Home channel is now: " + e.getTextChannel().getName());
					discordLog.trace("The owner changed the home channel to: " + e.getTextChannel().getName());
					}
					else {
					e.getChannel().sendMessage("Failed the check, try setting it manually in the .cfg");
					discordLog.warn("Changing the home channel failed! Tried to change it to: (" + e.getTextChannel().getId() + "/" 
							+ e.getTextChannel().getName()
							+ ") and current is: (" + ConfigFile.getHomeChannel() + "/" 
							+ jda.getTextChannelById("" + ConfigFile.getHomeChannel()).getName() + ")");
					}
				}
			}
			break;
		}
	}
	
}
