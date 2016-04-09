package com.github.birdgeek.breadbot.discord;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;

import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import com.github.birdgeek.breadbot.utility.StatsFile;

import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class PmEvent extends ListenerAdapter {
	
	Logger discordLog;

	public PmEvent(Logger log) {
		this.discordLog = log;
	}
	
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) { //FIXME Must go about this the same way Google command is done
			if (e.getAuthor().getId().equalsIgnoreCase(ConfigFile.getOwnerID())) { //Only owner can change the bot from PMS

				discordLog.trace("Got PM from Owner");
				switch (e.getMessage().getContent()) {
				
				case "#stats": //prints out from the stats file
					StatsFile.readKeys(e);
					StatsFile.updateCount("stats");
					break;
					
					//FIXME It doesnt work like this
				case "#config": //Edit the config from discord PM's
					String[] configEditCmd = e.getMessage().getContent().substring(7).split(":"); 
					//each part of the editing process needs to be
					//split up by ":"
					if (configEditCmd[0].equalsIgnoreCase("edit")) {

						switch (configEditCmd[1]) { //DEBUG "config:edit:toggleirc
						
						case "toggleirc": //Toggle the IRC Relay
							boolean relay = !ConfigFile.shouldIrcRelay();
							ConfigFile.config.setProperty("IRC_Relay", relay);
							try {
								ConfigFile.config.save();
							} catch (ConfigurationException e1) {
								discordLog.error(e1.getMessage());
							}
							e.getChannel().sendMessage("IRC Relay is now = " + relay);
							break;
							
						case "+user":
							ArrayList<String> users = new ArrayList<String>(Arrays.asList(ConfigFile.config.getStringArray("Approved_Users")));
							String testUser = configEditCmd[3];
							
							for (int i=0; i< configEditCmd[3].split(",").length; i++) {
								users.add(configEditCmd[3].split(",")[i]);
								discordLog.trace("Trying to update Approved Users");
							}
							ConfigFile.config.setProperty("Approved_Users", users);
							ConfigFile.save();
							//DEBUG Runs the test right after update
							if (DiscordUtility.isApprovedUser(testUser)) {
								discordLog.trace("Success in Updating Approved Users");
								discordLog.info("Users are now: " + ConfigFile.getApprovedUsers());
								e.getChannel().sendMessage("Update Success!");
							}
							else {
								discordLog.warn("Failed to find new user in approved string array");
								e.getChannel().sendMessage("It failed the test - read console output for more");
							}
							break;
						}
					}

					if (configEditCmd[0].equalsIgnoreCase("relayValue")) { //DEBUG "Config:relayValue:[ConfigKey]"
						discordLog.debug("Told to relay value of: " + configEditCmd[1]);
						e.getChannel().sendMessage(ConfigFile.config.getProperty(configEditCmd[1]).toString());
					}
					//Some other second string
					break;

				case "#help":
					DiscordUtility.sendHelp(e.getChannel());
					break;
				}
			}
			else if (!e.getAuthor().getId().equalsIgnoreCase(DiscordMain.botID)) {
				e.getChannel().sendMessage("Sorry something went wrong!");
			}

		}
}
