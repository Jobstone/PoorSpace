package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpacePlayer extends SpaceOwner{
	
	public SpacePlayer(String player) {
		this.name = player;
		this.folder = new File(PoorSpace.plugin.getDataFolder(), "players/"+player);
	}

	public File getSettingsFile() {
		return new File(this.folder, "settings.yml");
	}
	
	public Set<String> getSelectors() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(getSettingsFile());
		if (config.contains("selectors"))
			return config.getConfigurationSection("selectors").getKeys(false);
		else
			return new HashSet<String>();
	}
	
	public boolean containsSelector(String selector) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(getSettingsFile());
		if (config.contains("selectors."+selector, false))
			return true;
		return false;
	}
	
	public void setSelector(String name, String selector) {
		File settings = getSettingsFile();
		FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
		config.set("selectors."+name, selector);
		try {
			config.save(settings);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public List<String> getGroups() {
		FileConfiguration config = YamlConfiguration.loadConfiguration(getSettingsFile());
		if (config.contains("groups")) {
			return config.getStringList("groups");
		}
		else
			return new ArrayList<String>();
	}

	@Override
	public OwnerType getType() {
		return OwnerType.PLAYER;
	}
}
