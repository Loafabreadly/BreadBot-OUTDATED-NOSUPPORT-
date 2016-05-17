package com.github.birdgeek.breadbot.discord;

import net.dv8tion.jda.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;


/**
 * Created by birdg on 5/11/2016.
 */
public class MemberJoinEvent extends ListenerAdapter {


    public MemberJoinEvent() {

    }

    /**
     *
     * @param e
     */
    public void onGuildMemberJoinEvent(GuildMemberJoinEvent e) {

        if (e.getUser().isBot()) {
            e.getGuild().getManager().kick(e.getUser());
            e.getJDA().getTextChannelById(DiscordMain.homeChannel).sendMessage(e.getUser().getUsername() + "was kicked because they were a bot!!");

        }
        e.getGuild().getManager().addRoleToUser(e.getUser(),e.getGuild().getRoleById("173911513617268737"));
    }

}


