package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
	
	public Set<String> getSelectorsSet() {
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


	public void joinGroup(String group) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(getSettingsFile());
		config.set("groups", config.getStringList("groups").add(group));
	}


	public void quitGroup(String group) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(getSettingsFile());
		config.set("groups", config.getStringList("groups").remove(group));
	}


	public boolean exists() {
		return this.folder.exists();
	}


	public void createFiles() {
		this.folder.mkdirs();
		List<String> list = new ArrayList<String>();
		File file0 = new File(this.folder, "Overworld.yml");
		FileConfiguration config0 = YamlConfiguration.loadConfiguration(file0);
		config0.set("list", list);
		File file1 = new File(this.folder, "Nether.yml");
		File file2 = new File(this.folder, "End.yml");
		File file3 = new File(this.folder, "Creative.yml");
		File file4 = new File(this.folder, "Minigame.yml");

		File file5 = new File(this.folder, "Default_Overworld.yml");
		FileConfiguration config5 = YamlConfiguration.loadConfiguration(file5);
		config5.set("group1", list);
		config5.set("group2", list);
		config5.set("group3", list);
		config5.set("permission1", "11111111");
		config5.set("permission2", "11111111");
		config5.set("permission3", "11111111");
		config5.set("permission4", "1100100011");
		File file6 = new File(this.folder, "Default_Nether.yml");
		File file7 = new File(this.folder, "Default_End.yml");
		File file8 = new File(this.folder, "Default_Creative.yml");
		File file9 = new File(this.folder, "Default_Minigame.yml");

		File file10 = new File(this.folder, "settings.yml");
		FileConfiguration config10 = YamlConfiguration.loadConfiguration(file10);
		config10.set("spaceinfo", true);
		config10.createSection("selectors");
		config10.set("groups", new ArrayList<>());

		File file11 = new File(this.folder, "stats.yml");
		FileConfiguration config11 = YamlConfiguration.loadConfiguration(file11);
		config11.set("giveups", 0);

		try {
			config0.save(file0);
			config0.save(file1);
			config0.save(file2);
			config0.save(file3);
			config0.save(file4);
			config5.save(file5);
			config5.save(file6);
			config5.save(file7);
			config5.save(file8);
			config5.save(file9);
			config10.save(file10);
			config11.save(file11);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}


	@Override
	public OwnerType getType() {
		return OwnerType.PLAYER;
	}
}
