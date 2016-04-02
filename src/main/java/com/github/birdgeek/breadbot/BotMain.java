package com.github.birdgeek.breadbot;

import java.util.Scanner;

import org.apache.commons.configuration.ConfigurationException;

import com.github.birdgeek.breadbot.discord.DiscordMain;
import com.github.birdgeek.breadbot.irc.IrcMain;

import net.dv8tion.jda.utils.SimpleLog;
import net.dv8tion.jda.utils.SimpleLog.Level;

public class BotMain {
	static long start;
	static ConfigFile config;
	static StatsFile stats;
	static String version;
	static SimpleLog discordLog;
	static SimpleLog ircLog;
	static SimpleLog systemLog;
	private static boolean shouldContinue;	
	
	/*
	 * Main method  for all bots
	 */
	public static void main(String[] args)  {
		
		discordLog = SimpleLog.getLog("Discord");
		ircLog = SimpleLog.getLog("IRC");
		systemLog = SimpleLog.getLog("System");
		
		try {
			config = new ConfigFile();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
		systemLog.fatal(e.getMessage());
		}
		try {
			stats = new StatsFile();
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			systemLog.fatal(e.getMessage());
		}
		start = System.currentTimeMillis();
		version =  ConfigFile.getVersion();
		
		discordLog.setLevel(Level.DEBUG);
		ircLog.setLevel(Level.DEBUG);
		systemLog.setLevel(Level.DEBUG);//TODO For releases; set this to a different lvl
		try {
			discordLog.debug("Logging in using: " + ConfigFile.getEmail());
		} catch (ConfigurationException e) {
			
			systemLog.fatal(e.getMessage());
		}
		
		
		
		if (ConfigFile.shouldEnableTwitch()) { //Should we enable the IRC portion?
				IrcMain.setup(ircLog);
		}
		
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
				
				discordLog.debug("commanded to kill");
				DiscordMain.sendMessage("Quiting from Console");
				shouldContinue = false;
				break;
				
			case 'c':
				
				discordLog.debug("commanded to chat");
				DiscordMain.sendMessage("[console] " + contents);
				break;
				
			case 'd':
				
				discordLog.debug("commanded to print diagnostics");
				DiscordMain.printDiagnostics();
				break;

			}
		}
		
		DiscordMain.jda.shutdown();
		IrcMain.kill();
		System.exit(0);
		scanner.close();
	}

		
		
}
