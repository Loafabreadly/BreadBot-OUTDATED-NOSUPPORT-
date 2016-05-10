package com.github.birdgeek.breadbot.discord;

import com.github.birdgeek.breadbot.utility.Logger;
import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

/**
 * Created by birdg on 5/10/2016.
 */
public class PrivateMessageListener extends ListenerAdapter {

    public void onPrivateMessageReceivedEvent(PrivateMessageReceivedEvent e) {
        Logger.countPrivateMessage();
        CommandInterpreter.processMessage(e);
    }

}
