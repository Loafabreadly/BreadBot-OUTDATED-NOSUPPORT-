package com.github.birdgeek;

import java.util.function.Consumer;

import net.dv8tion.jda.events.message.priv.PrivateMessageReceivedEvent;

public  class MyConsumer implements Consumer<PrivateMessageReceivedEvent> {

	public MyConsumer(PrivateMessageReceivedEvent e) {
		// TODO Auto-generated constructor stub
	}

	public void accept(PrivateMessageReceivedEvent e) {
		e.getAuthor().getPrivateChannel().sendMessage("");
		
	}

}
