package com.github.birdgeek.breadbot.utility;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import com.github.birdgeek.breadbot.BotMain;

import net.dv8tion.jda.MessageBuilder;
import net.dv8tion.jda.events.message.guild.GuildMessageReceivedEvent;

public class GoogleSearch {

	static Logger systemLog;
	String query = null;
	static String address = "http://www.google.com/search?q=";
	static String charset = "UTF-8";
	static String userAgent = "Breadbot " + BotMain.version + " (+http://birdgeek.github.io/BreadBot/)";
	
	public GoogleSearch (Logger log) {
		GoogleSearch.systemLog = log;
	}
	
	public static void search(String query, GuildMessageReceivedEvent event) throws IOException {
			 Elements links = Jsoup.connect(address + URLEncoder.encode(query, charset) + "&num=2").userAgent(userAgent).get().select(".g>.r>a");
			 for (Element link : links) {
				    String title = link.text();
				    String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
				    url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

				    if (!url.startsWith("http")) {
				        continue; // Ads/news/etc.
				    }			 
			 
			 String str = new MessageBuilder()
					 .appendString("**Site Title:** " + title
							 + "\n**URL**: " + url)
					 .build().getContent();
			//String str1 = str.replaceAll("<b>", "");
			//String str2 =  str1.replaceAll("</b>", "");
			 event.getChannel().sendMessage(str);
			

			 }
	}
}
