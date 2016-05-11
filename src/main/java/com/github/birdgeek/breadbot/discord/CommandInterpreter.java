package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.discord.commands.*;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

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

        String input = e.getMessage().getContent().substring(1);
        String[] availCommands = DiscordUtility.commands();

        for (int i = 0; i < DiscordUtility.commands().length; i++)
            if (availCommands[i].equalsIgnoreCase(input)) {
                processCommand(input.toLowerCase(), e);
                DiscordMain.discordLog.trace("Found a command!");
            }
    }

    /**
     * Inteprets the PM
     * @param e
     */
    static void processMessage(PrivateMessageReceivedEvent e) {

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
        }
    }
}
