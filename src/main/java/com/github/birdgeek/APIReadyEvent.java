package com.github.birdgeek;

import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;


public class APIReadyEvent extends ListenerAdapter {

	public void onEvent(Event event) {
		if (event instanceof ReadyEvent) {
			System.out.println("API Ready");
			BotMain.sendWelcome();
			event.getJDA().getAccountManager().setGame("Testerino");
			event.getJDA().getAccountManager().update();
		}
	}
}
