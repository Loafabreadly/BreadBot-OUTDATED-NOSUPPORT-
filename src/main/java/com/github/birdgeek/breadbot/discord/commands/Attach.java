package com.github.birdgeek.breadbot.discord.commands;

import com.github.birdgeek.breadbot.discord.DiscordMain;
import com.github.birdgeek.breadbot.utility.ConfigFile;
import com.github.birdgeek.breadbot.utility.DiscordUtility;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import org.apache.commons.configuration.ConfigurationException;

import java.util.Arrays;

/**
 * Created by birdg on 5/10/2016.
 */
public class Attach {

    public static void process(GuildMessageReceivedEvent e) {

        if (!Arrays.asList(DiscordUtility.getApprovedUsers()).contains(e.getAuthor().getId())) {
            DiscordMain.discordLog.trace("Someone unauthorized tried to change home channel");
            return;
        }

        DiscordMain.jda.getTextChannelById(DiscordMain.homeChannel).sendMessageAsync("`Switching Home Channels!`", msg -> {
            ConfigFile.config.setProperty("Home_Channel_ID", e.getChannel().getId());
            try {
                ConfigFile.config.save();
            } catch (ConfigurationException e1) {
                DiscordMain.discordLog.fatal(e1.getLocalizedMessage());
                e.getChannel().sendMessage("**Failed!**");
            }
            msg.updateMessage(":thumbsup::skin-tone-2:");
        } );
        e.getChannel().sendMessage("`This is now the home channel`");

    }


}
