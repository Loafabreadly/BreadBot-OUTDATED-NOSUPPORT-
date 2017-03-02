package com.github.birdgeek.breadbot.discord.commands;

import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;

/**
 * Created by birdg on 5/10/2016.
 */
public class Stats {

    public static void process(GuildMessageReceivedEvent e) {

        if (Arrays.asList(ConfigFile.getApprovedUsers()).contains(e.getAuthor().getId())) {
            e.getChannel().sendMessage(DiscordUtility.printStats());
        }
    }


}
