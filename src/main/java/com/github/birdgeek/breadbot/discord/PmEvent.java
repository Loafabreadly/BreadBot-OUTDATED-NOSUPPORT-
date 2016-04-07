package com.github.birdgeek.breadbot.discord;

import java.util.ArrayList;
import java.util.Arrays;

import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import com.github.birdgeek.breadbot.utility.StatsFile;

import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.utils.SimpleLog;

public class PmEvent {
	
	SimpleLog discordLog;

	public PmEvent(SimpleLog log) {
		this.discordLog = log;
	}
	
	public void onMessageReceivedEvent(PrivateMessageReceivedEvent e) {

			if (e.getAuthor().getUsername().equalsIgnoreCase("" + ConfigFile.getOwnerID())) { //Only owner can change the bot from PMS

				
				switch (e.getMessage().getContent()) {
				
				case "#stats": //prints out from the stats file
					StatsFile.readKeys(e);
					StatsFile.updateCount("stats");
					break;
					
					//TODO Test this config editing
				case "#config": //Edit the config from discord PM's
					String[] configEditCmd = e.getMessage().getContent().substring(7).split(":"); 
					//each part of the editing process needs to be
					//split up by ":"
					if (configEditCmd[0].equalsIgnoreCase("edit")) {

						switch (configEditCmd[1]) { //DEBUG "config:edit:toggleirc
						
						case "toggleirc": //Toggle the IRC Relay
							boolean relay = !ConfigFile.shouldIrcRelay();
							ConfigFile.config.setProperty("IRC_Relay", relay);
							e.getChannel().sendMessage("IRC Relay is now = " + relay);
							break;
							
						case "+user":
							ArrayList<String> users = new ArrayList<String>(Arrays.asList(ConfigFile.config.getStringArray("Approved_Users")));
							String testUser = configEditCmd[3];
							
							for (int i=0; i< configEditCmd[3].split(",").length; i++) {
								users.add(configEditCmd[3].split(",")[i]);
								discordLog.trace("Trying to update Approved Users");
							}
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
						e.getChannel().sendMessage((String) ConfigFile.config.getProperty(configEditCmd[1]));
					}
					//Some other second string
					break;

				case "#help":
					DiscordUtility.sendHelp(e.getChannel());
					break;
				}
			}
			else {
				
			}
			e.getChannel().sendMessage("Sorry something went wrong!");
		}
}
