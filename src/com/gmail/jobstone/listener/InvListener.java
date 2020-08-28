package com.gmail.jobstone.listener;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.gmail.jobstone.*;
import com.gmail.jobstone.space.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InvListener implements Listener {
	
	private final PoorSpace plugin;
	
	public InvListener (PoorSpace plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void click(InventoryClickEvent e) {
		String invName = e.getView().getTitle();
		if(e.getInventory().getHolder() == null && invName.startsWith("§1PoorSpace――")) {
			String window = invName.substring(13);
			if (window.equals("个人")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
						
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						if (click.equals("§a§l主世界")) {
							SpaceOpen.openWorld(player, 0, 1);
						}
						else if (click.equals("§a§l下界")) {
							SpaceOpen.openWorld(player, 1, 1);
						}
						else if (click.equals("§a§l末地")) {
							SpaceOpen.openWorld(player, 2, 1);
						}
						else if (click.equals("§a§l创造界")) {
							SpaceOpen.openWorld(player, 3, 1);
						}
						else if (click.equals("§a§l小游戏界")) {
							
						}
						
					}
				}
			}
			
			else if (window.startsWith("个人：主世界")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
						
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						if (click.startsWith("§a§l空间")) {
							String id = click.substring(6);
							SpaceOpen.openSpace(player, id, 0);
						}
						else if (click.equals("§a§l下一页")) {
							int page = Integer.parseInt(window.substring(10, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 0, page+1);
						}
						else if (click.equals("§a§l上一页")) {
							int page = Integer.parseInt(window.substring(10, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 0, page-1);
						}
						else if (click.equals("§a§l返回")) {
							SpaceOpen.openPlayer(player);
						}
						else if (click.equals("§a§l主世界")) {
							Location loc = Bukkit.getWorld("world").getSpawnLocation();
							SpaceOpen.openNearbyChunks(player, loc.getChunk().getX()+"."+loc.getChunk().getZ(), NormalSpace.getWorldId(loc));
						}
						
					}
				}
			}
			
			else if (window.startsWith("个人：下界")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
						
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						if (click.startsWith("§a§l空间")) {
							String id = click.substring(6);
							SpaceOpen.openSpace(player, id, 1);
						}
						else if (click.equals("§a§l下一页")) {
							int page = Integer.parseInt(window.substring(9, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 1, page+1);
						}
						else if (click.equals("§a§l上一页")) {
							int page = Integer.parseInt(window.substring(9, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 1, page-1);
						}
						else if (click.equals("§a§l返回")) {
							SpaceOpen.openPlayer(player);
						}
						else if (click.equals("§a§l下界")) {
							Location loc = Bukkit.getWorld("world_nether").getSpawnLocation();
							SpaceOpen.openNearbyChunks(player, loc.getChunk().getX()+"."+loc.getChunk().getZ(), NormalSpace.getWorldId(loc));
						}
						
					}
				}
			}
			
			else if (window.startsWith("个人：末地")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
						
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						if (click.startsWith("§a§l空间")) {
							String id = click.substring(6);
							SpaceOpen.openSpace(player, id, 2);
						}
						else if (click.equals("§a§l下一页")) {
							int page = Integer.parseInt(window.substring(9, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 2, page+1);
						}
						else if (click.equals("§a§l上一页")) {
							int page = Integer.parseInt(window.substring(9, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 2, page-1);
						}
						else if (click.equals("§a§l返回")) {
							SpaceOpen.openPlayer(player);
						}
						else if (click.equals("§a§l末地")) {
							Location loc = Bukkit.getWorld("world_the_end").getSpawnLocation();
							SpaceOpen.openNearbyChunks(player, loc.getChunk().getX()+"."+loc.getChunk().getZ(), NormalSpace.getWorldId(loc));
						}
						
					}
				}
			}
			
			else if (window.startsWith("个人：创造界")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
						
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						if (click.startsWith("§a§l空间")) {
							String id = click.substring(6);
							SpaceOpen.openSpace(player, id, 3);
						}
						else if (click.equals("§a§l下一页")) {
							int page = Integer.parseInt(window.substring(10, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 3, page+1);
						}
						else if (click.equals("§a§l上一页")) {
							int page = Integer.parseInt(window.substring(10, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 3, page-1);
						}
						else if (click.equals("§a§l返回")) {
							SpaceOpen.openPlayer(player);
						}
						else if (click.equals("§a§l创造界")) {
							Location loc = Bukkit.getWorld("creative").getSpawnLocation();
							SpaceOpen.openNearbyChunks(player, loc.getChunk().getX()+"."+loc.getChunk().getZ(), NormalSpace.getWorldId(loc));
						}
						
					}
				}
			}
			
			else if (window.startsWith("个人：小游戏界")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
						
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						if (click.startsWith("§a§l空间")) {
							String id = click.substring(6);
							SpaceOpen.openSpace(player, id, 4);
						}
						else if (click.equals("§a§l下一页")) {
							int page = Integer.parseInt(window.substring(11, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 4, page+1);
						}
						else if (click.equals("§a§l上一页")) {
							int page = Integer.parseInt(window.substring(11, window.lastIndexOf("/")));
							SpaceOpen.openWorld(player, 4, page-1);
						}
						else if (click.equals("§a§l返回")) {
							SpaceOpen.openPlayer(player);
						}
						else if (click.equals("§a§l小游戏界")) {
							Location loc = Bukkit.getWorld("minigame").getSpawnLocation();
							SpaceOpen.openNearbyChunks(player, loc.getChunk().getX()+"."+loc.getChunk().getZ(), NormalSpace.getWorldId(loc));
						}
						
					}
				}
			}
			
			else if (window.startsWith("主世界空间")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (window.contains("权限组")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							int j = slot(e.getRawSlot());
							String id = window.substring(5, window.indexOf("权限组"));
							int group = Integer.parseInt(window.substring(window.length()-1));
							permissionClick(player, id, j, 0, group);
							
						}
					}
					else if (window.contains("购买")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(5, window.indexOf("购买"));
							buyClick(player, id, click, 0);
							
						}
					}
					else if (window.contains("放弃")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(5, window.indexOf("放弃"));
							giveupClick(player, id, click, 0);
							
						}
					}
					else {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							spaceClick(player, click, window.substring(5), 0, "主世界");
							
						}
					}
				}
			}
			
			else if (window.startsWith("下界空间")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (window.contains("权限组")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							int j = slot(e.getRawSlot());
							String id = window.substring(4, window.indexOf("权限组"));
							int group = Integer.parseInt(window.substring(window.length()-1));
							permissionClick(player, id, j, 1, group);
							
						}
					}
					else  if (window.contains("购买")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(4, window.indexOf("购买"));
							buyClick(player, id, click, 1);
							
						}
					}
					else if (window.contains("放弃")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(4, window.indexOf("放弃"));
							giveupClick(player, id, click, 1);
							
						}
					}
					else {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							spaceClick(player, click, window.substring(4), 1, "下界");
							
						}
					}
				}
			}
			
			else if (window.startsWith("末地空间")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (window.contains("权限组")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							int j = slot(e.getRawSlot());
							String id = window.substring(4, window.indexOf("权限组"));
							int group = Integer.parseInt(window.substring(window.length()-1));
							permissionClick(player, id, j, 2, group);
							
						}
					}
					else  if (window.contains("购买")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(4, window.indexOf("购买"));
							buyClick(player, id, click, 2);
							
						}
					}
					else if (window.contains("放弃")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(4, window.indexOf("放弃"));
							giveupClick(player, id, click, 2);
							
						}
					}
					else {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							spaceClick(player, click, window.substring(4), 2, "末地");
							
						}
					}
				}
			}
			
			else if (window.startsWith("创造界空间")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (window.contains("权限组")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							int j = slot(e.getRawSlot());
							String id = window.substring(5, window.indexOf("权限组"));
							int group = Integer.parseInt(window.substring(window.length()-1));
							permissionClick(player, id, j, 3, group);
							
						}
					}
					else  if (window.contains("购买")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(5, window.indexOf("购买"));
							buyClick(player, id, click, 3);
							
						}
					}
					else if (window.contains("放弃")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(5, window.indexOf("放弃"));
							giveupClick(player, id, click, 3);
							
						}
					}
					else {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							spaceClick(player, click, window.substring(5), 3, "创造界");
							
						}
					}
				}
			}
			
			else if (window.startsWith("小游戏界空间")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (window.contains("权限组")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							int j = slot(e.getRawSlot());
							String id = window.substring(6, window.indexOf("权限组"));
							int group = Integer.parseInt(window.substring(window.length()-1));
							permissionClick(player, id, j, 4, group);
							
						}
					}
					else  if (window.contains("购买")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(6, window.indexOf("购买"));
							buyClick(player, id, click, 4);
							
						}
					}
					else if (window.contains("放弃")) {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							String id = window.substring(6, window.indexOf("放弃"));
							giveupClick(player, id, click, 4);
							
						}
					}
					else {
						if (e.getClick().equals(ClickType.LEFT)) {
							
							String click = e.getCurrentItem().getItemMeta().getDisplayName();
							spaceClick(player, click, window.substring(6), 4, "小游戏界");
							
						}
					}
				}
			}
			
			else if (window.startsWith("主世界区块")) {

				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
							
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						String id = window.substring(5, window.indexOf("附近"));
						nearbyClick(player, click, id, 0);
						
					}
				}
			}
			
			else if (window.startsWith("下界区块")) {

				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
							
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						String id = window.substring(4, window.indexOf("附近"));
						nearbyClick(player, click, id, 1);
						
					}
				}
			}
			
			else if (window.startsWith("末地区块")) {

				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
							
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						String id = window.substring(4, window.indexOf("附近"));
						nearbyClick(player, click, id, 2);
						
					}
				}
			}
			
			else if (window.startsWith("创造界区块")) {

				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
							
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						String id = window.substring(5, window.indexOf("附近"));
						nearbyClick(player, click, id, 3);
						
					}
				}

			}
			
			else if (window.startsWith("小游戏界区块")) {

				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {
					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {
							
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						String id = window.substring(6, window.indexOf("附近"));
						nearbyClick(player, click, id, 4);
						
					}
				}

			}

			else if (window.equals("个人群组")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {

					Player player = (Player) e.getWhoClicked();
					if (e.getClick().equals(ClickType.LEFT)) {

						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						String name = click.substring(4);
						SpaceOpen.openGroup(player, name, 1);

					}

				}
			}

			else if (window.startsWith("群组")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {

					Player player = (Player) e.getWhoClicked();
					SpaceGroup group = new SpaceGroup(window.substring(3, window.indexOf(' ')));
					if (group.exists()) {
						SpaceGroup.GroupRole role = group.getRole(player.getName());
						Material m = e.getCurrentItem().getType();
						switch (m) {
							case BARRIER:
								if (e.getClick().equals(ClickType.DOUBLE_CLICK)) {
									switch (role) {
										case OP:
										case MEMBER:
											group.removeOne(player.getName());
											player.closeInventory();
											break;
										case OWNER:
											group.remove();
											player.closeInventory();
									}
								}
								break;
							case ARROW:
								if (e.getRawSlot() == 45) {
									if (e.getClick().equals(ClickType.LEFT)) {
										int page = Integer.parseInt(window.substring(window.indexOf('第') + 1, window.lastIndexOf('/')));
										SpaceOpen.openGroup(player, group.getName(), page-1);
									}
								}
								else if (e.getRawSlot() == 53) {
									if (e.getClick().equals(ClickType.LEFT)) {
										int page = Integer.parseInt(window.substring(window.indexOf('第') + 1, window.lastIndexOf('/')));
										SpaceOpen.openGroup(player, group.getName(), page+1);
									}
								}
								break;
							case STRUCTURE_VOID:
								if (e.getClick().equals(ClickType.LEFT))
									SpaceOpen.openGroups(player);
								break;
							case PLAYER_HEAD:
								if (e.getRawSlot() != 1) {
									String name = e.getCurrentItem().getItemMeta().getDisplayName();
									name = name.substring(2);
									if (name.indexOf('§') != -1)
										name = name.substring(0, name.indexOf('§'));
									SpaceGroup.GroupRole clickRole = group.getRole(name);
									switch (clickRole) {
										case OP:
											if (role.equals(SpaceGroup.GroupRole.OWNER)) {

												int page = Integer.parseInt(window.substring(window.indexOf('第') + 1, window.lastIndexOf('/')));
												switch (e.getClick()) {
													case LEFT:
														group.deOp(name);
														SpaceOpen.openGroup(player, group.getName(), page);
														break;
													case MIDDLE:
														group.setOwner(name);
														SpaceOpen.openGroup(player, group.getName(), page);
														break;
													case RIGHT:
														group.removeOne(name);
														SpaceOpen.openGroup(player, group.getName(), page);
												}

											}
											break;
										case MEMBER:
											if (role.equals(SpaceGroup.GroupRole.OWNER)) {

												int page = Integer.parseInt(window.substring(window.indexOf('第') + 1, window.lastIndexOf('/')));
												switch (e.getClick()) {
													case LEFT:
														group.setOp(name);
														SpaceOpen.openGroup(player, group.getName(), page);
														break;
													case RIGHT:
														group.removeOne(name);
														SpaceOpen.openGroup(player, group.getName(), page);
												}

											}
											else if (role.equals(SpaceGroup.GroupRole.OP)) {

												switch (e.getClick()) {
													case RIGHT:
														group.removeOne(name);
														int page = Integer.parseInt(window.substring(window.indexOf('第') + 1, window.lastIndexOf('/')));
														SpaceOpen.openGroup(player, group.getName(), page);
												}

											}
									}
								}
						}
					}
					else {
						player.sendMessage("§7【PoorSpace】该群组不存在！");
					}

				}
			}

			else if (window.startsWith("搜索群组")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {

					Player player = (Player) e.getWhoClicked();
					int slot = e.getRawSlot();
					if (slot < 45) {
						String click = e.getCurrentItem().getItemMeta().getDisplayName();
						String name = click.substring(4);
						SpaceOpen.openGroup(player, name, 1);
					}
					else if (slot == 45) {
						int page = Integer.parseInt(window.substring(window.indexOf('第') + 1, window.lastIndexOf('/')));
						SpaceOpen.subSearchGroups(player, page-1);
					}
					else if (slot == 53) {
						int page = Integer.parseInt(window.substring(window.indexOf('第') + 1, window.lastIndexOf('/')));
						SpaceOpen.subSearchGroups(player, page+1);
					}

				}
			}

			else if (window.startsWith("创建群组")) {
				e.setCancelled(true);
				if (e.getRawSlot() < e.getInventory().getSize() && e.getCurrentItem()!= null && !e.getCurrentItem().getType().equals(Material.AIR)) {

					String name = window.substring(4, window.indexOf(' '));
					SpaceGroup group = new SpaceGroup(name);
					Player player = (Player) e.getWhoClicked();
					if (group.create(e.getCurrentItem().getType(), player.getName()))
						SpaceOpen.openGroup(player, name, 1);
					else {
						player.closeInventory();
						player.sendMessage("§7【PoorSpace】创建失败：该群组已经存在。");
					}

				}
			}
		}
	}
	
	private void spaceClick(Player player, String click, String id, int world, String w) {
		if (click.startsWith("§a§l空间")) {
			String spaceid = click.substring(6);
			SpaceOpen.openSpace(player, spaceid, world);
		}
		else if (click.equals("§e§l周围区块")) {
			SpaceOpen.openNearbyChunks(player, id.substring(0, id.lastIndexOf(".")), world);
		}
		else if (click.equals("§e§l权限组1")) {
			SpaceOpen.openPermission(player, id, world, 1);
		}
		else if (click.equals("§e§l权限组2")) {
			SpaceOpen.openPermission(player, id, world, 2);
		}
		else if (click.equals("§e§l权限组3")) {
			SpaceOpen.openPermission(player, id, world, 3);
		}
		else if (click.equals("§e§l权限组4")) {
			SpaceOpen.openPermission(player, id, world, 4);
		}
		else if (click.startsWith("§a§l购买空间")) {
			SpaceOpen.openBuy(player, id, world);
		}
		else if (click.startsWith("§4§l放弃空间")) {
			SpaceOpen.openGiveup(player, id, world);
		}
		else if (click.equals("§a§l"+w)) {
			SpaceOpen.openWorld(player, world, 1);
		}
		else if (click.startsWith("§e§l空间")) {
			if (NormalSpace.getWorldId(player.getLocation()) != world)
				player.sendMessage("§7【PoorSpace】您不在此空间所在世界，无法查看！");
			else {
				int limit = NormalSpace.limit.getOrDefault(player.getName(), 0);
				if (limit < 1) {
					NormalSpace.showParticle(player, id, world);
					NormalSpace.limit.put(player.getName(), limit+1);
				}
				else {
					player.sendMessage("§7【PoorSpace】您最多同时查看一个粒子效果！");
				}
			}
				
		}
	}
	
	private void permissionClick(Player player, String id, int j, int world, int group) {
		if (j == 10) {
			SpaceOpen.openSpace(player, id, world);
		}
		else if (j != -1) {
			if (group != 4 && (j == 7 || j == 8))
				return;
			NormalSpace space = new NormalSpace(id, world);
			if (NormalSpace.isOwned(id, world) && space.owner().equals(player.getName())) {
				
				char[] pm = space.permission(group);
				if (pm[j] == '0')
					pm[j] = '1';
				else
					pm[j] = '0';
				space.setPermission(group, pm);
				SpaceManager manager = SpaceManager.getSpaceManager(world);
				manager.update(id, space);
				SpaceOpen.openPermission(player, id, world, group);
				
			}
		}
	}
	
	private void buyClick(Player player, String id, String click, int world) {
		if (click.equals("§a§l确认购买")) {

		    NormalSpace space = new NormalSpace(id, world);
		    if (space.owner() != null) {
                player.sendMessage("§7【PoorSpace】该空间已被其他人购买！");
                SpaceOpen.openSpace(player, id, world);
                return;
            }

			int totalexp = 0;
			int explevel = player.getLevel();
			int level2 = (int)Math.pow(explevel, 2);
			  
			if (explevel <= 16)
				totalexp = level2 + 6*explevel;
			else if (explevel <= 31)
				totalexp = 5*level2/2 - 81*explevel/2 + 360;
			else
				totalexp = 9*level2/2 - 325*explevel/2 + 2220;
			totalexp += player.getExp()*player.getExpToLevel();

			List<String> list = SpacePlayer.getSpaceList(player.getName(), 0);
			int cost = NormalSpace.cost(id, world);
			if (world == 0) {
				if (list.isEmpty())
					cost = 0;
				else if (list.size() < 4)
					cost -= 40;
			}
			
			if (totalexp >= cost) {
				player.setLevel(0);
				player.setExp(0);
				player.giveExp(totalexp-cost);
				space.setOwner(new SpacePlayer(player.getName()));
				player.sendMessage("§7【PoorSpace】空间"+id+"购买成功！");
				SpaceOpen.openSpace(player, id, world);
				
				@SuppressWarnings("deprecation")
				Advancement adv = Bukkit.getAdvancement(new NamespacedKey("poorcraft", "first_space"));
				AdvancementProgress pro = player.getAdvancementProgress(adv);
				pro.awardCriteria("first_space");
			}
			else {
				player.sendMessage("§7【PoorSpace】经验值不足，购买失败！");
				SpaceOpen.openSpace(player, id, world);
			}
			
		}
		else if (click.equals("§4§l取消购买")) {
			SpaceOpen.openSpace(player, id, world);
		}
	}
	
	private void giveupClick(Player player, String id, String click, int world) {
		if (click.equals("§4§l确认放弃")) {
			new NormalSpace(id, world).remove();
			SpaceManager manager = SpaceManager.getSpaceManager(world);
			manager.update(id, new NormalSpace(id, world));
			SpaceOpen.openSpace(player, id, world);
			
			@SuppressWarnings("deprecation")
			Advancement adv = Bukkit.getAdvancement(new NamespacedKey("poorcraft", "giveup_space"));
			AdvancementProgress pro = player.getAdvancementProgress(adv);
			pro.awardCriteria("giveup_space");
			
			if (world == 0) {
				List<String> list = SpacePlayer.getSpaceList(player.getName(), 0);
				if (list.size() == 0)
					return;
			}
			
			File file = new File(PoorSpace.plugin.getDataFolder(), "players/"+player.getName()+"/stats.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(file);
			int giveups = config.getInt("giveups");
			giveups++;
			config.set("giveups", giveups);
			try {
				config.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (giveups >= 20) {
				@SuppressWarnings("deprecation")
				Advancement adv2 = Bukkit.getAdvancement(new NamespacedKey("poorcraft", "giveup_space2"));
				AdvancementProgress pro2 = player.getAdvancementProgress(adv2);
				pro2.awardCriteria("giveup_space2");
			}
			if (giveups >= 500) {
				@SuppressWarnings("deprecation")
				Advancement adv3 = Bukkit.getAdvancement(new NamespacedKey("poorcraft", "giveup_space3"));
				AdvancementProgress pro3 = player.getAdvancementProgress(adv3);
				pro3.awardCriteria("giveup_space3");
			}
		}
		else if (click.equals("§a§l取消")) {
			SpaceOpen.openSpace(player, id, world);
		}
	}
	
	private void nearbyClick(Player player, String click, String id, int world) {
		if (click.startsWith("§a§l区块")) {
			SpaceOpen.openSpace(player, click.substring(6)+".0", world);
		}
		else if (click.equals("§a§l北")) {
			int split = id.indexOf(".");
			int x = Integer.parseInt(id.substring(0, split));
			int z = Integer.parseInt(id.substring(split+1));
			id = x+"."+(z-1);
			SpaceOpen.openNearbyChunks(player, id, world);
		}
		else if (click.equals("§a§l西")) {
			int split = id.indexOf(".");
			int x = Integer.parseInt(id.substring(0, split));
			int z = Integer.parseInt(id.substring(split+1));
			id = (x-1)+"."+z;
			SpaceOpen.openNearbyChunks(player, id, world);
		}
		else if (click.equals("§a§l东")) {
			int split = id.indexOf(".");
			int x = Integer.parseInt(id.substring(0, split));
			int z = Integer.parseInt(id.substring(split+1));
			id = (x+1)+"."+z;
			SpaceOpen.openNearbyChunks(player, id, world);
		}
		else if (click.equals("§a§l南")) {
			int split = id.indexOf(".");
			int x = Integer.parseInt(id.substring(0, split));
			int z = Integer.parseInt(id.substring(split+1));
			id = x+"."+(z+1);
			SpaceOpen.openNearbyChunks(player, id, world);
		}
	}

	private int slot(int click) {
		switch(click) {
		case 9:
			return 0;
		case 11:
			return 1;
		case 13:
			return 2;
		case 15:
			return 3;
		case 17:
			return 4;
		case 27:
			return 5;
		case 29:
			return 6;
		case 31:
			return 7;
		case 33:
			return 8;
		case 49:
			return 10;
		default:
			return -1;
		}
	}

}
