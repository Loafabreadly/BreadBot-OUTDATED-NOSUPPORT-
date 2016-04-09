package com.github.birdgeek.breadbot.discord;

import javax.security.auth.login.LoginException;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;

import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;

public class DiscordMain {
	
	public static JDA jda;
	static Logger discordLog;
	static String botID;
	
	public static void setup(Logger log) {
		discordLog = log;
	
		try {
			jda = new JDABuilder(ConfigFile.getEmail(), ConfigFile.getPassword())
				.addListener(new ChatEvent(jda, discordLog)) //Pass API and Specific Logger
				.addListener(new InviteEvent()) 
				.addListener(new DiscordToTwitchEvent())
				.addListener(new PmEvent(discordLog)) //Passes Logger
				.buildBlocking();
		} catch (LoginException | IllegalArgumentException | ConfigurationException | InterruptedException e) {
		discordLog.error(e.getMessage());
		} //Builds the discord bot - Blocks everything until API is ready
	
		jda.getAccountManager().setGame("Breadbot V: " + ConfigFile.getVersion());
		new DiscordUtility(DiscordMain.jda, discordLog); //Setup for Util class - passes JDA and Logger	
		botID = jda.getSelfInfo().getId();
		sendWelcome();
	}
	
	/*
	 * Sends on connect welcome to home discord channel
	 */
	public static void sendWelcome() {
		
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(
				new MessageBuilder()
				.appendCodeBlock("Welcome to Bread Bot! \n"
						+ "Version: " + ConfigFile.getVersion()
						, "python")
				.build());
		
		if (ConfigFile.shouldSendWelcomeMention()) { //Should we mention the Owner
			jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage(new MessageBuilder()
					.appendString("I am being run by ")
					.appendMention(jda.getUserById("" + ConfigFile.getOwnerID()))
					.build());
		}
		
		jda.getTextChannelById("" + ConfigFile.getHomeChannel()).sendMessage("You can read more about me here - http://birdgeek.github.io/BreadBot/");
	}
	
}
