package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PoorSpace extends JavaPlugin {
	
	public static PoorSpace plugin;
	public static SkullMeta slime;
	public static SkullMeta pig;
	public static SkullMeta chicken;
	
	public void onEnable() {
		
		PoorSpace.plugin = this;
		this.saveDefaultConfig();
		if (!new File(this.getDataFolder(), "chunks").exists()) {
			File chunks = new File(this.getDataFolder(), "chunks");
			chunks.mkdir();
			new File(chunks, "Overworld").mkdir();
			new File(chunks, "Nether").mkdir();
			new File(chunks, "End").mkdir();
			new File(chunks, "Creative").mkdir();
			new File(chunks, "Minigame").mkdir();
		}

		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta)item.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Slime"));
		PoorSpace.slime = meta.clone();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Pig"));
		PoorSpace.pig = meta.clone();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Chicken"));
		PoorSpace.chicken = meta.clone();

		
		//文件更新
		filesUpdate();
		
		new BukkitRunnable() {
			@Override
			public void run() {
				
				for (Player player : Bukkit.getOnlinePlayers()) {
					
					File pFile = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/settings.yml");
					if (pFile.exists()) {
						FileConfiguration config = YamlConfiguration.loadConfiguration(pFile);
						if (!config.getBoolean("spaceinfo")) {
							player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
							continue;
						}
					}
					
					Scoreboard board;
					Objective obj;
					board = Bukkit.getScoreboardManager().getNewScoreboard();
					obj = board.getObjective("PoorSpace");
					if (obj != null)
						obj.unregister();
					//obj = board.registerNewObjective("PoorSpace", "dummy", "§e§lPoorSpace");
					//obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					obj = board.registerNewObjective("PoorSpace", "dummy", "§e§lPoorSpace");
					obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					Location loc = player.getLocation();
					Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
					Score score1 = obj.getScore("§a当前空间："+space.id());
					score1.setScore(0);
					String owner = space.owner() == null ? "无" : space.owner();
					Score score2 = obj.getScore("§a所有者："+owner);
					score2.setScore(0);
					try {
						player.setScoreboard(board);
					} catch (IllegalStateException e) {}
				}
				
			}
		}.runTaskTimer(this, 20, 5);
		
		PluginCommand command = getCommand("poorspace");
		command.setExecutor(new SpaceExecutor(this));
		command.setTabCompleter(new SpaceTabCompleter());
		new FileListener(this);
		new InvListener(this);
		new SpaceOpen(this);
		new SpaceListener(this);
		
	}

	private void filesUpdate() {
		/*
		File players = new File(PoorSpace.plugin.getDataFolder(), "players");
		for (File pFile : players.listFiles()) {

			File file = new File(pFile, "settings.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if (!config.contains("groups")) {
				config.set("groups", new ArrayList<String>());
				try {
					config.save(file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}

		File file1 = new File(PoorSpace.plugin.getDataFolder(), "chunks/Overworld");
		for (File file : file1.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if (!config.contains("owner_type"))
				config.set("owner_type", SpaceOwner.OwnerType.PLAYER.name());
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File file2 = new File(PoorSpace.plugin.getDataFolder(), "chunks/Nether");
		for (File file : file2.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if (!config.contains("owner_type"))
				config.set("owner_type", SpaceOwner.OwnerType.PLAYER.name());
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File file3 = new File(PoorSpace.plugin.getDataFolder(), "chunks/End");
		for (File file : file3.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if (!config.contains("owner_type"))
				config.set("owner_type", SpaceOwner.OwnerType.PLAYER.name());
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		File file4 = new File(PoorSpace.plugin.getDataFolder(), "chunks/Creative");
		for (File file : file4.listFiles()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			if (!config.contains("owner_type"))
				config.set("owner_type", SpaceOwner.OwnerType.PLAYER.name());
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		(new File(PoorSpace.plugin.getDataFolder(), "groups")).mkdirs();
		*/
	}

}
