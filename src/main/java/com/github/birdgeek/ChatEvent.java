package com.github.birdgeek;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import org.apache.commons.configuration.ConfigurationException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.utils.SimpleLog;

public class ChatEvent extends ListenerAdapter {

	JDA jda;
	Random random = new Random();
	long start = BotMain.start;
	String[] approvedUsers = getApprovedUsers();
	SimpleLog discordLog;
	
	private static String helpFileName ="help.txt";
	static String[] availableCommands = {
			"help", "globalhelp", "dev", "ping", "stats", 
			"disconnect", "kill", "flip", "uptime", "currenttime"
			, "reload", "config"
			};
	
	public ChatEvent(JDA jda, SimpleLog discordLog) {
		this.jda = jda;
		this.discordLog = discordLog;
	}
	
	/*
	 * On Discord Message Received
	 * @see net.dv8tion.jda.hooks.ListenerAdapter#onMessageReceived(net.dv8tion.jda.events.message.MessageReceivedEvent)
	 */
	public void onMessageReceived(MessageReceivedEvent e) {
		
		if (e.getMessage().isPrivate()) {
			
			if (e.getAuthor().getUsername().equalsIgnoreCase("" + ConfigFile.getOwnerID())) {
				
				switch (e.getMessage().getContent()) {
				
				case "#stats": //prints out from the stats file
					StatsFile.readKeys(e);
					try {
						
						StatsFile.updateCount("stats");
					} catch (ConfigurationException e1) {
						discordLog.warn(e1.getMessage());
					}
					break;
					
					//TODO Test this config editing
				case "#config": //Edit the config from discord PM's
					String[] configEditCmd = e.getMessage().getContent().substring(7).split(":"); 
					//each part of the editing process needs to be
					//split up by ":"
					if (configEditCmd[0].equalsIgnoreCase("edit")) {
						
						switch (configEditCmd[1]) { //DEBUG "config:edit:toggleirc
						
						case "toggleirc": //Toggle the IRC Relay
							boolean relay = !ConfigFile.shouldIrcRelay();
							ConfigFile.config.setProperty("IRC_Relay", relay);
							e.getTextChannel().sendMessage("IRC Relay is now = " + relay);
							break;
							
						case "+user":
							ArrayList<String> users = new ArrayList<String>(Arrays.asList(ConfigFile.config.getStringArray("Approved_Users")));
							String testUser = configEditCmd[3];
							
							for (int i=0; i< configEditCmd[3].split(",").length; i++) {
								users.add(configEditCmd[3].split(",")[i]);
								discordLog.trace("Trying to update Approved Users");
							}
							//DEBUG Runs the test right after update
							if (isApprovedUser(testUser)) {
								discordLog.trace("Success in Updating Approved Users");
								discordLog.info("Users are now: " + ConfigFile.getApprovedUsers());
								e.getPrivateChannel().sendMessage("Update Success!");
							}
							else {
								discordLog.warn("Failed to find new user in approved string array");
								e.getPrivateChannel().sendMessage("It failed the test - read console output for more");
							}
							break;
						}
					}
					if (configEditCmd[0].equalsIgnoreCase("relayValue")) { //DEBUG "Config:relayValue:[ConfigKey]"
						e.getPrivateChannel().sendMessage((String) ConfigFile.config.getProperty(configEditCmd[1]));
						
					}
					//Some other second string
					break;
				case "#help":
					sendHelp(e.getPrivateChannel());
					break;
				}
			}
			else {
				
			}
			e.getAuthor().getPrivateChannel().sendMessage("Sorry something went wrong!");
		}
		else {
			
		switch (e.getMessage().getContent()) {
		
		case "#ping":
			delMessage(e);
			e.getTextChannel().sendMessage("PONG");
			try {
				
				StatsFile.updateCount("ping");
			} catch (ConfigurationException e2) {
				discordLog.warn(e2.getMessage());
				discordLog.warn(e2.getMessage());
			}
			break;
		
		case "#disconnect":
			if (isApprovedUser(getUsername(e))) {
				discordLog.info(e.getAuthor().getUsername() + " has stopped the bot!");
				delMessage(e);
				try {
					
					StatsFile.updateCount("disconnect");
				} catch (ConfigurationException e1) {
					discordLog.warn(e1.getMessage());
				}
				jda.shutdown();
			}
			else {
				e.getAuthor().getPrivateChannel().sendMessage("You are not authorized to do that!");
				}			
			break;
			
		case "#kill":
			if (isApprovedUser(getUsername(e))) {
				discordLog.info(e.getAuthor().getUsername() + " has killed the bot!");
				delMessage(e);
				try {
					
					StatsFile.updateCount("kill");
				} catch (ConfigurationException e1) {
					discordLog.warn(e1.getMessage());
				}
				IRCMain.irc.close();
				jda.shutdown(true);
				System.exit(0);
			}
			else {
				e.getAuthor().getPrivateChannel().sendMessage("You are not authorized to do that!");
			}			
			break;
			
		case "#flip":
			boolean choice = random.nextBoolean();
			delMessage(e);
			try {
				
				StatsFile.updateCount("flip");
			} catch (ConfigurationException e1) {
				discordLog.warn(e1.getMessage());
			}			
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
			delMessage(e);
			try {
				
				StatsFile.updateCount("help");
			} catch (ConfigurationException e1) {
				discordLog.warn(e1.getMessage());
			}			
			sendHelp(e);			
			break;
			
		case "#globalhelp":
			delMessage(e);
			try {
				
				StatsFile.updateCount("globalhelp");
			} catch (ConfigurationException e1) {
				discordLog.warn(e1.getMessage());
			}			
			sendGlobalHelp(e);			
			break;
			
		case "#uptime":
			delMessage(e);
			try {
				
				StatsFile.updateCount("uptime");
			} catch (ConfigurationException e1) {
				discordLog.warn(e1.getMessage());
			}			
			sendUptime(e);		
			break;
		case "#currenttime":
			if (isApprovedUser(getUsername(e))) {
				delMessage(e);
				try {
					
					StatsFile.updateCount("currenttime");
				} catch (ConfigurationException e1) {
					discordLog.warn(e1.getMessage());
				}				
				e.getTextChannel().sendMessage("" + System.nanoTime());
			}
			else {
				e.getAuthor().getPrivateChannel().sendMessage("You are not authorized to do that!");
				}			
			break;
			
		case "#dev":
			delMessage(e);
			try {
				
				StatsFile.updateCount("dev");
			} catch (ConfigurationException e1) {
				discordLog.warn(e1.getMessage());
			}			
			e.getTextChannel().sendMessage("BreadBot is developed by LoafaBread and all the code can be found at http://birdgeek.github.io/BreadBot/");			
			break;
			
		case "#reload":
			if (isApprovedUser(getUsername(e))) {
				ConfigFile.config.reload();
				try {
					
					StatsFile.updateCount("reload");
				} catch (ConfigurationException e1) {
					discordLog.warn(e1.getMessage());
				}				
				try {
					
					ConfigFile.config.load();
				} catch (ConfigurationException e1) {
					discordLog.warn(e1.getMessage());
				}
			}
			break;
			
		case "#debug":
			if (isApprovedUser(getUsername(e))) {
				
				BotMain.printDiagnostics();
				
				discordLog.trace(e.getAuthor().getUsername() + "  issue the debug command!");
			}
			break;
		
		case "#getChannel":
			if (isApprovedUser(getUsername(e))) {
				e.getAuthor().getPrivateChannel().sendMessage("ID For : '" + e.getTextChannel().getName() + "' is: " + e.getTextChannel().getId());
			}
		}
		}
	}

	//TODO: Comment all these functions below
	private void delMessage(MessageReceivedEvent e) {
		if (ConfigFile.shouldDelete()) {
			e.getMessage().deleteMessage();
		}
	}

	private void sendGlobalHelp(MessageReceivedEvent e) {
		e.getTextChannel().sendMessage(new MessageBuilder()
				.appendString("Welcome to the help command! Below are all the commands you can run!")
				.appendCodeBlock(getHelpCommands(), "java")
				.build());
		
	}

	private void sendHelp(MessageReceivedEvent e) {
		e.getAuthor().getPrivateChannel().sendMessage("Welcome to the help command! Below are all the commands you can run!");
		e.getAuthor().getPrivateChannel().sendMessage(new MessageBuilder().appendCodeBlock(getHelpCommands(), "python").build());
		
	}
	
	private void sendHelp(PrivateChannel e) {
		e.sendMessage(new MessageBuilder().appendCodeBlock(getHelpCommands(), "python").build());
	}
	
	private void sendUptime(MessageReceivedEvent e) {
		long different = System.currentTimeMillis() - BotMain.start;
		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;
		
		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;
		
		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;
		
		long elapsedSeconds = different / secondsInMilli;
		String time = String.format("%d days, %d hours, %d minutes, %d seconds%n", //THANKS FOR FIXING THIS SHIT VAN
		    elapsedDays,
		    elapsedHours, elapsedMinutes, elapsedSeconds);
		
		e.getTextChannel().sendMessage( "I have been online for " + time);	
		}

	private String getHelpCommands() {
		
		StringBuilder sb = new StringBuilder();
		
		try {
			FileReader fr = new FileReader(helpFileName);
			
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			String everything = sb.toString();
			br.close();
			return everything;
		}
		catch (FileNotFoundException ex) {
			discordLog.info("Unable to open file: " + helpFileName);
		}
		catch (IOException ex) {
			discordLog.info("Error reading file");
		}
		return "It must have failed on me :(";
	}

	private String[] getApprovedUsers() {
		return ConfigFile.getApprovedUsers();
		
	}
	private String getUsername(MessageReceivedEvent e) {
		return e.getAuthor().getUsername();
				}
	private boolean isApprovedUser(String username) {
		for (int i=0; i < approvedUsers.length; i++) {
			if (username.equalsIgnoreCase(approvedUsers[i])) {
				return true;
			}
		}
			return false;
	}
}
