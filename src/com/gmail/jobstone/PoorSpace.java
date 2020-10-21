package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import com.gmail.jobstone.listener.FileListener;
import com.gmail.jobstone.listener.GeneralListener;
import com.gmail.jobstone.listener.InvListener;
import com.gmail.jobstone.listener.SpaceListener;
import com.gmail.jobstone.space.NormalSpace;
import com.gmail.jobstone.space.SpaceManager;
import com.gmail.jobstone.space.SpaceOpen;
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

		//文件更新
        oldDataBackup();
		filesUpdate();

		if (!new File(this.getDataFolder(), "spaces").exists()) {
			File spaces = new File(this.getDataFolder(), "spaces");
			spaces.mkdir();
			new File(spaces, "world").mkdir();
			new File(spaces, "world_nether").mkdir();
			new File(spaces, "world_the_end").mkdir();
			new File(spaces, "creative").mkdir();
			new File(spaces, "minigame").mkdir();
		}

		ItemStack item = new ItemStack(Material.PLAYER_HEAD);
		SkullMeta meta = (SkullMeta)item.getItemMeta();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Slime"));
		PoorSpace.slime = meta.clone();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Pig"));
		PoorSpace.pig = meta.clone();
		meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Chicken"));
		PoorSpace.chicken = meta.clone();
		
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
					obj = board.registerNewObjective("PoorSpace", "dummy", "§e§lPoorSpace");
					obj.setDisplaySlot(DisplaySlot.SIDEBAR);
					Location loc = player.getLocation();
					NormalSpace space = new NormalSpace(NormalSpace.getSpaceId(loc), NormalSpace.getWorldId(loc));
					Score score1 = obj.getScore("§a当前空间："+space.id());
					score1.setScore(0);
					String owner = space.owner() == null ? "无" : space.owner();
					Score score2 = obj.getScore("§a所有者："+owner);
					score2.setScore(0);
					try {
						player.setScoreboard(board);
					} catch (IllegalStateException ignored) {}
				}
				
			}
		}.runTaskTimer(this, 20, 5);

		SpaceManager.initialize();
		
		PluginCommand command = getCommand("poorspace");
		SpaceExecutor executor = new SpaceExecutor(this);
		command.setExecutor(executor);
		command.setTabCompleter(new SpaceTabCompleter());
		getCommand("ps-op").setExecutor(new OpExecutor());
		new FileListener(this);
		new InvListener(this);
		new SpaceOpen(this);
		new SpaceListener(this);
		new GeneralListener();
		
	}

	private void oldDataBackup() {
		File old = new File(getDataFolder().getParentFile(), "PoorSpace_old_data");
		if (!old.exists()) {
			try {
				final Path source = getDataFolder().toPath();
				final Path target = old.toPath();

				Files.walkFileTree(source, EnumSet.of(FileVisitOption.FOLLOW_LINKS), Integer.MAX_VALUE,
						new SimpleFileVisitor<Path>() {
							@Override
							public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
									throws IOException
							{
								Path targetdir = target.resolve(source.relativize(dir));
								try {
									Files.copy(dir, targetdir);
								} catch (FileAlreadyExistsException e) {
									if (!Files.isDirectory(targetdir))
										throw e;
								}
								return FileVisitResult.CONTINUE;
							}
							@Override
							public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
									throws IOException
							{
								Files.copy(file, target.resolve(source.relativize(file)));
								return FileVisitResult.CONTINUE;
							}
						});
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private void filesUpdate() {
		String[] worldNames = {"world", "world_nether", "world_the_end", "creative", "minigame"};
		String[] permissions = {"permission1", "permission2", "permission3", "permission4"};
		File spaceFolder = new File(getDataFolder(), "spaces");
		for (String worldName : worldNames) {
		    File worldFolder = new File(spaceFolder, worldName);
		    for (File subFolder : Objects.requireNonNull(worldFolder.listFiles())) {
		        for (File spaceFile : Objects.requireNonNull(subFolder.listFiles())) {
		            FileConfiguration config = YamlConfiguration.loadConfiguration(spaceFile);
		            for (int i = 0; i < 4; ++i) {
		                char[] pm = Objects.requireNonNull(config.getString(permissions[i])).toCharArray();
		                ArrayList<Character> list = new ArrayList<>();
		                for (char pms : pm)
		                    list.add(pms);
                        list.add(7, pm[2]);
                        StringBuilder sb = new StringBuilder();
                        for (char c : list)
                            sb.append(c);
                        config.set(permissions[i], sb.toString());
                    }
		            try {
                        config.save(spaceFile);
                    } catch (IOException e) {
		                e.printStackTrace();
                    }
                }
            }
        }

		File playerFolders = new File(getDataFolder(), "players");
		for (File playerFolder : Objects.requireNonNull(playerFolders.listFiles())) {
		    for (String worldName : worldNames) {
		        File worldFile = new File(playerFolder, "default_" + worldName + ".yml");
		        FileConfiguration config = YamlConfiguration.loadConfiguration(worldFile);
                for (int i = 0; i < 4; ++i) {
                    char[] pm = Objects.requireNonNull(config.getString(permissions[i])).toCharArray();
                    ArrayList<Character> list = new ArrayList<>();
                    for (char pms : pm)
                        list.add(pms);
                    list.add(7, pm[2]);
                    StringBuilder sb = new StringBuilder();
                    for (char c : list)
                        sb.append(c);
                    config.set(permissions[i], sb.toString());
                }
                try {
                    config.save(worldFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

	}

}
