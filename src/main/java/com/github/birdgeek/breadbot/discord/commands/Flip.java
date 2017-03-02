package com.github.birdgeek.breadbot.discord.commands;

import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

/**
 * Created by birdg on 5/10/2016.
 */
public class Flip {

    public static void process(GuildMessageReceivedEvent e) {

        if (e.getMessage().getRawContent().length() < 6) {
            Random ran = new Random();
           int choice =  ran.nextInt(2);
            if (choice == 0)
                e.getChannel().sendMessage("**You flipped heads!**");
            else
                e.getChannel().sendMessage("**You flipped tails!**");
        }
        else {
            String[] choices = e.getMessage().getRawContent().substring(6).split(" ");
            Random ran = new Random();
            e.getChannel().sendMessage("**" + choices[ran.nextInt(choices.length)].toUpperCase() + " was rolled!**");
        }
    }


}
