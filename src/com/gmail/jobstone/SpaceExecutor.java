package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gmail.jobstone.space.*;
import javafx.util.Pair;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutChat;
import net.minecraft.server.v1_15_R1.IChatBaseComponent.ChatSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SpaceExecutor implements CommandExecutor {

    @SuppressWarnings("unused")
    private final PoorSpace plugin;

    public SpaceExecutor(PoorSpace plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {

            Player player = (Player)sender;
            if (cmd.getName().equalsIgnoreCase("poorspace")) {
                if (args.length == 0) {
                    Location loc = player.getLocation();
                    SpaceOpen.openSpace(player, NormalSpace.getSpaceId(loc), NormalSpace.getWorldId(loc));
                    return true;
                }
                switch (args[0]) {
                    case "space": {
                        if (args.length == 3) {
                            int world;
                            if ((world = NormalSpace.getWorldId(args[1])) != -1) {
                                String id = args[2];
                                if (NormalSpace.isSpaceLegal(id, world)) {
                                    SpaceOpen.openSpace(player, id, world);
                                } else
                                    player.sendMessage("§7【PoorSpace】该空间不存在！");
                            } else
                                player.sendMessage("§7【PoorSpace】该世界不存在！");
                        }
                        break;
                    }
                    case "pmgroup": {
                        if (args.length < 5 || args.length > 15)
                            return true;
                        OperationType operation;
                        switch (args[1]) {
                            case "set":
                                operation = OperationType.SET;
                                break;
                            case "add":
                                operation = OperationType.ADD;
                                break;
                            case "remove":
                                operation = OperationType.REMOVE;
                                break;
                            default:
                                player.sendMessage("§7【PoorSpace】无效的操作！");
                                return true;
                        }
                        int world = Space.getWorldId(args[2]);
                        int groupId;
                        try {
                            groupId = Integer.parseInt(args[4]);
                        } catch (NumberFormatException e) {
                            player.sendMessage("§7【PoorSpace】无效的权限组编号！");
                            return true;
                        }
                        List<String> list = new ArrayList<>(Arrays.asList(args).subList(5, args.length));
                        new Thread(() -> {
                            SpacePlayer spacePlayer = new SpacePlayer(player.getName());
                            String selector = SpacePlayer.preProcessSelector(player, world, args[3]);
                            OperationResult<List<String>> result = spacePlayer.changeSpacePermissionGroup(operation, world, selector, groupId, list);
                            if (result.success()) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 5; i < args.length; i++)
                                    sb.append("\n§7 - ").append(args[i]);
                                String nameList = sb.toString();
                                nameList = "".equals(nameList) ? "空" : nameList;

                                StringBuilder spaceString = new StringBuilder("§7");
                                List<String> changedSpaces = result.getContent();
                                int size = changedSpaces.size();
                                for (int i = 0; i < size; ++i) {
                                    if (i != 0)
                                        spaceString.append(", ");
                                    spaceString.append(changedSpaces.get(i));
                                }

                                TextComponent component = new TextComponent("§7【PoorSpace】已" + operation.getName() + SpaceOpen.world(world) + "的");
                                TextComponent extra1 = new TextComponent("§n这些空间");
                                extra1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(spaceString.toString())}));
                                TextComponent extra2 = new TextComponent("§7中的权限组" + groupId + "玩家列表： " + nameList);
                                component.addExtra(extra1);
                                component.addExtra(extra2);

                                player.spigot().sendMessage(component);
                            } else
                                player.sendMessage("§7【PoorSpace】" + result.getMessage());
                        }).start();
                        break;
                    }
                    case "group": {
                        if (args.length == 1)
                            SpaceOpen.openGroups(player);
                        else if (args[1].equals("search")) {
                            if (args.length == 2)
                                SpaceOpen.searchGroups(player);
                            else if (args.length == 3)
                                SpaceOpen.searchGroups(player, args[2]);
                        } else if (args[1].equals("add")) {
                            if (args.length >= 4 && args.length <= 13) {

                                SpaceGroup group = new SpaceGroup(args[2]);
                                if (group.exists()) {

                                    SpaceGroup.GroupRole role = group.getRole(player.getName());
                                    if (role.equals(SpaceGroup.GroupRole.OWNER) || role.equals(SpaceGroup.GroupRole.OP)) {
                                        new Thread(() -> {
                                            Set<String> members = new HashSet<>();
                                            for (int i = 3; i < args.length; i++)
                                                members.add(args[i]);
                                            Set<String> fails = group.addMembers(members);
                                            StringBuilder fail = new StringBuilder("");
                                            boolean b = true;
                                            for (String f : fails) {
                                                if (!b)
                                                    fail.append(", ");
                                                else
                                                    b = false;
                                                fail.append(f);
                                            }
                                            String msg = "§7【PoorSpace】已添加相关玩家！";
                                            if (!fails.isEmpty())
                                                msg += "以下玩家因加入的群组数已满添加失败：\n§7" + fail.toString();
                                            player.sendMessage(msg);
                                        }).start();
                                    } else
                                        player.sendMessage("§7【PoorSpace】你没有权限为该群组添加玩家！");

                                } else
                                    player.sendMessage("§7【PoorSpace】未找到名为“" + args[2] + "”的群组。");

                            }
                        } else if (args[1].equals("remove")) {
                            if (args.length >= 4 && args.length <= 13) {

                                SpaceGroup group = new SpaceGroup(args[2]);
                                if (group.exists()) {

                                    SpaceGroup.GroupRole role = group.getRole(player.getName());
                                    if (role.equals(SpaceGroup.GroupRole.OP)) {
                                        new Thread(() -> {
                                            Set<String> members = new HashSet<>();
                                            for (int i = 3; i < args.length; i++)
                                                members.add(args[i]);
                                            group.removeMembers(members);
                                            player.sendMessage("§7【PoorSpace】成功移除相关玩家！");
                                        }).start();
                                    } else if (role.equals(SpaceGroup.GroupRole.OWNER)) {
                                        new Thread(() -> {
                                            Set<String> members = new HashSet<>();
                                            for (int i = 3; i < args.length; i++)
                                                members.add(args[i]);
                                            group.removeAll(members);
                                            player.sendMessage("§7【PoorSpace】成功移除相关玩家！");
                                        }).start();
                                    } else
                                        player.sendMessage("§7【PoorSpace】你没有权限移除该权限组的玩家！");

                                } else
                                    player.sendMessage("§7【PoorSpace】未找到名为“" + args[2] + "”的群组。");

                            }
                        } else if (args[1].equals("create")) {
                            if (args.length == 3) {

                                if ((new SpacePlayer(player.getName())).getGroups().size() < 9) {
                                    Pattern pattern = Pattern.compile("[\\s\\\\/:\\*\\?\\\"<>\\|]");
                                    Matcher matcher = pattern.matcher(args[2]);
                                    if (matcher.find()) {
                                        player.sendMessage("§7【PoorSpace】群组名称中不能包括空白字符及\"\\/:*?\"<>|\"！");
                                    } else {
                                        if (args[2].length() <= 10) {
                                            SpaceGroup group = new SpaceGroup(args[2]);
                                            if (group.exists())
                                                player.sendMessage("§7【PoorSpace】名为" + args[2] + "的群组已经存在！");
                                            else
                                                SpaceOpen.createGroup(player, args[2]);
                                        } else
                                            player.sendMessage("§7【PoorSpace】群组名称不能超过10个字符！");
                                    }
                                } else
                                    player.sendMessage("§7【PoorSpace】您加入的群组数已到达上限！");

                            }
                        }

                        break;
                    }
                    case "permission": {
                        if (args.length == 6) {
                            OperationType operation;
                            switch (args[1]) {
                                case "set":
                                    operation = OperationType.SET;
                                    break;
                                default:
                                    player.sendMessage("§7【PoorSpace】无效的操作！");
                                    return true;
                            }
                            int world = Space.getWorldId(args[2]);
                            int groupId;
                            try {
                                groupId = Integer.parseInt(args[4]);
                            } catch (NumberFormatException e) {
                                player.sendMessage("§7【PoorSpace】无效的权限组编号！");
                                return true;
                            }
                            new Thread(() -> {
                                SpacePlayer spacePlayer = new SpacePlayer(player.getName());
                                String selector = SpacePlayer.preProcessSelector(player, world, args[3]);
                                OperationResult<List<String>> result = spacePlayer.changeSpacePermission(operation, world, selector, groupId, args[5]);
                                if (result.success()) {
                                    StringBuilder spaceString = new StringBuilder("§7");
                                    List<String> changedSpaces = result.getContent();
                                    int size = changedSpaces.size();
                                    for (int i = 0; i < size; ++i) {
                                        if (i != 0)
                                            spaceString.append(", ");
                                        spaceString.append(changedSpaces.get(i));
                                    }

                                    TextComponent component = new TextComponent("§7【PoorSpace】已" + operation.getName() + SpaceOpen.world(world) + "的");
                                    TextComponent extra1 = new TextComponent("§n这些空间");
                                    extra1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(spaceString.toString())}));
                                    TextComponent extra2 = new TextComponent("§7中的权限组" + groupId + "权限： " + args[5]);
                                    component.addExtra(extra1);
                                    component.addExtra(extra2);

                                    player.spigot().sendMessage(component);
                                } else
                                    player.sendMessage("§7【PoorSpace】" + result.getMessage());
                            }).start();
                        }
                        break;
                    }
                    case "on": {
                        if (args.length == 1) {
                            File pFile = new File(PoorSpace.plugin.getDataFolder(), "players/" + player.getName() + "/settings.yml");
                            FileConfiguration config = YamlConfiguration.loadConfiguration(pFile);
                            config.set("spaceinfo", true);
                            try {
                                config.save(pFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case "off": {
                        if (args.length == 1) {
                            File pFile = new File(PoorSpace.plugin.getDataFolder(), "players/" + player.getName() + "/settings.yml");
                            FileConfiguration config = YamlConfiguration.loadConfiguration(pFile);
                            config.set("spaceinfo", false);
                            try {
                                config.save(pFile);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    }
                    case "selector": {
                        if (args.length == 4 && args[1].equals("set")) {
                            SpacePlayer spaceplayer = new SpacePlayer(player.getName());
                            Set<String> set = spaceplayer.getSelectorsSet();
                            if (set.size() >= 10 && !set.contains(args[2])) {
                                player.sendMessage("§7【PoorSpace】选择器设置失败：最多能设置十个自定义选择器名称。");
                            } else {
                                spaceplayer.setSelector(args[2], args[3]);
                                player.sendMessage("§7【PoorSpace】成功将选择器\"" + args[3] + "\"设置为\"" + args[2] + "\"！");
                            }
                        } else if (args.length == 3 && args[1].equals("remove")) {
                            SpacePlayer spaceplayer = new SpacePlayer(player.getName());
                            if (spaceplayer.containsSelector(args[2])) {
                                spaceplayer.setSelector(args[2], null);
                                player.sendMessage("§7【PoorSpace】成功移除选择器\"" + args[2] + "\"！");
                            } else {
                                player.sendMessage("§7【PoorSpace】选择器移除失败：名为\"" + args[2] + "\"的选择器不存在。");
                            }
                        } else if (args.length == 2 && args[1].equals("list")) {
                            SpacePlayer spaceplayer = new SpacePlayer(player.getName());
                            File file = spaceplayer.getSettingsFile();
                            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                            ConfigurationSection section = config.getConfigurationSection("selectors");
                            Set<String> set = section.getKeys(false);
                            if (set.isEmpty()) {
                                player.sendMessage("§7【PoorSpace】您未设置任何选择器。");
                            } else {
                                StringBuilder sbd = new StringBuilder("§7【PoorSpace】您已设置的选择器如下：");
                                for (String name : set)
                                    sbd.append("\n - ").append(name).append(" : ").append(section.getString(name));
                                player.sendMessage(sbd.toString());
                            }
                        }
                        break;
                    }
                    case "copy": {
                        if (args.length == 4) {
                            boolean hasDust = false;
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            if (player.getGameMode().equals(GameMode.SURVIVAL) && itemStack.hasItemMeta()) {
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                if (itemMeta != null && itemMeta.hasLore()) {
                                    List<String> lore = itemMeta.getLore();
                                    if (lore != null && lore.get(0).equals("§7穷王之尘")) {
                                        hasDust = true;
                                    }
                                }
                            }
                            int amount = hasDust ? itemStack.getAmount() : 0;

                            SpacePlayer spaceplayer = new SpacePlayer(player.getName());
                            int world = Space.getWorldId(args[1]);
                            String selector = SpacePlayer.preProcessSelector(player, world, args[2]);
                            String selectorCreative = SpacePlayer.preProcessSelector(player, 3, args[3]);
                            OperationResult<Pair<List<String>, Integer>> result = spaceplayer.copySpaces(world, selector, selectorCreative, amount);
                            if (!result.success())
                                player.sendMessage("§7【PoorSpace】" + result.getMessage());
                            else {
                                Pair<List<String>, Integer> pair = result.getContent();
                                if (hasDust)
                                    itemStack.setAmount(amount - pair.getValue());
                                StringBuilder spaceString = new StringBuilder("§7");
                                List<String> changedSpaces = pair.getKey();
                                int size = changedSpaces.size();
                                for (int i = 0; i < size; ++i) {
                                    if (i != 0)
                                        spaceString.append(", ");
                                    spaceString.append(changedSpaces.get(i));
                                }

                                TextComponent component = new TextComponent("§7【PoorSpace】已复制" + SpaceOpen.world(world) + "的");
                                TextComponent extra1 = new TextComponent("§n这些空间");
                                extra1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(spaceString.toString())}));
                                TextComponent extra2 = new TextComponent("§7至创造界空间 " + args[3] + " 附近。");
                                component.addExtra(extra1);
                                component.addExtra(extra2);

                                player.spigot().sendMessage(component);
                            }
                        }
                        break;
                    }
                }
            }

        }
        return true;
    }

}
