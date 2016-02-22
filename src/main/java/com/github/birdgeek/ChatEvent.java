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
	
	public void onMessageReceived(MessageReceivedEvent e) {
		if (e.getMessage().isPrivate()) {
			if (e.getAuthor().getUsername().equalsIgnoreCase(SettingsFile.config.getString("Owner"))) {
				switch (e.getMessage().getContent()) {
				case "#stats":
					StatsFile.readKeys(e);
				}
			}
			e.getAuthor().getPrivateChannel().sendMessage("Sorry you don't have access!");
		}
		else {
		switch (e.getMessage().getContent()) {
		case "#ping":
			delMessage(e);
			e.getTextChannel().sendMessage("PONG");
			try {
				StatsFile.updateCount("ping");
			} catch (ConfigurationException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			break;
		case "#disconnect":
			if (isApprovedUser(getUsername(e))) {
				System.out.println(e.getAuthor().getUsername() + " has stopped the bot!");
				delMessage(e);
				try {
					StatsFile.updateCount("disconnect");
				} catch (ConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				api.shutdown();
			}
			else {
				e.getAuthor().getPrivateChannel().sendMessage("You are not authorized to do that!");
				}			
			break;
		case "#kill":
			if (isApprovedUser(getUsername(e))) {
				System.out.println(e.getAuthor().getUsername() + " has killed the bot!");
				delMessage(e);
				try {
					StatsFile.updateCount("kill");
				} catch (ConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
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
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			sendHelp(e);			
			break;
		case "#globalhelp":
			delMessage(e);
			try {
				StatsFile.updateCount("globalhelp");
			} catch (ConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			sendGlobalHelp(e);			
			break;
		case "#uptime":
			delMessage(e);
			try {
				StatsFile.updateCount("uptime");
			} catch (ConfigurationException e1) {
				// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
			e.getTextChannel().sendMessage("BreadBot is developed by LoafaBread and all the code can be found at http://birdgeek.github.io/BreadBot/");			
			break;
		case "#reload":
			if (isApprovedUser(getUsername(e))) {
				SettingsFile.config.reload();
				try {
					StatsFile.updateCount("reload");
				} catch (ConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}				
				try {
					SettingsFile.config.load();
				} catch (ConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		}
	}

	private void delMessage(MessageReceivedEvent e) {
		if (SettingsFile.config.getBoolean("delcmd")) {
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
			System.out.println("Unable to open file: " + helpFileName);
		}
		catch (IOException ex) {
			System.out.println("Error reading file");
		}
		return "It must have failed on me :(";
	}

	private String[] getApprovedUsers() {
		return SettingsFile.getApprovedUsers();
		
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
