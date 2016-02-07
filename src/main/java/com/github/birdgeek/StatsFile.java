package com.github.birdgeek;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;


public class StatsFile {
	//static PropertiesConfiguration statsFile = new PropertiesConfiguration();
	String fileName = "stats.cfg";
	static PropertiesConfiguration config;
	
	public StatsFile () throws ConfigurationException {
		StatsFile.config = new PropertiesConfiguration(fileName);
	}
	
	
	public int getStats(String cmd) {
		return config.getInt(cmd);
	}
	static public void readKeys() {
		System.out.println(config.getInt("ping"));
	}
	static public void updateCount(String cmd) throws ConfigurationException {
		int i = config.getInt(cmd);
		config.setProperty(cmd, i + 1);
		config.save();
	}

}
