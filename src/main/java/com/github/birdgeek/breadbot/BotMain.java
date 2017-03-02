package com.github.birdgeek.breadbot;

import net.dv8tion.jda.utils.SimpleLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.birdgeek.breadbot.discord.DiscordMain;
import com.github.birdgeek.breadbot.irc.IrcMain;
import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.StatsFile;

public class BotMain {
	
	public static long start;
	static ConfigFile config;
	static StatsFile stats;
	public static SimpleLog discordLog;
	public static SimpleLog ircLog;
	public static SimpleLog systemLog;
	public final static String version = "0.0.7";
	
	/*
	 * Main method  for Breadbot
	 */
	public static void main(String[] args)  {
		
		discordLog = SimpleLog.getLog("Discord");
		ircLog = SimpleLog.getLog("IRC");
		systemLog = SimpleLog.getLog("System");
	
		
		config = new ConfigFile(systemLog);
		stats = new StatsFile();
		
		start = System.currentTimeMillis();
		

		discordLog.debug("Logging in using: " + ConfigFile.getBotToken());
		DiscordMain.setup(discordLog);
		
		
		if (ConfigFile.shouldEnableIrc()) { //Should we enable the IRC portion?
			IrcMain.setup(ircLog);
			systemLog.info("Enabled twitch");
		}
	}
}
