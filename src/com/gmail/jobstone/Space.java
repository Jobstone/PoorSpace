package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Space {
	
	private static JavaPlugin plugin = SpaceOpen.plugin;;
	private String id;
	private int world;
	private File file;
	private String owner;
	private List<String> group1 = new ArrayList<String>();
	private List<String> group2 = new ArrayList<String>();
	private List<String> group3 = new ArrayList<String>();
	private char[] permission1 = {'1', '1', '1', '1', '1', '1', '1', '1'};
	private char[] permission2 = {'1', '1', '1', '1', '1', '1', '1', '1'};
	private char[] permission3 = {'1', '1', '1', '1', '1', '1', '1', '1'};
	private char[] permission4 = {'1', '1', '0', '0', '1', '1', '1', '1', '1', '0'};
	
	public Space(String s, int world) {
		id = s;
		this.world = world;
		String w = "Overworld";
		switch(world) {
		case 0:
			w = "Overworld";
			break;
		case 1:
			w = "Nether";
			break;
		case 2:
			w = "End";
			break;
		case 3:
			w = "Creative";
			break;
		case 4:
			w = "Minigame";
			break;
		}
		file = new File(plugin.getDataFolder(), "chunks/"+w+"/"+id+".yml");
		if (file.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			owner = config.getString("owner");
			group1 = config.getStringList("group1");
			group2 = config.getStringList("group2");
			group3 = config.getStringList("group3");
			permission1 = config.getString("permission1").toCharArray();
			permission2 = config.getString("permission2").toCharArray();
			permission3 = config.getString("permission3").toCharArray();
			permission4 = config.getString("permission4").toCharArray();
		}
		else {
			owner = null;
			if (world == 3)
				permission4[7] = '0';
		}
	}
	
	public String id() {
		return id;
	}
	
	public int world() {
		return world;
	}
	
	public File file() {
		return file;
	}
	
	public String owner() {
		return owner;
	}
	
	public List<String> group(int i) {
		switch(i) {
		case 1:
			return group1;
		case 2:
			return group2;
		case 3:
			return group3;
		}
		return null;
	}
	
	public char[] permission(int i) {
		switch(i) {
		case 1:
			return permission1;
		case 2:
			return permission2;
		case 3:
			return permission3;
		case 4:
			return permission4;
		}
		return null;
	}
	
	public void setGroup(int group, List<String> list) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		switch(group) {
		case 1:
			group1 = list;
			config.set("group1", group1);
			break;
		case 2:
			group2 = list;
			config.set("group2", group2);
			break;
		case 3:
			group3 = list;
			config.set("group3", group3);
			break;
		}
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setPermission(int i, char[] pm) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		switch(i) {
		case 1:
			permission1 = pm;
			config.set("permission1", String.valueOf(permission1));
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			permission2 = pm;
			config.set("permission2", String.valueOf(permission2));
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 3:
			permission3 = pm;
			config.set("permission3", String.valueOf(permission3));
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case 4:
			permission4 = pm;
			config.set("permission4", String.valueOf(permission4));
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	public boolean canExplode() {
		switch(permission4[8]) {
		case '0':
			return true;
		default:
			return false;
		}
	}
	
	public boolean canFire() {
		switch(permission4[9]) {
		case '0':
			return true;
		default:
			return false;
		}
	}
	
	public ItemStack toItem() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName("§a§l空间"+id);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7所有者：§e"+owner);
		lore.add("§e点击查看");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public void setOwner(String player) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		List<String> list = new ArrayList<String>();
		String w = "Overworld";
		switch(world) {
		case 0:
			w = "Overworld";
			break;
		case 1:
			w = "Nether";
			break;
		case 2:
			w = "End";
			break;
		case 3:
			w = "Creative";
			break;
		case 4:
			w = "Minigame";
			break;
		}
		File pFile = new File(plugin.getDataFolder(), "players/"+player+"/"+w+".yml");
		FileConfiguration pconfig = YamlConfiguration.loadConfiguration(pFile);
		if (!file.exists()) {
			config.set("group1", list);
			config.set("group2", list);
			config.set("group3", list);
			config.set("permission1", String.valueOf(permission1));
			config.set("permission2", String.valueOf(permission2));
			config.set("permission3", String.valueOf(permission3));
			config.set("permission4", String.valueOf(permission4));
		}
		else {
			File bFile = new File(plugin.getDataFolder(), "players/"+owner+"/"+w+".yml");
			FileConfiguration bconfig = YamlConfiguration.loadConfiguration(bFile);
			List<String> list2 = bconfig.getStringList("list");
			list2.remove(id);
			bconfig.set("list", list2);
			try {
				bconfig.save(bFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File newFile = new File(plugin.getDataFolder(), "players/"+player+"/Default_"+w+".yml");
		if (newFile.exists()) {
			FileConfiguration newconfig = YamlConfiguration.loadConfiguration(newFile);
			config.set("group1", newconfig.getStringList("group1"));
			config.set("group2", newconfig.getStringList("group2"));
			config.set("group3", newconfig.getStringList("group3"));
			config.set("permission1", newconfig.getString("permission1"));
			config.set("permission2", newconfig.getString("permission2"));
			config.set("permission3", newconfig.getString("permission3"));
			config.set("permission4", newconfig.getString("permission4"));
		}
		List<String> list1 = pconfig.getStringList("list");
		list1.add(id);
		pconfig.set("list", list1);
		owner = player;
		config.set("owner", owner);
		try {
			config.save(file);
			pconfig.save(pFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void remove() {
		String w = "Overworld";
		switch(world) {
		case 0:
			w = "Overworld";
			break;
		case 1:
			w = "Nether";
			break;
		case 2:
			w = "End";
			break;
		case 3:
			w = "Creative";
			break;
		case 4:
			w = "Minigame";
			break;
		}
		File pFile = new File(plugin.getDataFolder(), "players/"+owner+"/"+w+".yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(pFile);
		List<String> list = config.getStringList("list");
		list.remove(id);
		config.set("list", list);
		try {
			config.save(file);
			config.save(pFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.file.delete();
	}
	
	public static void showParticle(Player player, String id, int world) {
		
		int startx = 16*Integer.parseInt(id.substring(0, id.indexOf(".")));
		int y = Integer.parseInt(id.substring(id.lastIndexOf(".")+1));
		int startz = 16*Integer.parseInt(id.substring(id.indexOf(".")+1, id.lastIndexOf(".")));
		final int top;
		final int bottom;
		
		if (world == 0) {
			switch(y) {
			case 0:
				bottom = 0;
				top = 20;
				break;
			case 1:
				bottom = 20;
				top = 50;
				break;
			case 2:
				bottom = 50;
				top = 100;
				break;
			case 3:
				bottom = 100;
				top = 200;
				break;
			case 4:
				bottom = 200;
				top = 256;
				break;
			default:
				bottom = 0;
				top = 256;
			}
		}
		else if (world == 1) {
			switch(y) {
			case 0:
				bottom = 0;
				top = 50;
				break;
			case 1:
				bottom = 50;
				top = 128;
				break;
			case 2:
				bottom = 128;
				top = 256;
				break;
			default:
				bottom = 0;
				top = 256;
			}
		}
		else {
			bottom = 0;
			top = 256;
		}
		
		World w = player.getWorld();
		
		int startx1 = startx+16;
		int startz1 = startz+16;
		Set<int[]> set = new HashSet<>();
		
		for (int i = 0; bottom+i <= top; i = i+2)
			for (int j = 0; j <= 16; j = j+2) {
				set.add(new int[]{startx, bottom+i, startz+j});
				set.add(new int[]{startx1, bottom+i, startz+j});
			}
		for (int i = 0; i <= 16; i = i+2)
			for (int j = 0; j <= 16; j = j+2) {
				set.add(new int[]{startx+i, bottom, startz+j});
				set.add(new int[]{startx+i, top, startz+j});
			}
		for (int i = 0; i <= 16; i = i+2)
			for (int j = 0; bottom+j <= top; j = j+2) {
				set.add(new int[]{startx+i, bottom+j, startz});
				set.add(new int[]{startx+i, bottom+j, startz1});
			}
		
		new BukkitRunnable() {
			int times = 0;
			
			@Override
			public void run() {
				
				for (int[] s : set)
					player.spawnParticle(Particle.FIREWORKS_SPARK, new Location(w, s[0], s[1], s[2]), 1, 0, 0, 0, 0);
				times++;
				if (times == 10) {
					Space.limit.put(player.getName(), Space.limit.get(player.getName())-1);
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 50);
		
	}
	
	public static Map<String, Integer> limit = new HashMap<String, Integer>();
	
	
	public static int cost(String id, int world) {
		String m = id.substring(id.lastIndexOf(".")+1);
		if (world == 0) {
			switch(m) {
			case "0":
				return 120;
			case "1":
				return 80;
			case "2":
				return 120;
			case "3":
				return 160;
			case "4":
				return 160;
			}
		}
		else if (world == 1) {
			switch(m) {
			case "0":
				return 100;
			case "1":
				return 150;
			case "2":
				return 300;
			}
		}
		else if (world == 2)
			return 300;
		else if (world == 3)
			return 100;
		return 9999;
	}
	
	public static boolean isOwned(String id, int world) {
		String w = "error";
		switch(world) {
		case 0:
			w = "Overworld";
			break;
		case 1:
			w = "Nether";
			break;
		case 2:
			w = "End";
			break;
		case 3:
			w = "Creative";
			break;
		case 4:
			w = "Minigame";
			break;
		}
		
		if (new File(plugin.getDataFolder(), "chunks/"+w+"/"+id+".yml").exists())
			return true;
		else
			return false;
	}
	
	public static int getWorldid(Location loc) {
		String world = loc.getWorld().getName();
		return Space.getWorldid(world);
	}
	
	public static int getWorldid(String world) {
		switch(world) {
		case "world":
			return 0;
		case "world_nether":
			return 1;
		case "world_the_end":
			return 2;
		case "creative":
			return 3;
		case "minigame":
			return -1;
		default:
			return -1;
		}
	}
	
	public static String getSpaceid(Location loc) {
		Chunk chunk = loc.getChunk();
		double y = loc.getY();
		String world = loc.getWorld().getName();
		if (world.equals("world")) {
			if (y <= 20) {
				return chunk.getX()+"."+chunk.getZ()+".0";
			}
			else if (y <= 50) {
				return chunk.getX()+"."+chunk.getZ()+".1";
			}
			else if (y <= 100) {
				return chunk.getX()+"."+chunk.getZ()+".2";
			}
			else if (y <= 200) {
				return chunk.getX()+"."+chunk.getZ()+".3";
			}
			else {
				return chunk.getX()+"."+chunk.getZ()+".4";
			}
		}
		else if (world.equals("world_nether")) {
			if (y <= 50) {
				return chunk.getX()+"."+chunk.getZ()+".0";
			}
			else if (y <= 128) {
				return chunk.getX()+"."+chunk.getZ()+".1";
			}
			else {
				return chunk.getX()+"."+chunk.getZ()+".2";
			}
		}
		else
			return chunk.getX()+"."+chunk.getZ()+".0";
	}
	
	public static boolean isSpaceLegal(String id, int world) {
		if (!id.contains(".") || !id.substring(id.indexOf(".")+1).contains("."))
			return false;
		try {
			Integer.parseInt(id.substring(0, id.indexOf(".")));
			int y = Integer.parseInt(id.substring(id.lastIndexOf(".")+1));
			Integer.parseInt(id.substring(id.indexOf(".")+1, id.lastIndexOf(".")));
			if (y >= 0 && y <= Space.getWorldMax(world))
				return true;
			else
				return false;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	public static boolean isGroupLegal(String group) {
		switch(group) {
		case "1":
		case "2":
		case "3":
			return true;
		default:
			return false;
		}
	}
	
	public static int getWorldMax(int world) {
		switch(world) {
		case 0:
			return 4;
		case 1:
			return 2;
		case 2:
		case 3:
		case 4:
			return 0;
		default:
			return -1;
		}
	}
	
	public static List<String> getSpaceList(String player, int world) {
		String w;
		switch(world) {
		case 0:
			w = "/Overworld.yml";
			break;
		case 1:
			w = "/Nether.yml";
			break;
		case 2:
			w = "/End.yml";
			break;
		case 3:
			w = "/Creative.yml";
			break;
		default:
			return null;
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player+w));
		return config.getStringList("list");
	}
	
	public static String getWorldName(int world) {
		switch(world) {
		case 0:
			return "Overworld";
		case 1:
			return "Nether";
		case 2:
			return "End";
		case 3:
			return "Creative";
		case 4:
			return "Minigame";
		default:
			return null;
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Space) {
			Space space = (Space)o;
			if (space.world() == this.world && space.id().equals(this.id))
				return true;
		}
		return false;
	}

}
