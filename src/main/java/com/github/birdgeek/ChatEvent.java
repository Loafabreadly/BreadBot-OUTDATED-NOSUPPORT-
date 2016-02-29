package com.github.birdgeek;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import org.apache.commons.configuration.ConfigurationException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class ChatEvent extends ListenerAdapter {

	JDA api;
	Random random = new Random();
	long start = BotMain.start;
	String[] approvedUsers = getApprovedUsers();
	static String helpFileName ="help.txt";
	static String[] availableCommands = {
			"help", "globalhelp", "dev", "ping", "stats", 
			"disconnect", "kill", "flip", "uptime", "currenttime"
			, "reload"
			};
	
	public ChatEvent(JDA jda) {
		this.api = jda;
	}
	
	/*
	 * On Discord Message Received
	 * @see net.dv8tion.jda.hooks.ListenerAdapter#onMessageReceived(net.dv8tion.jda.events.message.MessageReceivedEvent)
	 */
	public void onMessageReceived(MessageReceivedEvent e) {
		
		if (e.getMessage().isPrivate()) {
			
			if (e.getAuthor().getUsername().equalsIgnoreCase(ConfigFile.config.getString("Owner"))) {
				
				switch (e.getMessage().getContent()) {
				
				case "#stats": //prints out from the stats file
					StatsFile.readKeys(e);
					try {
						
						StatsFile.updateCount("stats");
					} catch (ConfigurationException e1) {
						e1.printStackTrace();
					}
					break;
					
				case "#config": //Edit the config from discord PM's
					String[] configEditCmd = e.getMessage().getContent().substring(7).split(":");
					if (configEditCmd[0].equalsIgnoreCase("edit")) {
						
						switch (configEditCmd[1]) {
						
						case "toggleirc": //Toggle the IRC Relay
							boolean relay = !ConfigFile.config.getBoolean("IRC_Relay");
							ConfigFile.config.setProperty("IRC_Relay", relay);
							e.getTextChannel().sendMessage("IRC Relay is now = " + relay);
							break;
						}
					}
					//Some other second string
					break;
				}
			}
			e.getAuthor().getPrivateChannel().sendMessage("Sorry you don't have access!");
		}
		else {
			
		//TODO catch the DiscordConsoleOut stream properly
		switch (e.getMessage().getContent()) {
		
		case "#ping":
			delMessage(e);
			e.getTextChannel().sendMessage("PONG");
			try {
				
				StatsFile.updateCount("ping");
			} catch (ConfigurationException e2) {
				e2.printStackTrace();
			}
			break;
			
		case "#kill":
			if (isApprovedUser(getUsername(e))) {
				DiscordConsoleStream.println(e.getAuthor().getUsername() + " has killed the bot!");
				delMessage(e);
				try {
					
					StatsFile.updateCount("kill");
				} catch (ConfigurationException e1) {
					e1.printStackTrace();
				}
				IRCMain.irc.close();
				api.shutdown(true);
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
				e1.printStackTrace();
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
				e1.printStackTrace();
			}			
			sendHelp(e);			
			break;
		case "#globalhelp":
			delMessage(e);
			try {
				
				StatsFile.updateCount("globalhelp");
			} catch (ConfigurationException e1) {
				e1.printStackTrace();
			}			
			sendGlobalHelp(e);			
			break;
		case "#uptime":
			delMessage(e);
			try {
				
				StatsFile.updateCount("uptime");
			} catch (ConfigurationException e1) {
				e1.printStackTrace();
			}			
			sendUptime(e);		
			break;
		case "#currenttime":
			if (isApprovedUser(getUsername(e))) {
				delMessage(e);
				try {
					
					StatsFile.updateCount("currenttime");
				} catch (ConfigurationException e1) {
					e1.printStackTrace();
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
				e1.printStackTrace();
			}			
			e.getTextChannel().sendMessage("BreadBot is developed by LoafaBread and all the code can be found at http://birdgeek.github.io/BreadBot/");			
			break;
		case "#reload":
			if (isApprovedUser(getUsername(e))) {
				ConfigFile.config.reload();
				try {
					
					StatsFile.updateCount("reload");
				} catch (ConfigurationException e1) {
					e1.printStackTrace();
				}				
				try {
					
					ConfigFile.config.load();
				} catch (ConfigurationException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case "#debug":
			if (isApprovedUser(getUsername(e))) {
				e.getChannel().sendMessage("Server ID: " + api.getGuildById("" + ConfigFile.getHomeChannel())).getId();
				e.getChannel().sendMessage("Server Name: " + api.getGuildById("" + ConfigFile.getHomeServer()).getName());
				e.getChannel().sendMessage("Channel ID: " + api.getTextChannelById("" + ConfigFile.getHomeChannel()).getId());
				e.getChannel().sendMessage("Channel Name: " + api.getTextChannelById("" + ConfigFile.getHomeChannel()).getName());
				e.getChannel().sendMessage("Debug Mode: " + api.isDebug());
				//BotMain.setupConsoleOut();
				//DiscordConsoleStream.println("Test Out");
			}
		}
		}
	}

	private void delMessage(MessageReceivedEvent e) {
		if (ConfigFile.config.getBoolean("delcmd")) {
			e.getMessage().deleteMessage();
		}
	}

	private void sendGlobalHelp(MessageReceivedEvent e) {
		e.getTextChannel().sendMessage("Welcome to the help command! Below are all the commands you can run!");
		e.getTextChannel().sendMessage(getHelpCommands());
		
	}

	private void sendHelp(MessageReceivedEvent e) {
		e.getAuthor().getPrivateChannel().sendMessage("Welcome to the help command! Below are all the commands you can run!");
		e.getAuthor().getPrivateChannel().sendMessage(getHelpCommands());
		
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
			DiscordConsoleStream.println("Unable to open file: " + helpFileName);
		}
		catch (IOException ex) {
			DiscordConsoleStream.println("Error reading file");
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
