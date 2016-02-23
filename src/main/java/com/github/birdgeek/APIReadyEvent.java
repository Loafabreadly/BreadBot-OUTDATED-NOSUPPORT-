package com.github.birdgeek;

import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.ReadyEvent;
import net.dv8tion.jda.hooks.EventListener;


public class APIReadyEvent implements EventListener {
	static boolean isReady = false;

	@Override
	public void onEvent(Event event) {
		if (event instanceof ReadyEvent) {
			System.out.println("Ready");
			isReady = true;
			BotMain.sendWelcome();
		}
		else
			isReady = false;
		
	}

}
