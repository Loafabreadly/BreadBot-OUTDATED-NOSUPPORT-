package com.github.birdgeek.breadbot.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.github.birdgeek.breadbot.BotMain;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.PrivateChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.SimpleLog;

public class DiscordUtility {
	static JDA jda;
	static SimpleLog discordLog;
	private static String helpFileName ="help.txt";
	static String[] approvedUsers = getApprovedUsers();
	
	public DiscordUtility(JDA api, SimpleLog log) {
		DiscordUtility.jda = api;
		DiscordUtility.discordLog = log;
	}
	
	public static String getUsernameID(MessageReceivedEvent e) {
		return e.getAuthor().getId();
	}

	public static boolean isOwner(String username) {
		if (ConfigFile.getOwnerID().toString().equalsIgnoreCase(username))
			return true;
		else
			return false;
	}

	//TODO: Comment all these functions below
	public static void delMessage(MessageReceivedEvent e) {

		if (ConfigFile.shouldDelete()) {
			
			e.getMessage().deleteMessage();
		}
	}

	public static void sendGlobalHelp(MessageReceivedEvent e) {
		e.getTextChannel().sendMessage(new MessageBuilder()
				.appendString("Welcome to the help command! Below are all the commands you can run!")
				.appendCodeBlock(getHelpCommands(), "python")
				.build());
		
	}

	public static void sendHelp(MessageReceivedEvent e) {
		if (!e.getAuthor().getUsername().equalsIgnoreCase(jda.getSelfInfo().getUsername())) {
			e.getAuthor().getPrivateChannel().sendMessage("Welcome to the help command! Below are all the commands you can run!");
			e.getAuthor().getPrivateChannel().sendMessage(new MessageBuilder().appendCodeBlock(getHelpCommands(), "python").build());
		}
		else {
			e.getTextChannel().sendMessage("Cannot send help to yourself in PM; try #global help");
		}
	}
	
	public static void sendHelp(PrivateChannel e) {
		e.sendMessage(new MessageBuilder().appendCodeBlock(getHelpCommands(), "python").build());
	}
	

	public static void sendUptime(MessageReceivedEvent e) {
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

	private static String getHelpCommands() {
		
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

	public static String[] getApprovedUsers() {
		return ConfigFile.getApprovedUsers();
		
	}
	public static String getUsername(MessageReceivedEvent e) {
		return e.getAuthor().getUsername();
	}
	
	public static boolean isApprovedUser(String username) {
		for (int i=0; i < approvedUsers.length; i++) {
			if (username.equalsIgnoreCase(approvedUsers[i])) {
				return true;
			}
		}
			return false;
	}
	
	public static void printDiagnostics() {
		
		sendMessage(new MessageBuilder().appendCodeBlock(""
				+ "Home Channel: " + ConfigFile.getHomeChannel() + "/" + jda.getTextChannelById(ConfigFile.getHomeChannel()).getName()
				+ "\nHome Guild:" + ConfigFile.getHomeGuild() + "/" + jda.getGuildById(ConfigFile.getHomeGuild()).getName()
				+ "\nOwner: " + ConfigFile.getOwnerID()
				, "python")
				.build());
		
		}
	public static void sendMessage(String contents) {
		jda.getTextChannelById("" +ConfigFile.getHomeChannel()).sendMessage(contents);
	
	}

	public static void sendMessage(Message message) {
		jda.getTextChannelById("" +ConfigFile.getHomeChannel()).sendMessage(message);
	}

}
