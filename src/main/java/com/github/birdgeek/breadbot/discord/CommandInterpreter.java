package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.discord.commands.*;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import com.github.birdgeek.breadbot.utility.GoogleSearch;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by birdg on 5/10/2016.
 */
public class CommandInterpreter {

    /**
     * Interprets the Guild Message
     * @param e
     */
    static void processMessage(GuildMessageReceivedEvent e) {

        if (e.getMessage().getContent().charAt(0) != DiscordMain.callsign
                || Arrays.asList(DiscordUtility.ignoredUsersIds()).contains(e.getAuthor().getId()))
            return;

        String input = e.getMessage().getContent().substring(1).toLowerCase();
        processCommand(input, e);
    }

    static void processCommand(String input, GuildMessageReceivedEvent e) {

        switch (input) {

            case "ping":
                Ping.process(e);
                break;
            case "help":
                Help.process(e);
                break;
            case "dev":
                Dev.process(e);
                break;
            case "stats":
                Stats.process(e);
                break;
            case "kill":
                Kill.process(e);
                break;
            case "flip":
                Flip.process(e);
                break;
            case "uptime":
                Uptime.process(e);
                break;
            case "reload":
                Reload.process(e);
                break;
            case "config":
                Config.process(e);
                break;
            case "attach":
                Attach.process(e);
                break;
            case "debug":
                Debug.process(e);
                break;
        }
        if (input.length() > 2 && input.substring(0, 2).contentEquals("g ")) {
            String searchquery = input.substring(2);
            try {
                GoogleSearch.search(searchquery, e, false);
            } catch (IOException e1) {
                DiscordMain.discordLog.fatal(e1.getLocalizedMessage());
            }
        }

        if (input.length() > 3 && input.substring(0, 3).contentEquals("gs ")) {
            String searchquery = input.substring(3);
            try {
                GoogleSearch.search(searchquery, e, true);
            } catch (IOException e1) {
               DiscordMain.discordLog.fatal(e1.getMessage());
            }
        }
    }
    /**
     * Inteprets the PM
     * @param e
     */
    static void processMessage(PrivateMessageReceivedEvent e) {

    }

    static void processPMCommand(PrivateMessageReceivedEvent e) {

    }
}
