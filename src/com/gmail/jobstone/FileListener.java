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

		SpacePlayer player = new SpacePlayer(e.getPlayer().getName());
		if (!player.exists())
			player.createFiles();
		
	}

}
