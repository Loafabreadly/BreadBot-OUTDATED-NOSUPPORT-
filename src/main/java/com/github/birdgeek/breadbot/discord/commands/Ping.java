package com.github.birdgeek.breadbot.discord.commands;


import java.time.temporal.ChronoUnit;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import static java.time.OffsetDateTime.*;

/**
 * Created by birdg on 5/10/2016.
 */
public class Ping {

    public static void process(GuildMessageReceivedEvent e) {

        long until = e.getMessage().getTime().until(now(), ChronoUnit.MILLIS);
        e.getChannel().sendMessageAsync("`Ping: "+until+"ms`", msg -> {
            long ping = e.getMessage().getTime().until(msg.getTime(), ChronoUnit.MILLIS);
            msg.updateMessage("`Ping: " + ping + "ms`");
        });
    }



}
