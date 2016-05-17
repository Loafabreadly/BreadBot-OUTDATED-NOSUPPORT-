package com.github.birdgeek.breadbot.discord.commands;

import com.github.birdgeek.breadbot.BotMain;
import com.github.birdgeek.breadbot.discord.DiscordMain;
import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.utils.ApplicationUtil;

/**
 * Created by birdg on 5/10/2016.
 */
public class Dev {

    public static void process(GuildMessageReceivedEvent e) {

        e.getChannel().sendMessage(new MessageBuilder()
                .appendCodeBlock("[BreadBot][" + BotMain.version + "]\n\n"
                        + "[Developed By][@LoafaBread#8395]\n"
                        + "[Github][http://birdgeek.github.io/BreadBot/]","MD").build());
    }


}
