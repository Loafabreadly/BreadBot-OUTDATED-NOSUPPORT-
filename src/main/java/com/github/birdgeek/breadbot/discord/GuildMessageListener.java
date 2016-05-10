package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.utility.Logger;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Created by birdg on 5/10/2016.
 */
public class GuildMessageListener extends ListenerAdapter {

    public void onGuildMessageReceived(GuildMessageReceivedEvent e) {
        Logger.countGuildMessage();
        CommandInterpreter.processMessage(e);
    }
}
