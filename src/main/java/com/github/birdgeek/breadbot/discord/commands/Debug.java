package com.github.birdgeek.breadbot.discord.commands;

import com.github.birdgeek.breadbot.utility.ConfigFile;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;

/**
 * Created by birdg on 5/17/2016.
 */
public class Debug {
    public static void process(GuildMessageReceivedEvent e) {


        if (Arrays.asList(ConfigFile.getApprovedUsers()).contains(e.getAuthor().getId())) {
            e.getChannel().sendMessage("This does nothing yet");
        }
    }
}
