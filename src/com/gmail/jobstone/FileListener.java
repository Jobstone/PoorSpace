package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class FileListener implements Listener {
	
	private final PoorSpace plugin;
	
	public FileListener (PoorSpace plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onLogin(PlayerLoginEvent e) {
		File pFile = new File(plugin.getDataFolder(), "players/"+e.getPlayer().getName());
		if (!pFile.exists()) {
			pFile.mkdirs();
			List<String> list = new ArrayList<String>();
			File file0 = new File(pFile, "Overworld.yml");
			FileConfiguration config0 = YamlConfiguration.loadConfiguration(file0);
			config0.set("list", list);
			File file1 = new File(pFile, "Nether.yml");
			File file2 = new File(pFile, "End.yml");
			File file3 = new File(pFile, "Creative.yml");
			File file4 = new File(pFile, "Minigame.yml");
			
			File file5 = new File(pFile, "Default_Overworld.yml");
			FileConfiguration config5 = YamlConfiguration.loadConfiguration(file5);
			config5.set("group1", list);
			config5.set("group2", list);
			config5.set("group3", list);
			config5.set("permission1", "11111111");
			config5.set("permission2", "11111111");
			config5.set("permission3", "11111111");
			config5.set("permission4", "1100100011");
			File file6 = new File(pFile, "Default_Nether.yml");
			File file7 = new File(pFile, "Default_End.yml");
			File file8 = new File(pFile, "Default_Creative.yml");
			File file9 = new File(pFile, "Default_Minigame.yml");
			
			File file10 = new File(pFile, "settings.yml");
			FileConfiguration config10 = YamlConfiguration.loadConfiguration(file10);
			config10.set("spaceinfo", true);
			config10.createSection("selectors");
			
			File file11 = new File(pFile, "stats.yml");
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
		
	}

}
