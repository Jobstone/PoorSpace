package com.gmail.jobstone.space;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import com.gmail.jobstone.PoorSpace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class SpacePlayer extends SpaceOwner {
	
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
			return new HashSet<>();
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

	public OperationResult changeSpacePermissionGroup(OperationType operation, int world, String selector, int groupId, List<String> list) {
		if (world < 0 || world > 3)
			return new OperationResult("该世界不存在！");
		if (groupId < 1 || groupId > 3)
			return new OperationResult("无效的权限组编号！");
		List<Space> spaces = this.resolveSelector(world, selector);
		if (0 == spaces.size())
			return new OperationResult("空间选择器未找到任何已拥有的空间（原因可能为空间选择器不合法）！");
		List<String> spaceList = new ArrayList<>();
		switch (operation) {
			case SET:
				for (Space space : spaces) {
					space.setGroup(groupId, list);
					spaceList.add(space.toString());
				}
				break;
			case ADD:
				for (Space space : spaces) {
					space.addGroup(groupId, list);
					spaceList.add(space.toString());
				}
				break;
			case REMOVE:
				for (Space space : spaces) {
					space.removeGroup(groupId, list);
					spaceList.add(space.toString());
				}
				break;
		}
		return new OperationResult(spaceList);
	}

	public OperationResult changeSpacePermission(OperationType operation, int world, String selector, int groupId, String permission) {
		if (world < 0 || world > 3)
			return new OperationResult("该世界不存在！");
		if (groupId < 1 || groupId > 4)
			return new OperationResult("无效的权限组编号！");
		List<Space> spaces = this.resolveSelector(world, selector);
		if (0 == spaces.size())
			return new OperationResult("空间选择器未找到任何已拥有的空间（原因可能为空间选择器不合法）！");
		if (permission.length() != permissionLength(groupId) || !isPermissionLegal(permission))
			return new OperationResult("无效的权限设置！");
		List<String> spaceList = new ArrayList<>();
		switch (operation) {
			case SET:
				for (Space space : spaces) {
					space.setPermission(groupId, permission.toCharArray());
					spaceList.add(space.toString());
				}
				break;
			default:
				return new OperationResult("无效的操作！");
		}
		return new OperationResult(spaceList);
	}

	private static int permissionLength(int i) {
		switch (i) {
			case 1:
			case 2:
			case 3:
				return 7;
			case 4:
				return 9;
			default:
				return -1;
		}
	}

	private static boolean isPermissionLegal(String string) {
		Pattern pattern = Pattern.compile("^[01-]*$");
		return pattern.matcher(string).matches();
	}

	public List<Space> resolveSelector(int world, String selector) {
		File settings = this.getSettingsFile();
		FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
		if (config.contains("selectors." + selector))
			selector = config.getString("selectors." + selector);

		List<Space> spaces = new ArrayList<>();
		if (selector.contains("+")) {
			String[] subSelectors = selector.split("\\+");
			for (String subSelector : subSelectors)
				this.resolveSingleSelector(world, subSelector, spaces);
		}
		else
			this.resolveSingleSelector(world, selector, spaces);
		return spaces;
	}

	private void resolveSingleSelector(int world, String selector, List<Space> list) {
		switch (selector) {
			case "all": {
				for (String spaceId : this.getSpaceList(world)) {
					NormalSpace space = new NormalSpace(spaceId, world);
					if (!list.contains(space))
						list.add(space);
				}
				break;
			}
			case "new": {
				DefaultSpace space = this.getDefaultSpace(world);
				if (!list.contains(space))
					list.add(space);
				break;
			}
			default: {
				if (selector.contains("~")) {
					String id1 = selector.substring(0, selector.indexOf('~'));
					String id2 = selector.substring(selector.indexOf('~') + 1);
					if (NormalSpace.isSpaceLegal(id1, world) && NormalSpace.isSpaceLegal(id2, world)) {
						int x1 = Integer.parseInt(id1.substring(0, id1.indexOf(".")));
						int z1 = Integer.parseInt(id1.substring(id1.indexOf(".") + 1, id1.lastIndexOf(".")));
						int y1 = Integer.parseInt(id1.substring(id1.lastIndexOf(".") + 1));
						int x2 = Integer.parseInt(id2.substring(0, id2.indexOf(".")));
						int z2 = Integer.parseInt(id2.substring(id2.indexOf(".") + 1, id2.lastIndexOf(".")));
						int y2 = Integer.parseInt(id2.substring(id2.lastIndexOf(".") + 1));

						int t;
						if (x1 > x2) {
							t = x1;
							x1 = x2;
							x2 = t;
						}
						if (y1 > y2) {
							t = y1;
							y1 = y2;
							y2 = t;
						}
						if (z1 > z2) {
							t = z1;
							z1 = z2;
							z2 = t;
						}
						for (String id : this.getSpaceList(world)) {
							int x = Integer.parseInt(id.substring(0, id.indexOf(".")));
							int z = Integer.parseInt(id.substring(id.indexOf(".")+1, id.lastIndexOf(".")));
							int y = Integer.parseInt(id.substring(id.lastIndexOf(".")+1));
							NormalSpace space = new NormalSpace(id, world);
							if (x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2 && !list.contains(space))
								list.add(space);
						}
					}
				}
				else if (NormalSpace.isSpaceLegal(selector, world)){
					NormalSpace space = new NormalSpace(selector, world);
					if (space.owner() != null && space.owner().equals(this.name) && !list.contains(space))
						list.add(space);
				}
			}
		}
	}

	public static String preProcessSelector(Player player, int world, String selector) {
		if (selector.contains("+")) {
			String[] subSelectors = selector.split("\\+");
			for (int i = 0; i < subSelectors.length; ++i) {
				if (subSelectors[i].equals("now")) {
					subSelectors[i] = NormalSpace.getSpaceId(player.getLocation());
				}
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < subSelectors.length; ++i) {
				if (i != 0)
					sb.append('+');
				sb.append(subSelectors[i]);
			}
			return sb.toString();
		}
		else
			return selector.equals("now") ? NormalSpace.getSpaceId(player.getLocation()) : selector;
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
		File file = getSettingsFile();
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		List<String> groups = config.getStringList("groups");
		groups.add(group);
		config.set("groups", groups);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void quitGroup(String group) {
		File file = getSettingsFile();
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		List<String> groups = config.getStringList("groups");
		groups.remove(group);
		config.set("groups", groups);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean exists() {
		return this.folder.exists();
	}

	public void createFiles() {
		this.folder.mkdirs();
		List<String> list = new ArrayList<>();
		File file0 = new File(this.folder, "world.yml");
		FileConfiguration config0 = YamlConfiguration.loadConfiguration(file0);
		config0.set("list", list);
		File file1 = new File(this.folder, "world_nether.yml");
		File file2 = new File(this.folder, "world_the_end.yml");
		File file3 = new File(this.folder, "creative.yml");
		File file4 = new File(this.folder, "minigame.yml");

		File file5 = new File(this.folder, "default_world.yml");
		FileConfiguration config5 = YamlConfiguration.loadConfiguration(file5);
		config5.set("group1", list);
		config5.set("group2", list);
		config5.set("group3", list);
		config5.set("permission1", "1111111");
		config5.set("permission2", "1111111");
		config5.set("permission3", "1111111");
		config5.set("permission4", "000001111");
		File file6 = new File(this.folder, "default_world_nether.yml");
		File file7 = new File(this.folder, "default_world_the_end.yml");
		File file8 = new File(this.folder, "default_creative.yml");
		File file9 = new File(this.folder, "default_minigame.yml");

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
	public File getWorldFile(int world) {
		return SpacePlayer.getWorldFile(this.name, world);
	}

	@Override
	public File getDefaultWorldFile(int world) {
		return SpacePlayer.getDefaultWorldFile(this.name, world);
	}

	public DefaultSpace getDefaultSpace(int world) {
		return new DefaultSpace(this.name, world);
	}

	@Override
	public OwnerType getType() {
		return OwnerType.PLAYER;
	}



	public static File getWorldFile(String player, int world) {
		return new File(PoorSpace.plugin.getDataFolder(), "players/"+player+"/"+ Space.getWorldName(world)+".yml");
	}

	public static File getDefaultWorldFile(String player, int world) {
		return new File(PoorSpace.plugin.getDataFolder(), "players/"+player+"/default_"+ Space.getWorldName(world)+".yml");
	}

	public static List<String> getSpaceList(String player, int world) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(SpacePlayer.getWorldFile(player, world));
		return config.getStringList("list");
	}

}
