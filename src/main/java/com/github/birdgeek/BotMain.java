package com.github.birdgeek;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.security.auth.login.LoginException;

import org.apache.commons.configuration.ConfigurationException;
import org.pircbotx.exception.IrcException;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.utils.SimpleLog;

public class BotMain {
	static long start;
	static ConfigFile config;
	static StatsFile stats;
	static JDA jda;
	static String version;
	static SimpleLog discordLog;
	static SimpleLog ircLog;
	private static boolean shouldContinue;	
	
	/*
	 * Main method  for all bots
	 */
	public static void main(String[] args) throws  FileNotFoundException, IOException, ConfigurationException, LoginException, IllegalArgumentException, InterruptedException {
		config = new ConfigFile();
		stats = new StatsFile();
		start = System.currentTimeMillis();
		version =  ConfigFile.config.getString("Version");
		discordLog = SimpleLog.getLog("Discord Log");
		ircLog = SimpleLog.getLog("IRC Log");
		discordLog.setLevel(SimpleLog.Level.DEBUG);
		discordLog.debug("test");
		
		jda = new JDABuilder(ConfigFile.getEmail(), ConfigFile.getPassword())
				.addListener(new ChatEvent(jda))
				.addListener(new InviteEvent())
				.buildBlocking(); //Builds the discord bot
		
		jda.getAccountManager().setGame("Breadbot V: " +version);
		sendWelcome();
		goLive();

		if (ConfigFile.config.getBoolean("Twitch_Enable")) { //Should we enable the IRC portion?
				try { //Tries to build IRC bot
					IRCMain.setup();
				} catch (IrcException e) {
					e.printStackTrace();
				}
		}
	}

public static void goLive(){
		
		shouldContinue = true;
		
		Scanner scanner = new Scanner(System.in);
		
		while(shouldContinue){
			
			String input = scanner.nextLine();
			char command = input.charAt(0);
			String contents = input.substring(1);
			
			switch(command){
			
			case 'k':
				
				discordLog.debug("commanded to kill");
				sendMessage( "Quiting from Console");
				shouldContinue = false;
				break;
				
			case 'c':
				
				discordLog.debug("commanded to chat");
				sendMessage("[console] " + contents);
				break;
				
			case 'd':
				
				discordLog.debug("commanded to print diagnostics");
				printDiagnostics();
				break;

			}
		}
		
		jda.shutdown();
		System.exit(0);
		scanner.close();
	}

	private static void printDiagnostics() {
		
	sendMessage(new MessageBuilder().appendCodeBlock(""
			+ "Home Channel: " + ConfigFile.getHomeChannel() + "/" + jda.getTextChannelById("" + ConfigFile.getHomeChannel()).getName()
			+ "\nHome Guild:" + ConfigFile.getHomeGuild() + "/" + jda.getGuildById("" + ConfigFile.getHomeGuild()).getName()
			+ "\nOwner: " + ConfigFile.config.getString("Owner_ID")
			, "python")
			.build());
	
	}

	private static void sendMessage(String contents) {
		jda.getTextChannelById("" +ConfigFile.getHomeChannel()).sendMessage(contents);
	
}

	public static void sendMessage(Message message) {
		jda.getTextChannelById("" +ConfigFile.getHomeChannel()).sendMessage(message);
	}
	/*
	 * Sends on connect welcome to home discord channel
	 */
	public static void sendWelcome() {
		
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(
				new MessageBuilder()
				.appendCodeBlock("Welcome to Bread Bot! \n"
						+ "Version: " + ConfigFile.config.getString("Version")
						, "java")
				.build());
		
		if (ConfigFile.config.getBoolean("Send_Welcome_Mention")) { //Should we mention the Owner
			jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(new MessageBuilder()
					.appendString("I am being run by ")
					.appendMention(jda.getUsersByName(ConfigFile.config.getString("Owner_ID")).get(0))
					.build());
		}
		
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage("You can read more about me here - http://birdgeek.github.io/BreadBot/");
	}
	
}
