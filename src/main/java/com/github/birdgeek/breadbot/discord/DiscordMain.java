package com.github.birdgeek.breadbot.discord;

import javax.security.auth.login.LoginException;

import org.apache.commons.configuration.ConfigurationException;

import com.github.birdgeek.breadbot.utility.ConfigFile;

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.utils.SimpleLog;

public class DiscordMain {
	
	public static JDA jda;
	static SimpleLog discordLog;
	
	public static void setup(SimpleLog log) {
		discordLog = log;
	
		try {
			jda = new JDABuilder(ConfigFile.getEmail(), ConfigFile.getPassword())
				.addListener(new ChatEvent(jda, discordLog)) //Pass API and Specific Logger
				.addListener(new InviteEvent()) //TODO Test invite Util
				.addListener(new DiscordToTwitchEvent())
				.addListener(new PmEvent(discordLog)) //Passes Logger
				.buildBlocking();
		} catch (LoginException | IllegalArgumentException | ConfigurationException | InterruptedException e) {
		discordLog.fatal(e.getMessage());
		} //Builds the discord bot - Blocks everything until API is ready
	
		jda.getAccountManager().setGame("Breadbot V: " + ConfigFile.getVersion());
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
