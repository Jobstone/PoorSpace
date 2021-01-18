package com.gmail.jobstone.space;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import com.gmail.jobstone.Pair;
import com.gmail.jobstone.PoorSpace;
import org.bukkit.*;
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

    public OperationResult<List<String>> changeSpacePermissionGroup(OperationType operation, int world, String selector, int groupId, List<String> list) {
        if (world < 0 || world > 3)
            return new OperationResult<>("该世界不存在！");
        if (groupId < 1 || groupId > 3)
            return new OperationResult<>("无效的权限组编号！");
        OperationResult<List<Space>> result = this.resolveSelector(world, selector);
        if (!result.success())
            return new OperationResult<>("空间选择器不合法！");
        List<Space> spaces = result.getContent();
        if (0 == spaces.size())
            return new OperationResult<>("空间选择器未找到任何已拥有的空间！");
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
        return new OperationResult<>(spaceList);
    }

    public OperationResult<List<String>> changeSpacePermission(OperationType operation, int world, String selector, int groupId, String permission) {
        if (world < 0 || world > 3)
            return new OperationResult<>("该世界不存在！");
        if (groupId < 1 || groupId > 4)
            return new OperationResult<>("无效的权限组编号！");
        OperationResult<List<Space>> result = this.resolveSelector(world, selector);
        if (!result.success())
            return new OperationResult<>("空间选择器不合法！");
        List<Space> spaces = result.getContent();
        if (0 == spaces.size())
            return new OperationResult<>("空间选择器未找到任何已拥有的空间！");
        if (permission.length() != permissionLength(groupId) || !isPermissionLegal(permission))
            return new OperationResult<>("无效的权限设置！");
        List<String> spaceList = new ArrayList<>();
        switch (operation) {
            case SET:
                for (Space space : spaces) {
                    space.setPermission(groupId, permission.toCharArray());
                    spaceList.add(space.toString());
                }
                break;
            default:
                return new OperationResult<>("无效的操作！");
        }
        return new OperationResult<>(spaceList);
    }

    public OperationResult<Pair<List<String>, Integer>> copySpaces(int world, String selector, String creativeSpaceId, int amount) {
        if (world < 0 || world > 3)
            return new OperationResult<>("该世界不存在！");
        OperationResult<Pair<List<String>, Position2D>> result = this.resolveCopySelector(world, selector);
        if (!result.success())
            return new OperationResult<>(result.getMessage());
        Pair<List<String>, Position2D> pair = result.getContent();
        List<String> spaces = pair.first;
        if (0 == spaces.size())
            return new OperationResult<>("空间选择器未找到任何已拥有的空间！");

        Position2D basePosition = pair.second;
        Position2D basePositionCreative = NormalSpace.getPosition2D(creativeSpaceId);
//		List<Position2D> originalPositions = spaces.stream()
//				.map(NormalSpace::getPosition2D)
//				.distinct()
//				.collect(Collectors.toList());
        List<Position2D> originalPositions = new ArrayList<>();
        List<Position2D> copyPositions = new ArrayList<>();
        for (String space : spaces) {
            Position2D originalPosition = NormalSpace.getPosition2D(space);
            if (!originalPositions.contains(originalPosition)) {
                originalPositions.add(originalPosition);
                copyPositions.add(originalPosition.diff(basePosition).add(basePositionCreative));
            }
        }
        int size = originalPositions.size();
        int cost = world == 3 ? 0 : size;
        if (cost > amount)
            return new OperationResult<>("主手上的穷王之尘数量不足，共需要 " + cost + " 个。");

        if (!NormalSpace.isSpaceLegal(creativeSpaceId, 3))
            return new OperationResult<>("创造界空间id不合法！");
        World originalWorld = Bukkit.getWorld(Space.getWorldName(world));
        World copyWorld = Bukkit.getWorld("creative");

        for (int i = 0; i < size; ++i) {
            Position2D copyPosition = copyPositions.get(i);
            NormalSpace creativeSpace = new NormalSpace(copyPosition.toPosition3D(0), 3);
            if (creativeSpace.owner() == null || !creativeSpace.owner().equals(this.name))
                return new OperationResult<>("未拥有创造空间 " + creativeSpace.id() + " ！");
            Position2D originalPosition = originalPositions.get(i);
            if (!originalWorld.isChunkGenerated(originalPosition.x, originalPosition.y)
                    || !copyWorld.isChunkGenerated(copyPosition.x, copyPosition.y))
                return new OperationResult<>("涉及区块未生成！");
        }

        List<ChunkSnapshot> originalChunks = new ArrayList<>();
        for (Position2D originalPosition : originalPositions)
            originalChunks.add(originalWorld.getChunkAt(originalPosition.x, originalPosition.y).getChunkSnapshot());

        for (int k = 0; k < size; ++k) {
            Position2D originalPosition = originalPositions.get(k);
            Position2D copyPosition = copyPositions.get(k);
            ChunkSnapshot originalChunk = originalChunks.get(k);
            Chunk copyChunk = copyWorld.getChunkAt(copyPosition.x, copyPosition.y);
            int max = NormalSpace.getWorldMax(world);
            for (int y = 0; y <= max; ++y) {
                int[] yBounds = NormalSpace.getSpaceYBounds(world, y);
                if (spaces.contains(originalPosition.toPosition3D(y).toString())) {
                    for (int i = yBounds[0]; i < yBounds[1]; ++i) {
                        for (int x = 0; x < 16; ++x) {
                            for (int z = 0; z < 16; ++z) {
                                copyChunk.getBlock(x, i, z).setBlockData(originalChunk.getBlockData(x, i, z), false);
                            }
                        }
                    }
                }
                else {
                    for (int i = yBounds[0]; i < yBounds[1]; ++i) {
                        for (int x = 0; x < 16; ++x) {
                            for (int z = 0; z < 16; ++z) {
                                copyChunk.getBlock(x, i, z).setType(Material.AIR, false);
                            }
                        }
                    }
                }
            }
        }

        return new OperationResult<>(new Pair<>(spaces, cost));
    }

    private static int permissionLength(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
                return 8;
            case 4:
                return 10;
            default:
                return -1;
        }
    }

    private static boolean isPermissionLegal(String string) {
        Pattern pattern = Pattern.compile("^[01-]*$");
        return pattern.matcher(string).matches();
    }

    public OperationResult<List<Space>> resolveSelector(int world, String selector) {
        File settings = this.getSettingsFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
        if (config.contains("selectors." + selector))
            selector = config.getString("selectors." + selector);

        List<Space> spaces = new ArrayList<>();
        if (selector.contains("+")) {
            String[] subSelectors = selector.split("\\+");
            for (String subSelector : subSelectors)
                if (!this.resolveSingleSelector(world, subSelector, spaces).success())
                    return new OperationResult<>("Illegal Selector");
        }
        else if (!this.resolveSingleSelector(world, selector, spaces).success())
            return new OperationResult<>("Illegal Selector");
        return new OperationResult<>(spaces);
    }

    private OperationResult resolveSingleSelector(int world, String selector, List<Space> list) {
        switch (selector) {
            case "all": {
                for (String spaceId : this.getSpaceList(world)) {
                    NormalSpace space = new NormalSpace(spaceId, world);
                    if (!list.contains(space))
                        list.add(space);
                }
                return OperationResult.SUCCESS;
            }
            case "new": {
                DefaultSpace space = this.getDefaultSpace(world);
                if (!list.contains(space))
                    list.add(space);
                return OperationResult.SUCCESS;
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
                        return OperationResult.SUCCESS;
                    }
                }
                else if (NormalSpace.isSpaceLegal(selector, world)){
                    NormalSpace space = new NormalSpace(selector, world);
                    if (space.owner() != null && space.owner().equals(this.name) && !list.contains(space))
                        list.add(space);
                    return OperationResult.SUCCESS;
                }
            }
        }
        return new OperationResult("Illegal Selector");
    }

    private OperationResult<Pair<List<String>, Position2D>> resolveCopySelector(int world, String selector) {
        File settings = this.getSettingsFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
        if (config.contains("selectors." + selector))
            selector = config.getString("selectors." + selector);

        List<String> spaces = new ArrayList<>();
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

                if ((x2 - x1 + 1) * (z2 - z1 + 1) > 9)
                    return new OperationResult<>("复制选择器区块数不得大于 9 个！");

                for (String id : this.getSpaceList(world)) {
                    Position3D pos = NormalSpace.getPosition3D(id);
                    if (pos.x >= x1 && pos.x <= x2 && pos.z >= y1 && pos.z <= y2 && pos.y >= z1 && pos.y <= z2)
                        spaces.add(id);
                }
                return new OperationResult<>(new Pair<>(spaces, new Position2D(x1, z1)));
            }
        }
        else if (NormalSpace.isSpaceLegal(selector, world)){
            NormalSpace space = new NormalSpace(selector, world);
            if (space.owner() != null && space.owner().equals(this.name))
                spaces.add(selector);
            return new OperationResult<>(new Pair<>(spaces, NormalSpace.getPosition3D(selector).toPosition2D()));
        }
        return new OperationResult<>("空间选择器不合法！");
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
            return new ArrayList<>();
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
        config5.set("permission1", "11111111");
        config5.set("permission2", "11111111");
        config5.set("permission3", "11111111");
        config5.set("permission4", "0000011011");
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
