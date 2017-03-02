package com.github.birdgeek.breadbot.utility;

/**
 * Created by birdg on 5/10/2016.
 */
public class Logger {

    static int guildMessageCount = 0;
    static int privateMessageCount = 0; //TODO dont leave these as set to 0

    public Logger() {
        //TODO work with serializations here
    }
    public static void countGuildMessage() {
        guildMessageCount ++;
    }

    public static void countPrivateMessage() {
        privateMessageCount ++;
    }
}
