package com.github.birdgeek.breadbot.discord.commands;

import com.github.birdgeek.breadbot.utility.DiscordUtility;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

/**
 * Created by birdg on 5/10/2016.
 */
public class Uptime {

    public static void process(GuildMessageReceivedEvent e) {

        DiscordUtility.sendUptime(e);
    }


}
