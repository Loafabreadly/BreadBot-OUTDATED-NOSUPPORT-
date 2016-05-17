package com.github.birdgeek.breadbot.discord;

import net.dv8tion.jda.hooks.ListenerAdapter;


/**
 * Created by birdg on 5/11/2016.
 */
public class MemberJoinEvent extends ListenerAdapter {


    public MemberJoinEvent() {

    }

    public void onGuildMemberJoinEvent(MemberJoinEvent e) {

        String username = e.getUser().getUsername();

        e.getGuild().getPublicChannel().sendMessage("**" + username + "** has joined *" + e.getGuild().getName() + "*");



    }

}


