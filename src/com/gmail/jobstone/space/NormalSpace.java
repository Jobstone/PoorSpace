package com.gmail.jobstone.space;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gmail.jobstone.PoorSpace;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class NormalSpace extends Space {

	private String id;
	private String owner;
	private SpaceOwner.OwnerType ownerType;

	
	public NormalSpace(String id, int world) {
		this.id = id;
		this.world = world;
		this.file = NormalSpace.getFile(world, id);
        if (file.exists()) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			owner = config.getString("owner");
			ownerType = SpaceOwner.OwnerType.valueOf(config.getString("owner_type"));
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
			ownerType = null;
			if (world == 3)
				permission4[1] = '0';
		}
	}

	public String id() {
		return id;
	}
	
	public String owner() {
		return owner;
	}

	public SpaceOwner.OwnerType getOwnerType() {
		return this.ownerType;
	}

	public SpaceOwner getOwner() {
		if (this.ownerType.equals(SpaceOwner.OwnerType.PLAYER))
			return new SpacePlayer(owner);
		else
			return null;
	}

	
	public ItemStack toItem() {
		ItemStack item = new ItemStack(Material.PAPER);
		ItemMeta meta = item.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setDisplayName("§a§l空间"+id);
		ArrayList<String> lore = new ArrayList<String>();
		if (this.ownerType.equals(SpaceOwner.OwnerType.PLAYER))
			lore.add("§7所有者：§e"+owner);
		else
			lore.add("§7所有者：§e"+owner+"[群组]");
		lore.add("§e点击查看");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public void setOwner(SpaceOwner spaceOwner) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		if (!file.exists()) {
			List<String> list = new ArrayList<>();
			config.set("group1", list);
			config.set("group2", list);
			config.set("group3", list);
			config.set("permission1", String.valueOf(permission1));
			config.set("permission2", String.valueOf(permission2));
			config.set("permission3", String.valueOf(permission3));
			config.set("permission4", String.valueOf(permission4));
		}
		else
			this.getOwner().removeSpace(world, id);

		File defaultFile = spaceOwner.getDefaultWorldFile(world);
		if (defaultFile.exists()) {
			FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(defaultFile);
			config.set("group1", defaultConfig.getStringList("group1"));
			config.set("group2", defaultConfig.getStringList("group2"));
			config.set("group3", defaultConfig.getStringList("group3"));
			config.set("permission1", defaultConfig.getString("permission1"));
			config.set("permission2", defaultConfig.getString("permission2"));
			config.set("permission3", defaultConfig.getString("permission3"));
			config.set("permission4", defaultConfig.getString("permission4"));
		}

		spaceOwner.addSpace(world, id);
		owner = spaceOwner.getName();
		ownerType = spaceOwner.getType();
		config.set("owner", owner);
		config.set("owner_type", ownerType.name());
		try {
			config.save(file);
            this.update();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setGroup(int group, List<String> list) {
		super.setGroup(group, list);
		this.update();
	}

	//返回值：0.失败 1.成功 2.人数已满
	@Override
	public int addGroup(int group, List<String> names) {
		int result = super.addGroup(group, names);
		if (1 == result)
			this.update();
		return result;
	}

	//返回值：0.失败 1.成功
	@Override
	public boolean removeGroup(int group, List<String> names) {
		boolean result = super.removeGroup(group, names);
		if (result)
			this.update();
		return result;
	}

	@Override
	public void setPermission(int i, char[] pm) {
		super.setPermission(i, pm);
		this.update();
		System.out.println("set permission");
	}
	
	public void remove() {
		SpaceOwner spaceOwner;
		if (this.ownerType.equals(SpaceOwner.OwnerType.PLAYER))
			spaceOwner = new SpacePlayer(owner);
		else
			return;
		spaceOwner.removeSpace(world, id);
		this.file.delete();
		this.update();
	}

	public void update() {
        SpaceManager manager = SpaceManager.getSpaceManager(world);
        manager.update(id, new NormalSpace(id, world));
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
					NormalSpace.limit.put(player.getName(), NormalSpace.limit.get(player.getName())-1);
					this.cancel();
				}
			}
		}.runTaskTimer(PoorSpace.plugin, 0, 50);
		
	}

	
	public static Map<String, Integer> limit = new HashMap<>();
	
	
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
		return NormalSpace.getFile(world, id).exists();
	}
	
	public static int getWorldId(Location loc) {
		String world = loc.getWorld().getName();
		return Space.getWorldId(world);
	}
	
	public static String getSpaceId(Location loc) {
		Chunk chunk = loc.getChunk();
		double y = loc.getY();
		String world = loc.getWorld().getName();
		if (world.equals("world")) {
			if (y < 20) {
				return chunk.getX()+"."+chunk.getZ()+".0";
			}
			else if (y < 50) {
				return chunk.getX()+"."+chunk.getZ()+".1";
			}
			else if (y < 100) {
				return chunk.getX()+"."+chunk.getZ()+".2";
			}
			else if (y < 200) {
				return chunk.getX()+"."+chunk.getZ()+".3";
			}
			else {
				return chunk.getX()+"."+chunk.getZ()+".4";
			}
		}
		else if (world.equals("world_nether")) {
			if (y < 50) {
				return chunk.getX()+"."+chunk.getZ()+".0";
			}
			else if (y < 128) {
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
			if (y >= 0 && y <= NormalSpace.getWorldMax(world))
				return true;
			else
				return false;
		} catch (NumberFormatException e) {
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

	public static File getFile(int world, String id) {
		String[] splits = id.split("\\.");
		if (splits.length != 3)
			return null;
		try {
			int x = Integer.parseInt(splits[0]);
			int y = Integer.parseInt(splits[1]);

			int groupX = x >> 5;
			int groupY = y >> 5;
			File folder = new File(PoorSpace.plugin.getDataFolder(),
					"spaces/" + Space.getWorldName(world) + "/" + groupX + "." + groupY);
			return new File(folder, id + ".yml");
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public String toString() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof NormalSpace) {
			NormalSpace space = (NormalSpace)o;
			return space.world() == this.world && space.id().equals(this.id);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 7;
		result = 31 * result + this.world;
		result = 31 * result + this.id().hashCode();
		return result;
	}

}
