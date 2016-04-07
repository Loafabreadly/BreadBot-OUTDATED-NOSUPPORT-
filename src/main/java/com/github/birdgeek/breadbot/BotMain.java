package com.github.birdgeek.breadbot;

import java.util.Scanner;

import org.apache.commons.configuration.ConfigurationException;

import com.github.birdgeek.breadbot.discord.DiscordMain;
import com.github.birdgeek.breadbot.irc.IrcMain;
import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import com.github.birdgeek.breadbot.utility.IrcUtility;
import com.github.birdgeek.breadbot.utility.StatsFile;

import net.dv8tion.jda.utils.SimpleLog;
import net.dv8tion.jda.utils.SimpleLog.Level;

public class BotMain {
	
	public static long start;
	static ConfigFile config;
	static StatsFile stats;
	static String version;
	static SimpleLog discordLog;
	static SimpleLog ircLog;
	static SimpleLog systemLog;
	private static boolean shouldContinue;	
	
	/*
	 * Main method  for Breadbot
	 */
	public static void main(String[] args)  {
		
		discordLog = SimpleLog.getLog("Discord");
		ircLog = SimpleLog.getLog("IRC");
		systemLog = SimpleLog.getLog("System");
		
		config = new ConfigFile();
		stats = new StatsFile();
		
		start = System.currentTimeMillis();
		version =  ConfigFile.getVersion();
		
		discordLog.setLevel(Level.DEBUG);
		ircLog.setLevel(Level.DEBUG);
		systemLog.setLevel(Level.DEBUG);//TODO For releases; set this to a different lvl
		
		try {
			discordLog.debug("Logging in using: " + ConfigFile.getEmail());
			DiscordMain.setup(discordLog);
		} catch (ConfigurationException e) {
			
			systemLog.fatal(e.getMessage());
		}
		
		
		
		if (ConfigFile.shouldEnableTwitch()) { //Should we enable the IRC portion?
				IrcMain.setup(ircLog);
		}
		
		new DiscordUtility(DiscordMain.jda, discordLog); //Setup for Util class - passes JDA and Logger
		goLive();
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
				
				discordLog.debug("Commanded to kill");
				DiscordUtility.sendMessage("Quiting from Console");
				shouldContinue = false;
				break;
				
			case 'c':
				
				discordLog.debug("Commanded to chat");
				DiscordUtility.sendMessage("[console] " + contents);
				break;
				
			case 'd':
				
				discordLog.debug("Commanded to print diagnostics");
				DiscordUtility.printDiagnostics();
				break;
				
			case 't':
				ircLog.debug("Commanded to chat");
				IrcUtility.sendMessage(contents);
				break;
			}
		}
		
		//DiscordMain.jda.shutdown();
		//IrcMain.kill();
		scanner.close();
		System.exit(0);
	}	
}
