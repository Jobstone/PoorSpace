package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpacePlayer {
	
	private final File settings;
	
	public SpacePlayer(String player) {
		this.settings = new File(PoorSpace.plugin.getDataFolder(), "players/"+player+"/settings.yml");
	}
	
	public File getFile() {
		return settings;
	}
	
	public Set<String> getSelectors() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
		if (config.contains("selectors"))
			return config.getConfigurationSection("selectors").getKeys(false);
		else
			return new HashSet<String>();
	}
	
	public boolean containsSelector(String selector) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
		if (config.contains("selectors."+selector, false))
			return true;
		return false;
	}
	
	public void setSelector(String name, String selector) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
		config.set("selectors."+name, selector);
		try {
			config.save(settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
