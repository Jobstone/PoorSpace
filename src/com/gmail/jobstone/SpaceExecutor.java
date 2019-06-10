package com.gmail.jobstone;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;

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
					SpaceOpen.openSpace(player, Space.getSpaceid(loc), Space.getWorldid(loc));
				}
				else if (args[0].equals("space")) {

					if (args.length == 3) {
						int world;
						if ((world = Space.getWorldid(args[1])) != -1) {

							String id = args[2];
							if (Space.isSpaceLegal(id, world)) {
								SpaceOpen.openSpace(player, id, world);
							} else
								player.sendMessage("§7【PoorSpace】该空间不存在！");

						} else
							player.sendMessage("§7【PoorSpace】该世界不存在！");
					}

				}
				else if (args[0].equals("pmgroup")) {
					
					if (args.length >= 5 && args.length <= 15) {

						if (args[1].equals("set")) {
							new Thread() {

								@Override
								public void run() {
									int world;
									if ((world = Space.getWorldid(args[2])) != -1) {
										List<Space> list = getSpaceList(player, args[3], world);
										if (list == null) {
											File file = new File(PoorSpace.plugin.getDataFolder(), "players/" + player.getName() + "/Default_" + Space.getWorldName(world) + ".yml");
											FileConfiguration config = YamlConfiguration.loadConfiguration(file);
											if (!file.exists()) {
												List<String> empty = new ArrayList<>();
												config.set("group1", empty);
												config.set("group2", empty);
												config.set("group3", empty);
												config.set("permission1", "1111111");
												config.set("permission2", "1111111");
												config.set("permission3", "1111111");
												config.set("permission4", "000001111");
											}
											List<String> group = new ArrayList<>();
											StringBuilder sb = new StringBuilder("");
											for (int i = 5; i < args.length; i++) {
												group.add(args[i]);
												if (i != 5)
													sb.append("\n");
												sb.append("§7 - " + args[i]);
											}
											String msg = sb.toString();

											switch (args[4]) {
												case "1":
													config.set("group1", group);
													if (msg.equals(""))
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组1玩家列表设为空。");
													else
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组1玩家列表设为：\n" + msg);
													break;
												case "2":
													config.set("group2", group);
													if (msg.equals(""))
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组2玩家列表设为空。");
													else
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组2玩家列表设为：\n" + msg);
													break;
												case "3":
													config.set("group3", group);
													if (msg.equals(""))
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组31玩家列表设为空。");
													else
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组3玩家列表设为：\n" + msg);
													break;
												default:
													player.sendMessage("§7【PoorSpace】权限组编号不合法！");
											}
											try {
												config.save(file);
											} catch (IOException e) {
												e.printStackTrace();
											}
										} else if (list.isEmpty()) {
											player.sendMessage("§7【PoorSpace】空间选择器未找到任何已拥有的空间（原因可能为空间选择器不合法）！");
										} else {
											List<String> group = new ArrayList<String>();
											StringBuilder sb = new StringBuilder();
											for (int i = 5; i < args.length; i++) {
												group.add(args[i]);
												if (i != 5)
													sb.append("\n");
												sb.append("§7 - " + args[i]);
											}
											String msg = sb.toString();

											StringBuilder spacesb = new StringBuilder("§7");

											switch (args[4]) {
												case "1":
													for (int i = 0; i < list.size(); i++) {
														if (i != 0)
															spacesb.append(", ");
														Space space = list.get(i);
														space.setGroup(1, group);
														spacesb.append(space.id());
													}
													if (msg.equals("")) {
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组1玩家列表设为空。\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													} else {
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组1玩家列表设为：\\n§7" + msg + "\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													}
													break;
												case "2":
													for (int i = 0; i < list.size(); i++) {
														if (i != 0)
															spacesb.append(", ");
														Space space = list.get(i);
														space.setGroup(2, group);
														spacesb.append(space.id());
													}
													if (msg.equals("")) {
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组2玩家列表设为空。\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													} else {
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组2玩家列表设为：\\n§7" + msg + "\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													}
													break;
												case "3":
													for (int i = 0; i < list.size(); i++) {
														if (i != 0)
															spacesb.append(", ");
														Space space = list.get(i);
														space.setGroup(3, group);
														spacesb.append(space.id());
													}
													if (msg.equals("")) {
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组3玩家列表设为空。\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													} else {
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组3玩家列表设为：\\n§7" + msg + "\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													}
													break;
												default:
													player.sendMessage("§7【PoorSpace】权限组编号不存在！");
											}
										}
									} else
										player.sendMessage("§7【PoorSpace】该世界不存在！");
								}

							}.start();
						}
						else if (args.length != 5) {
							if (args[1].equals("add")) {

								new Thread() {

									@Override
									public void run() {
										int world;
										if ((world = Space.getWorldid(args[2])) != -1) {
											List<Space> list = getSpaceList(player, args[3], world);
											if (list == null) {
												int groupnum = 0;
												switch(args[4]) {
													case "3":
														groupnum++;
													case "2":
														groupnum++;
													case "1":
														groupnum++;

														int previous;
														File file = new File(PoorSpace.plugin.getDataFolder(), "players/" + player.getName() + "/Default_" + Space.getWorldName(world) + ".yml");
														FileConfiguration config = YamlConfiguration.loadConfiguration(file);
														if (!file.exists()) {
															List<String> empty = new ArrayList<>();
															config.set("group1", empty);
															config.set("group2", empty);
															config.set("group3", empty);
															config.set("permission1", "1111111");
															config.set("permission2", "1111111");
															config.set("permission3", "1111111");
															config.set("permission4", "000001111");
															previous = 0;
														}
														else {
															previous = config.getStringList("group"+groupnum).size();
														}

														if (args.length + previous <= 15) {
															List<String> group = config.getStringList("group"+groupnum);
															StringBuilder sb = new StringBuilder("");
															for (int i = 5; i < args.length; i++) {
																group.add(args[i]);
																if (i != 5)
																	sb.append("\n");
																sb.append("§7 - " + args[i]);
															}
															String msg = sb.toString();

															config.set("group"+groupnum, group);
															player.sendMessage("§7【PoorSpace】已将以下玩家/群组添加至"+SpaceOpen.world(world)+"的默认空间设置的权限组"+groupnum+"中：\n" + msg);
															try {
																config.save(file);
															} catch (IOException e) {
																e.printStackTrace();
															}
														}
														else
															player.sendMessage("§7【PoorSpace】操作失败：一个权限组最多设置10个玩家/群组！");

														break;
													default:
														player.sendMessage("§7【PoorSpace】权限组编号不存在！");

												}
											} else if (list.isEmpty()) {
												player.sendMessage("§7【PoorSpace】空间选择器未找到任何已拥有的空间（原因可能为空间选择器不合法）！");
											} else {
												int groupnum = 0;
												switch (args[4]) {
													case "3":
														groupnum++;
													case "2":
														groupnum++;
													case "1":
														groupnum++;

														List<String> group = new ArrayList<>();
														StringBuilder sb = new StringBuilder();
														for (int i = 5; i < args.length; i++) {
															group.add(args[i]);
															if (i != 5)
																sb.append("\n");
															sb.append("§7 - " + args[i]);
														}
														String msg = sb.toString();

														StringBuilder space_success = new StringBuilder("§7");
														StringBuilder space_fail = new StringBuilder("§7");
														boolean i = true, j = true;

														for (Space space : list) {
															int result = space.addGroup(groupnum, group);
															if (result == 1) {
																if (!i)
																	space_success.append(", ");
																else
																	i = false;
																space_success.append(space.id());
															}
															else if (result == 2){
																if (!j)
																	space_fail.append(", ");
																else
																	j = false;
																space_fail.append(space.id());
															}
														}

														if (!i) {
															IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将以下玩家/群组添加至" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + space_success.toString() + "\"}},{\"text\":\"§7的权限组" + groupnum + "中：\\n§7" + msg + "\"}]}");
															PacketPlayOutChat packet = new PacketPlayOutChat(comp);
															((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
														}
														if (!j) {
															IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"\n§7§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + space_fail.toString() + "\"}},{\"text\":\"§7因添加后玩家/群组数即将超过10个，添加失败。\"}]}");
															PacketPlayOutChat packet = new PacketPlayOutChat(comp);
															((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
														}
														break;

													default:
														player.sendMessage("§7【PoorSpace】权限组编号不存在！");
												}
											}
										} else
											player.sendMessage("§7【PoorSpace】该世界不存在！");
									}

								}.start();

							}

							else if (args[1].equals("remove")) {

								new Thread() {

									@Override
									public void run() {
										int world;
										if ((world = Space.getWorldid(args[2])) != -1) {
											List<Space> list = getSpaceList(player, args[3], world);
											if (list == null) {
												int groupnum = 0;
												switch(args[4]) {
													case "3":
														groupnum++;
													case "2":
														groupnum++;
													case "1":
														groupnum++;

														File file = new File(PoorSpace.plugin.getDataFolder(), "players/" + player.getName() + "/Default_" + Space.getWorldName(world) + ".yml");
														FileConfiguration config = YamlConfiguration.loadConfiguration(file);
														if (!file.exists()) {
															List<String> empty = new ArrayList<>();
															config.set("group1", empty);
															config.set("group2", empty);
															config.set("group3", empty);
															config.set("permission1", "1111111");
															config.set("permission2", "1111111");
															config.set("permission3", "1111111");
															config.set("permission4", "000001111");
														}

														List<String> group = config.getStringList("group"+groupnum);
														StringBuilder sb = new StringBuilder("");
														for (int i = 5; i < args.length; i++) {
															group.remove(args[i]);
															if (i != 5)
																sb.append("\n");
															sb.append("§7 - " + args[i]);
														}
														String msg = sb.toString();

														config.set("group"+groupnum, group);
														player.sendMessage("§7【PoorSpace】已将以下玩家/群组从"+SpaceOpen.world(world)+"的默认空间设置的权限组"+groupnum+"中移除：\n" + msg);
														try {
															config.save(file);
														} catch (IOException e) {
															e.printStackTrace();
														}

														break;
													default:
														player.sendMessage("§7【PoorSpace】权限组编号不存在！");

												}
											} else if (list.isEmpty()) {
												player.sendMessage("§7【PoorSpace】空间选择器未找到任何已拥有的空间（原因可能为空间选择器不合法）！");
											} else {
												int groupnum = 0;
												switch (args[4]) {
													case "3":
														groupnum++;
													case "2":
														groupnum++;
													case "1":
														groupnum++;

														List<String> group = new ArrayList<>();
														StringBuilder sb = new StringBuilder();
														for (int i = 5; i < args.length; i++) {
															group.add(args[i]);
															if (i != 5)
																sb.append("\n");
															sb.append("§7 - " + args[i]);
														}
														String msg = sb.toString();

														StringBuilder space_success = new StringBuilder("§7");
														boolean i = true;

														for (Space space : list) {
															space.removeGroup(groupnum, group);
															if (!i)
																space_success.append(", ");
															else
																i = false;
															space_success.append(space.id());
														}

														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将以下玩家/群组从" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + space_success.toString() + "\"}},{\"text\":\"§7的权限组" + groupnum + "中移除：\\n§7" + msg + "\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);

														break;

													default:
														player.sendMessage("§7【PoorSpace】权限组编号不存在！");
												}
											}
										} else
											player.sendMessage("§7【PoorSpace】该世界不存在！");
									}

								}.start();

							}
						}
					}
					
				}
				else if (args[0].equals("group")) {

					if (args.length == 1) {
						SpaceOpen.openGroups(player);
					}

					else if (args[1].equals("search")) {
						if (args.length == 2)
							SpaceOpen.searchGroups(player);
						else if (args.length == 3)
							SpaceOpen.searchGroups(player, args[2]);
					}

					else if (args[1].equals("add")) {
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
											msg += "以下玩家因加入的群组数已满添加失败：\n§7"+fail.toString();
										player.sendMessage(msg);
									}).start();
								}
								else
									player.sendMessage("§7【PoorSpace】你没有权限为该群组添加玩家！");

							}
							else
								player.sendMessage("§7【PoorSpace】未找到名为“"+args[2]+"”的群组。");

						}
					}

					else if (args[1].equals("remove")) {
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
								}
								else if (role.equals(SpaceGroup.GroupRole.OWNER)) {
									new Thread(() -> {
										Set<String> members = new HashSet<>();
										for (int i = 3; i < args.length; i++)
											members.add(args[i]);
										group.removeAll(members);
										player.sendMessage("§7【PoorSpace】成功移除相关玩家！");
									}).start();
								}
								else
									player.sendMessage("§7【PoorSpace】你没有权限移除该权限组的玩家！");

							}
							else
								player.sendMessage("§7【PoorSpace】未找到名为“"+args[2]+"”的群组。");

						}
					}

					else if (args[1].equals("create")) {
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
							}
							else
								player.sendMessage("§7【PoorSpace】您加入的群组数已到达上限！");

						}
					}

				}
				else if (args[0].equals("permission")) {

					if (args.length == 6) {
						if (args[1].equals("set")) {

							new Thread() {
								@Override
								public void run() {
									int world;
									if ((world = Space.getWorldid(args[2])) != -1) {
										List<Space> list = getSpaceList(player, args[3], world);
										if (list == null) {

											File file = new File(PoorSpace.plugin.getDataFolder(), "players/" + player.getName() + "/Default_" + Space.getWorldName(world) + ".yml");
											FileConfiguration config = YamlConfiguration.loadConfiguration(file);
											if (!file.exists()) {
												List<String> empty = new ArrayList<String>();
												config.set("group1", empty);
												config.set("group2", empty);
												config.set("group3", empty);
												config.set("permission1", "1111111");
												config.set("permission2", "1111111");
												config.set("permission3", "1111111");
												config.set("permission4", "000001111");
											}

											switch (args[4]) {
												case "1":
													if (args[5].length() == 7 && isPermissionLegal(args[5])) {
														config.set("permission1", args[5]);
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组1权限设置为：" + args[5]);
													} else
														player.sendMessage("§7【PoorSpace】权限设置不合法！");
													break;
												case "2":
													if (args[5].length() == 7 && isPermissionLegal(args[5])) {
														config.set("permission2", args[5]);
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组2权限设置为：" + args[5]);
													} else
														player.sendMessage("§7【PoorSpace】权限设置不合法！");
													break;
												case "3":
													if (args[5].length() == 7 && isPermissionLegal(args[5])) {
														config.set("permission3", args[5]);
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组3权限设置为：" + args[5]);
													} else
														player.sendMessage("§7【PoorSpace】权限设置不合法！");
													break;
												case "4":
													if (args[5].length() == 9 && isPermissionLegal(args[5])) {
														config.set("permission4", args[5]);
														player.sendMessage("§7【PoorSpace】已将" + SpaceOpen.world(world) + "的默认空间设置中的权限组4权限设置为：" + args[5]);
													} else
														player.sendMessage("§7【PoorSpace】权限设置不合法！");
													break;
												default:
													player.sendMessage("§7【PoorSpace】权限组编号不存在！");
											}

											try {
												config.save(file);
											} catch (IOException e) {
												e.printStackTrace();
											}

										} else if (list.isEmpty()) {
											player.sendMessage("§7【PoorSpace】空间选择器未找到任何已拥有的空间（原因可能为空间选择器不合法）！");
										} else {
											StringBuilder spacesb = new StringBuilder("§7");
											switch (args[4]) {
												case "1":
													if (args[5].length() == 7 && isPermissionLegal(args[5])) {
														for (int i = 0; i < list.size(); i++) {
															if (i != 0)
																spacesb.append(", ");
															Space space = list.get(i);
															space.setPermission(1, args[5].toCharArray());
															spacesb.append(space.id());
														}
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组1权限设置为：" + args[5] + "\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													} else
														player.sendMessage("§7【PoorSpace】权限设置不合法！");
													break;
												case "2":
													if (args[5].length() == 7 && isPermissionLegal(args[5])) {
														for (int i = 0; i < list.size(); i++) {
															if (i != 0)
																spacesb.append(", ");
															Space space = list.get(i);
															space.setPermission(2, args[5].toCharArray());
															spacesb.append(space.id());
														}
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组2权限设置为：" + args[5] + "\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													} else
														player.sendMessage("§7【PoorSpace】权限设置不合法！");
													break;
												case "3":
													if (args[5].length() == 7 && isPermissionLegal(args[5])) {
														for (int i = 0; i < list.size(); i++) {
															if (i != 0)
																spacesb.append(", ");
															Space space = list.get(i);
															space.setPermission(3, args[5].toCharArray());
															spacesb.append(space.id());
														}
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组3权限设置为：" + args[5] + "\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													} else
														player.sendMessage("§7【PoorSpace】权限设置不合法！");
													break;
												case "4":
													if (args[5].length() == 9 && isPermissionLegal(args[5])) {
														for (int i = 0; i < list.size(); i++) {
															if (i != 0)
																spacesb.append(", ");
															Space space = list.get(i);
															space.setPermission(4, args[5].toCharArray());
															spacesb.append(space.id());
														}
														IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"§7【PoorSpace】已将" + SpaceOpen.world(world) + "的\",\"extra\":[{\"text\":\"§n这些空间\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"" + spacesb.toString() + "\"}},{\"text\":\"§7中的权限组4权限设置为：" + args[5] + "\"}]}");
														PacketPlayOutChat packet = new PacketPlayOutChat(comp);
														((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
													} else
														player.sendMessage("§7【PoorSpace】权限设置不合法！");
													break;
												default:
													player.sendMessage("§7【PoorSpace】权限组编号不存在！");
											}
										}
									} else
										player.sendMessage("§7【PoorSpace】该世界不存在！");
								}
							}.start();

						}
					}
					
				}
				else if (args[0].equals("on")) {

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

				}
				else if (args[0].equals("off")) {

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

				}
				else if (args[0].equals("selector")) {
					if (args.length == 4 && args[1].equals("set")) {
						
						SpacePlayer spaceplayer = new SpacePlayer(player.getName());
						Set<String> set = spaceplayer.getSelectorsSet();
						if (set.size() >= 10 && !set.contains(args[2])) {
							player.sendMessage("§7【PoorSpace】选择器设置失败：最多能设置十个自定义选择器名称。");
						}
						else {
							spaceplayer.setSelector(args[2], args[3]);
							player.sendMessage("§7【PoorSpace】成功将选择器\""+args[3]+"\"设置为\""+args[2]+"\"！");
						}
						
					}
					else if (args.length == 3 && args[1].equals("remove")) {
						
						SpacePlayer spaceplayer = new SpacePlayer(player.getName());
						if (spaceplayer.containsSelector(args[2])) {
							spaceplayer.setSelector(args[2], null);
							player.sendMessage("§7【PoorSpace】成功移除选择器\""+args[2]+"\"！");
						}
						else {
							player.sendMessage("§7【PoorSpace】选择器移除失败：名为\""+args[2]+"\"的选择器不存在。");
						}
						
					}
					else if (args.length == 2 && args[1].equals("list")) {
						
						SpacePlayer spaceplayer = new SpacePlayer(player.getName());
						File file = spaceplayer.getSettingsFile();
						FileConfiguration config = YamlConfiguration.loadConfiguration(file);
						ConfigurationSection section = config.getConfigurationSection("selectors");
						Set<String> set = section.getKeys(false);
						if (set.isEmpty()) {
							player.sendMessage("§7【PoorSpace】您未设置任何选择器。");
						}
						else {
							StringBuilder sbd = new StringBuilder("§7【PoorSpace】您已设置的选择器如下：");
							for (String name : set)
								sbd.append("\n - "+name+" : "+section.getString(name));
							player.sendMessage(sbd.toString());
						}
						
					}
					
				}
			}
			
		}
		return true;
	}
	
	
	private boolean isPermissionLegal(String string) {
		Pattern pattern = Pattern.compile("[^01]");  
		return !pattern.matcher(string).matches();  
	}

	private List<Space> getSpaceList(Player player, String string, int world) {
		
		File settings = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/settings.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(settings);
		if (config.contains("selectors."+string))
			string = config.getString("selectors."+string);
		
		List<Space> spacelist = new ArrayList<Space>();
		if (string.contains("+")) {
			String[] selectors = string.split("\\+");
			for (String selector : selectors) {
				if (!selectSpaces(spacelist, player, selector, world, false)) {
					return spacelist;
				}
			}
			return spacelist;
		}
		else {
			if (string.equals("new"))
				return null;
			selectSpaces(spacelist, player, string, world, true);
			return spacelist;
		}
		
	}

	private boolean selectSpaces(List<Space> spacelist,Player player, String string, int world, boolean one) {
		if (string.equals("all")) {
			if (one) {
				for (String id : Space.getSpaceList(player.getName(), world)) {
					Space space = new Space(id, world);
					spacelist.add(space);
				}
			}
			else {
				spacelist.clear();
				for (String id : Space.getSpaceList(player.getName(), world)) {
					Space space = new Space(id, world);
					spacelist.add(space);
				}
				return false;
			}
		}
		else if (string.equals("now")) {
			Location loc = player.getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (space.owner().equals(player.getName()) && !spacelist.contains(space))
				spacelist.add(space);
		}
		else if (string.equals("new")) {
			spacelist.clear();
			return false;
		}
		else if (string.contains("~")) {
			
			String id1 = string.substring(0, string.indexOf('~'));
			String id2 = string.substring(string.indexOf('~')+1, string.length());
			if (Space.isSpaceLegal(id1, world) && Space.isSpaceLegal(id2, world)) {
				int x1 = Integer.parseInt(id1.substring(0, id1.indexOf(".")));
				int z1 = Integer.parseInt(id1.substring(id1.indexOf(".")+1, id1.lastIndexOf(".")));
				int y1 = Integer.parseInt(id1.substring(id1.lastIndexOf(".")+1));
				int x2 = Integer.parseInt(id2.substring(0, id2.indexOf(".")));
				int z2 = Integer.parseInt(id2.substring(id2.indexOf(".")+1, id2.lastIndexOf(".")));
				int y2 = Integer.parseInt(id2.substring(id2.lastIndexOf(".")+1));
				
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
				
				for (String id : Space.getSpaceList(player.getName(), world)) {
					int x = Integer.parseInt(id.substring(0, id.indexOf(".")));
					int z = Integer.parseInt(id.substring(id.indexOf(".")+1, id.lastIndexOf(".")));
					int y = Integer.parseInt(id.substring(id.lastIndexOf(".")+1));
					Space space = new Space(id, world);
					if (x >= x1 && x <= x2 && y >= y1 && y <= y2 && z >= z1 && z <= z2 && !spacelist.contains(space))
						spacelist.add(space);
				}
			}
		}
		else if (Space.isSpaceLegal(string, world)){
			Space space = new Space(string, world);
			if (!spacelist.contains(space))
				spacelist.add(space);
		}
		
		return true;
	}

}
