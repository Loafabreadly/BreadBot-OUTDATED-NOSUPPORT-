package com.github.birdgeek.breadbot.discord;

import javax.security.auth.login.LoginException;

import com.github.birdgeek.breadbot.BotMain;
import org.slf4j.Logger;

import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;

public class DiscordMain {
	
	public static JDA jda;
	public static Logger discordLog;
	static String botID;
	static char callsign;
	public static String homeChannel;
	public static String homeGuild;
	
	public static void setup(Logger log) {
		discordLog = log;
		homeChannel = ConfigFile.getHomeChannel();
		homeGuild = ConfigFile.getHomeGuild();

		try {
			jda = new JDABuilder()
				.setBotToken(ConfigFile.getBotToken())
				.addListener(new GuildMessageListener()) //Pass API and Specific Logger
				.addListener(new DiscordToTwitchEvent())
				.addListener(new PrivateMessageListener())
				//.addListener(new PmEvent(discordLog)) //Passes Logger
				.buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException e) {
		discordLog.error(e.getMessage());
		} //Builds the discord bot - Blocks everything until API is ready
	
		jda.getAccountManager().setGame("Breadbot V: " + BotMain.version);
		new DiscordUtility(DiscordMain.jda, discordLog); //Setup for Util class - passes JDA and Logger	
		botID = jda.getSelfInfo().getId();
		callsign = ConfigFile.getCallsign();
		sendWelcome();
	}
	
	/*
	 * Sends on connect welcome to home discord channel
	 */
	public static void sendWelcome() {
		
		jda.getTextChannelById(DiscordMain.homeChannel).sendMessage( //DEBUG this sends NPE OFTEN
				new MessageBuilder()
						.appendString("*Hellow World! :bread: :robot:")
						.appendCodeBlock("[Welcome to Bread Bot!] \n\n"
							+ "[Version][" + BotMain.version +"]\n"
						, "MD")
						.appendString("You can read more about me here - http://birdgeek.github.io/BreadBot/")
				.build());
	}
	
}
