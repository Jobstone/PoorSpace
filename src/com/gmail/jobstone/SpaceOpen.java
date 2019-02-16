package com.gmail.jobstone;

import java.io.File;
import java.util.*;

import net.minecraft.server.v1_13_R2.MojangsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class SpaceOpen {
	
	public static PoorSpace plugin;
	public static Map<String, List<String>> searchResults = new HashMap<>();
	
	public SpaceOpen(PoorSpace plugin) {
		SpaceOpen.plugin = plugin;
	}
	
	public static void openPlayer(Player player) {
		
		Inventory pinv = Bukkit.getServer().createInventory(null, 9, "§1PoorSpace――个人");
		
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§a点击查看您在主世界拥有的空间");
		pinv.setItem(2, newItem(Material.GRASS_BLOCK, "§a§l主世界", lore));
		lore.clear();
		lore.add("§a点击查看您在下界拥有的空间");
		pinv.setItem(3, newItem(Material.NETHERRACK, "§a§l下界", lore));
		lore.clear();
		lore.add("§a点击查看您在末地拥有的空间");
		pinv.setItem(4, newItem(Material.END_STONE, "§a§l末地", lore));
		lore.clear();
		lore.add("§a点击查看您在创造界拥有的空间");
		pinv.setItem(5, newItem(Material.SANDSTONE, "§a§l创造界", lore));
		lore.clear();
		lore.add("§a点击查看您在小游戏界拥有的空间");
		pinv.setItem(6, newItem(Material.DIAMOND_SWORD, "§a§l小游戏界", lore));
		
		player.openInventory(pinv);
	}
	
	public static void openWorld(Player player, int world, int page) {
		
		if (world == 0) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Overworld.yml"));
			List<String> list = config.getStringList("list");
			subOpenWorld(player, list, "主世界", Material.GRASS_BLOCK, page, world);
		}
		else if (world == 1) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Nether.yml"));
			List<String> list = config.getStringList("list");
			subOpenWorld(player, list, "下界", Material.NETHERRACK, page, world);
		}
		else if (world == 2) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/End.yml"));
			List<String> list = config.getStringList("list");
			subOpenWorld(player, list, "末地", Material.END_STONE, page, world);
		}
		else if (world == 3) {
			FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Creative.yml"));
			List<String> list = config.getStringList("list");
			subOpenWorld(player, list, "创造界", Material.SANDSTONE, page, world);
		}
		else if (world == 4) {
			
		}
		
	}
	
	private static void subOpenWorld(Player player, List<String> list, String w, Material material, int page, int world) {
		int totalpage = (list.size()-1)/45+1;
		if (totalpage < 1)
			totalpage = 1;
		int imax;
		if (page > totalpage)
			page = totalpage;
		else if (page < 1)
			page = 1;
		
		Inventory inv = Bukkit.getServer().createInventory(null, 54, "§1PoorSpace――个人："+w+" 页数："+page+"/"+totalpage);
		if (page < totalpage) {
			imax = page*45;
			inv.setItem(53, newItem(Material.ARROW, "§a§l下一页"));
			if (page > 1)
				inv.setItem(45, newItem(Material.ARROW, "§a§l上一页"));
		}
		else {
			imax = list.size();
			if (page > 1)
				inv.setItem(45, newItem(Material.ARROW, "§a§l上一页"));
		}
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§e点击返回选择世界");
		inv.setItem(49, newItem(Material.BARRIER, "§a§l返回", lore));
		lore.clear();
		lore.add("§e点击查看"+w+"出生点附近区块");
		inv.setItem(50, newItem(material, "§a§l"+w, lore));
		int istart = (page-1)*45+1;
		
		for(int i = istart; i <= imax; i++) {
			Space space = new Space(list.get(i-1), world);
			inv.setItem(i-istart, space.toItem());
		}
		
		player.openInventory(inv);
	}
	
	public static void openSpace(Player player, String spaceid, int world) {
		Space space = new Space(spaceid, world);
		String w = "主世界";
		Material material = Material.GRASS_BLOCK;
		switch(world) {
		case 0:
			w = "主世界";
			material = Material.GRASS_BLOCK;
			break;
		case 1:
			w = "下界";
			material = Material.NETHERRACK;
			break;
		case 2:
			w = "末地";
			material = Material.END_STONE;
			break;
		case 3:
			w = "创造界";
			material = Material.SANDSTONE;
			break;
		case 4:
			w = "小游戏界";
			material = Material.DIAMOND_SWORD;
			break;
		}
		Inventory inv = Bukkit.getServer().createInventory(null, 18, "§1PoorSpace――"+w+"空间"+spaceid);
		
		String chunkid = spaceid.substring(0, spaceid.length()-2);
		ArrayList<String> lore = new ArrayList<String>();
		Material spacem;
		if (Space.isOwned(chunkid+".0", world)) {
			lore.add("§a拥有者："+new Space(chunkid+".0", world).owner());
			spacem = Material.MAP;
		}
		else {
			lore.add("§7无拥有者");
			spacem = Material.PAPER;
		}
		lore.add("§b"+spaceY(0, world));
		lore.add("§a点击切换");
		boolean enchant0 = spaceid.equals(chunkid+".0")?true:false;
		inv.setItem(0, newItem(spacem, "§a§l空间"+chunkid+".0", lore, enchant0));
		
		if (world == 0 || world == 1) {
			lore.clear();
			if (Space.isOwned(chunkid+".1", world)) {
				lore.add("§a拥有者："+new Space(chunkid+".1", world).owner());
				spacem = Material.MAP;
			}
			else {
				lore.add("§7无拥有者");
				spacem = Material.PAPER;
			}
			lore.add("§b"+spaceY(1, world));
			lore.add("§a点击切换");
			boolean enchant1 = spaceid.equals(chunkid+".1")?true:false;
			inv.setItem(1, newItem(spacem, "§a§l空间"+chunkid+".1", lore, enchant1));
			
			lore.clear();
			if (Space.isOwned(chunkid+".2", world)) {
				lore.add("§a拥有者："+new Space(chunkid+".2", world).owner());
				spacem = Material.MAP;
			}
			else {
				lore.add("§7无拥有者");
				spacem = Material.PAPER;
			}
			lore.add("§b"+spaceY(2, world));
			lore.add("§a点击切换");
			boolean enchant2 = spaceid.equals(chunkid+".2")?true:false;
			inv.setItem(2, newItem(spacem, "§a§l空间"+chunkid+".2", lore, enchant2));
			
			if (world == 0) {
				lore.clear();
				if (Space.isOwned(chunkid+".3", world)) {
					lore.add("§a拥有者："+new Space(chunkid+".3", world).owner());
					spacem = Material.MAP;
				}
				else {
					lore.add("§7无拥有者");
					spacem = Material.PAPER;
				}
				lore.add("§b"+spaceY(3, world));
				lore.add("§a点击切换");
				boolean enchant3 = spaceid.equals(chunkid+".3")?true:false;
				inv.setItem(3, newItem(spacem, "§a§l空间"+chunkid+".3", lore, enchant3));
				
				lore.clear();
				if (Space.isOwned(chunkid+".4", world)) {
					lore.add("§a拥有者："+new Space(chunkid+".4", world).owner());
					spacem = Material.MAP;
				}
				else {
					lore.add("§7无拥有者");
					spacem = Material.PAPER;
				}
				lore.add("§b"+spaceY(4, world));
				lore.add("§a点击切换");
				boolean enchant4 = spaceid.equals(chunkid+".4")?true:false;
				inv.setItem(4, newItem(spacem, "§a§l空间"+chunkid+".4", lore, enchant4));
			}
		}
		
		lore.clear();
		lore.add("§a点击查看此区块周围的区块");
		inv.setItem(8, newItem(Material.MAP, "§e§l周围区块", lore));
		
		lore.clear();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Overworld.yml"));
		List<String> list = config.getStringList("list");
		int cost = Space.cost(spaceid, world);
		String costs = cost+"";
		if (world == 0) {
			if (list.isEmpty())
				costs = "§m"+cost+"§e0";
			else if (list.size() < 4)
				costs = "§m"+cost+"§e"+(cost-40);
				
		}
		if (space.owner() == null) {
			lore.add("§e点击花费"+costs+"经验值购买此空间");
			inv.setItem(16, newItem(Material.EXPERIENCE_BOTTLE, "§a§l购买空间"+spaceid, lore));
			lore.clear();
			lore.add("§7无拥有者");
			spacem = Material.PAPER;
		}
		else {
			if (space.owner().equals(player.getName())) {
				lore.add("§c点击放弃该空间（无经验返还！）");
				inv.setItem(16, newItem(Material.COBWEB, "§4§l放弃空间"+spaceid, lore));
				lore.clear();
			}
			lore.add("§a拥有者："+space.owner());
			spacem = Material.MAP;
		}
		lore.add("§a点击显示空间边界（效果仅自己能看到）");
		inv.setItem(9, newItem(spacem, "§e§l空间"+spaceid, lore));
		
		lore.clear();
		lore.addAll(space.group(1));
		lore.add("§a点击查看设置");
		inv.setItem(10, newItem(Material.SIGN, "§e§l权限组1", lore));
		lore.clear();
		lore.addAll(space.group(2));
		lore.add("§a点击查看设置");
		inv.setItem(11, newItem(Material.SIGN, "§e§l权限组2", lore));
		lore.clear();
		lore.addAll(space.group(3));
		lore.add("§a点击查看设置");
		inv.setItem(12, newItem(Material.SIGN, "§e§l权限组3", lore));
		lore.clear();
		lore.add("§7默认权限组");
		lore.add("§a点击查看设置");
		inv.setItem(13, newItem(Material.SIGN, "§e§l权限组4", lore));
		
		lore.clear();
		lore.add("§e点击查看您在"+w+"拥有的空间");
		inv.setItem(17, newItem(material, "§a§l"+w, lore));
		
		player.openInventory(inv);
	}
	
	public static void openBuy(Player player, String id, int world) {
		String w = world(world);
		Inventory inv = Bukkit.getServer().createInventory(null, 9, "§1PoorSpace――"+w+"空间"+id+"购买");
		
		ArrayList<String> lore = new ArrayList<String>();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players/"+player.getName()+"/Overworld.yml"));
		List<String> list = config.getStringList("list");
		int cost = Space.cost(id, world);
		String costs = cost+"";
		if (world == 0) {
			if (list.isEmpty())
				costs = "§m"+cost+"§e0";
			else if (list.size() < 4)
				costs = "§m"+cost+"§e"+(cost-40);
				
		}
		lore.add("§e点击确认花费"+costs+"购买该空间！");
		inv.setItem(3, newItem(Material.EXPERIENCE_BOTTLE, "§a§l确认购买", lore));
		lore.clear();
		lore.add("§e点击取消购买");
		inv.setItem(5, newItem(Material.BARRIER, "§4§l取消购买", lore));
		
		player.openInventory(inv);
	}
	
	public static void openGiveup(Player player, String id, int world) {
		String w = world(world);
		Inventory inv = Bukkit.getServer().createInventory(null, 9, "§1PoorSpace――"+w+"空间"+id+"放弃");
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§c点击确认放弃该空间（无经验返还！）");
		inv.setItem(3, newItem(Material.COBWEB, "§4§l确认放弃", lore));
		lore.clear();
		lore.add("§e点击取消");
		inv.setItem(5, newItem(Material.BARRIER, "§a§l取消", lore));
		
		player.openInventory(inv);
	}
	
	public static void openPermission(Player player, String id, int world, int group) {
		Space space = new Space(id, world);
		String w = world(world);
		
		Inventory inv = Bukkit.getServer().createInventory(null, 54, "§1PoorSpace――"+w+"空间"+id+"权限组"+group);
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7玩家可以拾起地上的物品");
		inv.setItem(0, newItem(Material.DIAMOND, "§e§l拾起物品", lore));
		lore.clear();
		lore.add("§7玩家可以丢弃物品");
		inv.setItem(2, newItem(Material.ROTTEN_FLESH, "§e§l丢弃物品", lore));
		lore.clear();
		lore.add("§7玩家可以放置方块（不包括火把）");
		inv.setItem(4, newItem(Material.BRICK, "§e§l放置方块", lore));
		lore.clear();
		lore.add("§7玩家可以破坏方块（不包括火把）");
		inv.setItem(6, newItem(Material.WOODEN_PICKAXE, "§e§l破坏方块", lore));
		lore.clear();
		lore.add("§7玩家可以触发方块，如耕地、压力板等");
		inv.setItem(8, newItem(Material.OAK_PRESSURE_PLATE, "§e§l触发方块", lore));
		lore.clear();
		lore.add("§7玩家可以使用方块，如箱子、按钮等");
		inv.setItem(18, newItem(Material.CHEST, "§e§l使用方块", lore));
		lore.clear();
		lore.add("§7玩家可以与实体交互，如攻击、交易等");
		inv.setItem(20, newItem(Material.EMERALD, "§e§l实体", lore));
		lore.clear();
		lore.add("§7玩家可以放置和破坏火把、红石火把（非流体中）");
		inv.setItem(22, newItem(Material.TORCH, "§e§l火把", lore));
		int m = 8;
		if (group == 4) {
			lore.clear();
			lore.add("§7防止爆炸对方块产生的破坏");
			inv.setItem(24, newItem(Material.TNT, "§e§l防爆", lore));
			lore.clear();
			lore.add("§7防止火蔓延至此空间");
			inv.setItem(26, newItem(Material.FLINT_AND_STEEL, "§e§l防火", lore));
			m = 10;
		}
		
		char[] pm = space.permission(group);
		
		ItemStack on = new ItemStack(Material.LIME_DYE);
		ItemMeta onmeta = on.getItemMeta();
		lore.clear();
		boolean owned = Space.isOwned(id, world);
		if (owned && space.owner().equals(player.getName()))
			lore.add("§e点击关闭该项");
		onmeta.setDisplayName("§a§l当前状态：开启");
		onmeta.setLore(lore);
		on.setItemMeta(onmeta);
		
		
		ItemStack off = new ItemStack(Material.GRAY_DYE);
		ItemMeta offmeta = off.getItemMeta();
		lore.clear();
		if (owned && space.owner().equals(player.getName()))
			lore.add("§e点击开启该项");
		offmeta.setDisplayName("§4§l当前状态：关闭");
		offmeta.setLore(lore);
		off.setItemMeta(offmeta);
		
		ItemStack item;
		for(int j = 0; j < m; j++) {
			
			if (pm[j] == '0')
				item = off;
			else
				item = on;
			
			if (j < 5)
				inv.setItem(9+j*2, item);
			else
				inv.setItem(17+j*2, item);
			
		}
		
		lore.clear();
		lore.add("§e§l点击返回空间");
		inv.setItem(49, newItem(Material.BARRIER, "§a§l返回", lore));
		
		player.openInventory(inv);
	}
	
	public static void openNearbyChunks(Player player, String id, int world) {
		String w = world(world);
		int split = id.indexOf(".");
		int x = Integer.parseInt(id.substring(0, split));
		int z = Integer.parseInt(id.substring(split+1));
		Inventory inv = Bukkit.getServer().createInventory(null, 45, "§1PoorSpace――"+w+"区块"+id+"附近");
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				Material material = Material.PAPER;
				ArrayList<String> lore = new ArrayList<String>();
				int max = -1;
				switch (world) {
				case 0:
					max = 5;
					break;
				case 1:
					max = 3;
					break;
				case 2:
				case 3:
				case 4:
					max = 1;
					break;
				}
				String chunkid = (x-2+j)+"."+(z-2+i);
				for (int m = 0; m < max; m++) {
					
					String spaceY = spaceY(m, world);
					String spaceid = chunkid+"."+m;
					String owner = "";
					if (Space.isOwned(spaceid, world)) {
						Space space = new Space(spaceid, world);
						owner = " "+space.owner();
						if (material.equals(Material.PAPER))
							material = Material.MAP;
					}
					lore.add("§7空间"+spaceid+"（"+spaceY+owner+"）");
					
				}
				lore.add("§e点击查看区块");
				inv.setItem(i*9+j, newItem(material, "§a§l区块"+chunkid, lore));
			}
		}
		
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§e点击查看北部区块");
		inv.setItem(16, newItem(Material.ARROW, "§a§l北", lore));
		lore.clear();
		lore.add("§e点击查看西部区块");
		inv.setItem(24, newItem(Material.ARROW, "§a§l西", lore));
		lore.clear();
		lore.add("§e点击查看东部区块");
		inv.setItem(26, newItem(Material.ARROW, "§a§l东", lore));
		lore.clear();
		lore.add("§e点击查看南部区块");
		inv.setItem(34, newItem(Material.ARROW, "§a§l南", lore));
		inv.setItem(25, newItem(Material.COMPASS, "§a§l方位"));
		
		player.openInventory(inv);
	}

	public static void openGroups(Player player) {

		Inventory inv = Bukkit.getServer().createInventory(null, 9, "§1PoorSpace――个人群组");

		SpacePlayer spacePlayer = new SpacePlayer(player.getName());
		List<String> groups = spacePlayer.getGroups();
		int size = groups.size();
		for (int i = 0; i < size; i++) {
			inv.setItem(i, (new SpaceGroup(groups.get(i))).toItem());
		}

		player.openInventory(inv);

	}

	public static void searchGroups(Player player, String s) {

		List<String> groups = new ArrayList<>();
		for (String group : SpaceGroup.getAllGroups()) {
			if (group.contains(s))
				groups.add(group);
		}

		SpaceOpen.searchResults.put(player.getName(), groups);
		SpaceOpen.subSearchGroups(player, 1);

	}

	public static void searchGroups(Player player) {

		SpaceOpen.searchResults.put(player.getName(), Arrays.asList(SpaceGroup.getAllGroups()));
		SpaceOpen.subSearchGroups(player, 1);

	}

	public static void subSearchGroups(Player player, int page) {

		List<String> groups = SpaceOpen.searchResults.get(player.getName());
		int totalpage = (groups.size()-1)/45+1;
		if (totalpage < 1)
			totalpage = 1;
		int imax;
		if (page > totalpage)
			page = totalpage;
		else if (page < 1)
			page = 1;

		Inventory inv = Bukkit.getServer().createInventory(null, 54, "§1PoorSpace――搜索群组 第"+page+"/"+totalpage+"页");
		if (page < totalpage) {
			imax = page*45;
			inv.setItem(53, newItem(Material.ARROW, "§a§l下一页"));
			if (page > 1)
				inv.setItem(45, newItem(Material.ARROW, "§a§l上一页"));
		}
		else {
			imax = groups.size();
			if (page > 1)
				inv.setItem(45, newItem(Material.ARROW, "§a§l上一页"));
		}

		int istart = (page-1)*45+1;
		for (int i = istart; i <= imax; i++) {
			inv.setItem(i-istart, (new SpaceGroup(groups.get(i-1))).toItem());
		}

		player.openInventory(inv);

	}

	public static void openGroup(Player player, String name, int page) {

        SpaceGroup group = new SpaceGroup(name);
        if (group.exists()) {

            SpaceGroup.GroupRole role = group.getRole(player.getName());
            List<String> ops = group.getOps();
            List<String> members = group.getMembers();
            int totalsize = ops.size()+members.size();
            int totalpage = (totalsize+35)/36;
            if (totalpage < 1)
            	totalpage = 1;
            int imax;
            if (page > totalpage)
                page = totalpage;
            else if (page < 1)
                page = 1;

            Inventory inv = Bukkit.getServer().createInventory(null, 54, "§1PoorSpace――群组："+name+" 第"+page+"/"+totalpage+"页");
			if (page < totalpage) {
				imax = page*36;
				inv.setItem(53, newItem(Material.ARROW, "§a§l下一页"));
				if (page > 1)
					inv.setItem(45, newItem(Material.ARROW, "§a§l上一页"));
			}
			else {
				imax = totalsize;
				if (page > 1)
					inv.setItem(45, newItem(Material.ARROW, "§a§l上一页"));
			}

			int istart = (page-1)*36;
            int opsize = ops.size();
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta)item.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();
            for (int i = istart; i <= imax; i++) {
                if (i < opsize) {

                    meta.setDisplayName("§a"+ops.get(i)+"§b[管理员]");
                    meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Pig"));
                    if (role.equals(SpaceGroup.GroupRole.OWNER)) {
                        lore.add("§e点击左键取消其管理，中键设置其为群主，右键将其移出群组");
                        meta.setLore(lore);
                        lore.clear();
                    }
                    else
                        meta.setLore(lore);
                    item.setItemMeta(meta);

                }
                else {

                    meta.setDisplayName("§a"+members.get(i-opsize));
                    meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Chicken"));
                    if (role.equals(SpaceGroup.GroupRole.OWNER) || role.equals(SpaceGroup.GroupRole.OP)) {
                        lore.add("§e点击左键设置其为管理，右键将其移出群组");
                        meta.setLore(lore);
                        lore.clear();
                    }
                    else
                        meta.setLore(lore);
                    item.setItemMeta(meta);

                }
				inv.setItem(i + 9, item);
            }
            meta.setDisplayName("§e群主："+group.getOwner());
            meta.setOwningPlayer(Bukkit.getOfflinePlayer("MHF_Slime"));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(1, item);

            inv.setItem(0, group.toItem());
            inv.setItem(49, newItem(Material.STRUCTURE_VOID, "§a§l返回个人群组界面"));
            switch (role) {
				case OWNER:
					lore.add("§e双击解散群组，无法撤销！");
					inv.setItem(8, newItem(Material.BARRIER, "§4§l解散群组", lore));
					break;
				case OP:
				case MEMBER:
					lore.add("§e双击退出群组，无法撤销！");
					inv.setItem(8, newItem(Material.BARRIER, "§4§l退出群组", lore));
					break;
			}

            player.openInventory(inv);

        }
        else {
        	player.closeInventory();
			player.sendMessage("§7【PoorSpace】该群组不存在！");
		}

	}

	public static void createGroup(Player player, String name) {

		Inventory inv = Bukkit.getServer().createInventory(null, 54, "§1PoorSpace——创建群组"+name+" 选择图标");

		inv.setItem(1, newItem(Material.OAK_DOOR, "§a点击选择图标"));
		inv.setItem(2, newItem(Material.SPRUCE_DOOR, "§a点击选择图标"));
		inv.setItem(3, newItem(Material.BIRCH_DOOR, "§a点击选择图标"));
		inv.setItem(4, newItem(Material.DARK_OAK_DOOR, "§a点击选择图标"));
		inv.setItem(5, newItem(Material.ACACIA_DOOR, "§a点击选择图标"));
		inv.setItem(6, newItem(Material.JUNGLE_DOOR, "§a点击选择图标"));
		inv.setItem(7, newItem(Material.IRON_DOOR, "§a点击选择图标"));

		player.openInventory(inv);

	}
	
	private static String spaceY(int m, int world) {
		String name = "error";
		if (world == 0) {
			switch(m) {
			case 0:
				name = "高度：1-20";
				break;
			case 1:
				name = "高度：21-50";
				break;
			case 2:
				name = "高度：51-100";
				break;
			case 3:
				name = "高度：101-200";
				break;
			case 4:
				name = "高度：201-256";
				break;
			}
		}
		else if (world == 1) {
			switch(m) {
			case 0:
				name = "高度：1-50";
				break;
			case 1:
				name = "高度：51-128";
				break;
			case 2:
				name = "高度：129-256";
				break;
			}
		}
		else {
			name = "全高度";
		}
		return name;
	}
	
	public static String world(int world) {
		String w = "主世界";
		switch(world) {
		case 0:
			w = "主世界";
			break;
		case 1:
			w = "下界";
			break;
		case 2:
			w = "末地";
			break;
		case 3:
			w = "创造界";
			break;
		case 4:
			w = "小游戏界";
			break;
		}
		return w;
	}
	
	private static ItemStack newItem(Material material, String name) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		item.setItemMeta(meta);
		return item;
	}
	
	private static ItemStack newItem(Material material, String name, ArrayList<String> lore) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	private static ItemStack newItem(Material material, String name, ArrayList<String> lore, boolean enchant) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.setLore(lore);
		if (enchant) {
			meta.addEnchant(Enchantment.DURABILITY, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		item.setItemMeta(meta);
		return item;
	}

}
