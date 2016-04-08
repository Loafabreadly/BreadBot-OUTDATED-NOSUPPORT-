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
			"disconnect", "kill", "flip", "uptime", "currenttime"
			, "reload", "config", "attach", "getChannel", "getServer"
			};
	
	public ChatEvent(JDA jda, Logger discordLog) {
		this.jda = jda;
		this.discordLog = discordLog;
	}
	
	/*
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
				 e.getChannel().sendMessage("Heads!");
			 }
			 else if (choice == false) {
				 e.getChannel().sendMessage("Tails!");
			 }
			 else {
				 e.getChannel().sendMessage( "You don fucked up");
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
			
		case "#debug":
			if (DiscordUtility.isApprovedUser(username)) {
				
				DiscordUtility.printDiagnostics();
				discordLog.trace(e.getAuthor().getUsername() + "  issued the debug command!");
			}
			break;
		
		case "#getChannel":
				e.getAuthor().getPrivateChannel().sendMessage("ID For : '" + e.getChannel().getName() + "' is: " + e.getChannel().getId());
			break;
		case "#getServer":
			e.getAuthor().getPrivateChannel().sendMessage("ID For : " + e.getGuild().getName() + " is :" + e.getGuild().getId());
			break;
		
		case "#attach":
			if (DiscordUtility.isOwner(DiscordUtility.getUsernameID(e))) {
				e.getChannel().sendMessage("Trying to switch the home channel");
				if (ConfigFile.getHomeChannel().toString().equalsIgnoreCase(e.getChannel().getId())) {
					e.getChannel().sendMessage("**This is already the home channel!**");
				}
				else {
					ConfigFile.config.setProperty("Home_Channel_ID",  e.getChannel().getId());
					if (ConfigFile.getHomeChannel().toString().equalsIgnoreCase(e.getChannel().getId())) {
					e.getChannel().sendMessage("Success! Home channel is now: " + e.getChannel().getName());
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
			}
			break;
		}
	}
	
}
