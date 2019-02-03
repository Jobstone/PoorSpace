package com.gmail.jobstone;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TravelAgent;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.vehicle.VehicleDamageEvent;
import org.bukkit.inventory.EquipmentSlot;

import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;

public class SpaceListener implements Listener {
	
	@SuppressWarnings("unused")
	private final PoorSpace plugin;
	
	public SpaceListener (PoorSpace plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void pick(EntityPickupItemEvent e) {
		if (e.getEntity() instanceof Player) {
			String player = ((Player)e.getEntity()).getName();
			Location loc = e.getItem().getLocation();
			if (!playerpm(player, loc, 0))
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void drop(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getPlayer().getLocation();
		if (!playerpm(player.getName(), loc, 1)) {
			sendActionBarMessage(player, "您没有在这个空间扔物品的权限！");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void place(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getBlockPlaced().getLocation();
		if ((e.getBlock().getType().equals(Material.TORCH) || e.getBlock().getType().equals(Material.WALL_TORCH) || e.getBlock().getType().equals(Material.REDSTONE_TORCH) || e.getBlock().getType().equals(Material.REDSTONE_WALL_TORCH))
				&& !e.getBlockReplacedState().getType().equals(Material.WATER) && !e.getBlockReplacedState().getType().equals(Material.LAVA)) {
			if (!playerpm(player.getName(), loc, 7)) {
				sendActionBarMessage(player, "您没有在该空间放置火把的权限！");
				e.setCancelled(true);
			}
		}
		else {
			if (!playerpm(player.getName(), loc, 2)) {
				sendActionBarMessage(player, "您没有在该空间放置方块的权限！");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void bucketPlace(PlayerBucketEmptyEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation();
		if (!playerpm(player.getName(), loc, 2)) {
			sendActionBarMessage(player, "您没有在这个空间放置方块的权限！");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void bucketFill(PlayerBucketFillEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getBlockClicked().getRelative(e.getBlockFace()).getLocation();
		if (!playerpm(player.getName(), loc, 3)) {
			sendActionBarMessage(player, "您没有破坏该空间方块的权限！");
			e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void breakblock(BlockBreakEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getBlock().getLocation();
		if (e.getBlock().getType().equals(Material.TORCH) || e.getBlock().getType().equals(Material.WALL_TORCH) || e.getBlock().getType().equals(Material.REDSTONE_TORCH) || e.getBlock().getType().equals(Material.REDSTONE_WALL_TORCH)) {
			if (!playerpm(player.getName(), loc, 7)) {
				sendActionBarMessage(player, "您没有破坏该空间火把的权限！");
				e.setCancelled(true);
			}
		}
		else {
			if (!playerpm(player.getName(), loc, 3)) {
				sendActionBarMessage(player, "您没有破坏该空间方块的权限！");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void portalCreate(PlayerPortalEvent e) {
		if (e.getCause().equals(TeleportCause.NETHER_PORTAL)) {
			TravelAgent agent = e.getPortalTravelAgent();
			Location loc = agent.findPortal(e.getTo());
			if (loc == null)
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact1(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.PHYSICAL)) {
			Player player = e.getPlayer();
			Location loc = e.getClickedBlock().getLocation();
			if (!playerpm(player.getName(), loc, 4)) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact2(PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (trigger(e.getClickedBlock()) && (!e.getPlayer().isSneaking() || e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))) {
				Player player = e.getPlayer();
				Location loc = e.getClickedBlock().getLocation();
				if (!playerpm(player.getName(), loc, 5)) {
					sendActionBarMessage(player, "您没有使用该空间方块的权限！");
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact3(PlayerInteractEntityEvent e) {
		if ((e.getHand().equals(EquipmentSlot.HAND) || e.getHand().equals(EquipmentSlot.OFF_HAND)) && !(e.getRightClicked() instanceof Player)) {
			Player player = e.getPlayer();
			Location loc = e.getRightClicked().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				sendActionBarMessage(player, "您没有使用该空间实体的权限！");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact4(EntityDamageByEntityEvent e) {
		Entity entity = e.getEntity();
		if ((entity instanceof Monster || entity instanceof Slime || entity instanceof Flying || entity instanceof Shulker) && e.getEntity().getCustomName() == null)
			return;
		if (entity instanceof Player)
			return;
		Player damager;
		if (e.getDamager() instanceof Player)
			damager = (Player)e.getDamager();
		else if (e.getDamager() instanceof Projectile && ((Projectile)e.getDamager()).getShooter() instanceof Player) {
			damager = (Player) ((Projectile) e.getDamager()).getShooter();
		}
		else 
			return;
		Location loc = e.getEntity().getLocation();
		if (!playerpm(damager.getName(), loc, 6)) {
			sendActionBarMessage(damager, "您没有攻击该空间实体的权限！");
			e.setCancelled(true);
		}
		
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void entity11 (ProjectileHitEvent e) {
		if (e.getEntity() instanceof Arrow && e.getHitEntity() != null && e.getHitEntity() instanceof LivingEntity && !(e.getHitEntity() instanceof Player)) {
			if (e.getEntity().getShooter() instanceof Player) {
				Player player = (Player)e.getEntity().getShooter();
				Location loc = e.getHitEntity().getLocation();
				if (!playerpm(player.getName(), loc, 6)) {
					e.getEntity().setFireTicks(-1);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact5(PlayerInteractAtEntityEvent e) {
		if (e.getRightClicked() instanceof ArmorStand && e.getHand().equals(EquipmentSlot.HAND) && !(e.getRightClicked() instanceof Player)) {
			Player player = e.getPlayer();
			Location loc = e.getRightClicked().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				sendActionBarMessage(player, "您没有使用该空间实体的权限！");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void interact6 (HangingBreakByEntityEvent e) {
		if (e.getRemover() instanceof Player) {
			Player player = (Player)e.getRemover();
			Location loc = e.getEntity().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				sendActionBarMessage(player, "您没有破坏该空间实体的权限！");
				e.setCancelled(true);
			}
		}
		else {
			Location loc = e.getEntity().getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (!space.canExplode())
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void entity7 (VehicleDamageEvent e) {
		if (e.getAttacker() instanceof Player && (e.getVehicle() instanceof Minecart || e.getVehicle() instanceof Boat)) {
			Player player = (Player) e.getAttacker();
			Location loc = e.getVehicle().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				sendActionBarMessage(player, "您没有破坏该空间实体的权限！");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void entity8 (PlayerInteractEvent e) {
		if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) && e.getItem() != null && (boatCartArmorstand(e.getMaterial()) || spawnEggs(e.getMaterial()))) {
			Player player = e.getPlayer();
			Location loc = e.getClickedBlock().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				sendActionBarMessage(player, "您没有在该空间内放置实体的权限！");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void entity9 (HangingPlaceEvent e) {
		Player player = e.getPlayer();
		Location loc = e.getEntity().getLocation();
		if (!playerpm(player.getName(), loc, 6)) {
			sendActionBarMessage(player, "您没有在该空间内放置实体的权限！");
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void entity10 (PlayerFishEvent e) {
		if (e.getState().equals(PlayerFishEvent.State.CAUGHT_ENTITY)) {
			Player player = e.getPlayer();
			Location loc = e.getCaught().getLocation();
			if (!playerpm(player.getName(), loc, 6)) {
				sendActionBarMessage(player, "您没有使用该空间实体的权限！");
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void explode(EntityExplodeEvent e) {
		List<Block> blockListCopy = new ArrayList<Block>();
	    blockListCopy.addAll(e.blockList());
		for (Block block : blockListCopy) {
			Location loc = block.getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (!space.canExplode())
				e.blockList().remove(block);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void mosterDamage(EntityChangeBlockEvent e) {
		if (monsters(e.getEntityType())) {
			Location loc = e.getBlock().getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (!space.canExplode())
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void fire(BlockSpreadEvent e) {
		if (e.getSource().getType().equals(Material.FIRE)) {
			Location loc = e.getBlock().getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (!space.canFire())
				e.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void fire2(BlockBurnEvent e) {
		Location loc = e.getBlock().getLocation();
		Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
		if (!space.canFire())
			e.setCancelled(true);
		
	}
	
	/*
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void fallingblock(EntityChangeBlockEvent e) {
		if (e.getEntityType().equals(EntityType.FALLING_BLOCK)) {
			FallingBlock block = (FallingBlock) e.getEntity();
			Location loc = e.getBlock().getLocation();
			Space space = new Space(Space.getSpaceid(loc), Space.getWorldid(loc));
			if (space.permission(4)[2] == '0') {
				e.setCancelled(true);
				loc.getWorld().dropItem(loc, new ItemStack(block.getBlockData().getMaterial()));
			}
		}
	}
	*/
	
	private boolean boatCartArmorstand(Material material) {
		switch(material) {
		case ACACIA_BOAT:
		case BIRCH_BOAT:
		case DARK_OAK_BOAT:
		case JUNGLE_BOAT:
		case OAK_BOAT:
		case SPRUCE_BOAT:
		case CHEST_MINECART:
		case COMMAND_BLOCK_MINECART:
		case FURNACE_MINECART:
		case HOPPER_MINECART:
		case MINECART:
		case TNT_MINECART:
		case ARMOR_STAND:
			return true;
		default:
			return false;
		}
	}
	
	private boolean spawnEggs(Material material) {
		if (material.name().endsWith("SPAWN_EGG"))
			return true;
		else
			return false;
	}
	
	private boolean monsters(EntityType type) {
		switch(type) {
		case WITHER:
		case ZOMBIE:
		case ZOMBIE_VILLAGER:
		case PIG_ZOMBIE:
		case HUSK:
			return true;
		default:
			return false;
		}
	}
	
	private boolean trigger(Block block) {
		switch(block.getType()) {
		case CHEST:
		case ENDER_CHEST:
		case TRAPPED_CHEST:
		case WHITE_SHULKER_BOX:
		case ORANGE_SHULKER_BOX:
		case MAGENTA_SHULKER_BOX:
		case LIGHT_BLUE_SHULKER_BOX:
		case YELLOW_SHULKER_BOX:
		case LIME_SHULKER_BOX:
		case PINK_SHULKER_BOX:
		case GRAY_SHULKER_BOX:
		case LIGHT_GRAY_SHULKER_BOX:
		case CYAN_SHULKER_BOX:
		case PURPLE_SHULKER_BOX:
		case BLUE_SHULKER_BOX:
		case BROWN_SHULKER_BOX:
		case GREEN_SHULKER_BOX:
		case RED_SHULKER_BOX:
		case BLACK_SHULKER_BOX:
		case SHULKER_BOX:
		case ACACIA_BUTTON:
		case BIRCH_BUTTON:
		case DARK_OAK_BUTTON:
		case JUNGLE_BUTTON:
		case OAK_BUTTON:
		case SPRUCE_BUTTON:
		case STONE_BUTTON:
		case LEVER:
		case COMPARATOR:
		case REPEATER:
		case IRON_DOOR:
		case ACACIA_TRAPDOOR:
		case BIRCH_TRAPDOOR:
		case DARK_OAK_TRAPDOOR:
		case JUNGLE_TRAPDOOR:
		case OAK_TRAPDOOR:
		case SPRUCE_TRAPDOOR:
		case IRON_TRAPDOOR:
		case OAK_DOOR:
		case SPRUCE_DOOR:
		case BIRCH_DOOR:
		case JUNGLE_DOOR:
		case ACACIA_DOOR:
		case DARK_OAK_DOOR:
		case OAK_FENCE_GATE:
		case SPRUCE_FENCE_GATE:
		case BIRCH_FENCE_GATE:
		case JUNGLE_FENCE_GATE:
		case DARK_OAK_FENCE_GATE:
		case ACACIA_FENCE_GATE:
		case CRAFTING_TABLE:
		case FURNACE:
		case HOPPER:
		case BREWING_STAND:
		case ANVIL:
		case CHIPPED_ANVIL:
		case DAMAGED_ANVIL:
		case BEACON:
		case DISPENSER:
		case DROPPER:
		case NOTE_BLOCK:
		case JUKEBOX:
		case ENCHANTING_TABLE:
		case DAYLIGHT_DETECTOR:
		case BLACK_BED:
		case BLUE_BED:
		case BROWN_BED:
		case CYAN_BED:
		case GRAY_BED:
		case GREEN_BED:
		case LIGHT_BLUE_BED:
		case LIGHT_GRAY_BED:
		case LIME_BED:
		case MAGENTA_BED:
		case ORANGE_BED:
		case PINK_BED:
		case PURPLE_BED:
		case RED_BED:
		case WHITE_BED:
		case YELLOW_BED:
		case SIGN:
		case WALL_SIGN:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean playerpm(String player, Location loc, int pmid) {
		
		String spaceid = Space.getSpaceid(loc);
		int worldid = Space.getWorldid(loc);
		Space space = new Space(spaceid, worldid);
		if (Space.isOwned(spaceid, worldid) && space.owner().equals(player))
			return true;
		int group = checkGroup(space, player);
		char[] pm = space.permission(group);
		if (pm[pmid] == '1')
			return true;
		else
			return false;
	}
	
	private static int checkGroup(Space space, String player) {
		for (int i = 1; i < 4; i++) {
			if (space.group(i).contains(player))
				return i;
		}
		return 4;
	}
	
	
	private void sendActionBarMessage(Player player, String message) {
        IChatBaseComponent dummyComponent = ChatSerializer.a("{\"text\":\""+message+"\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(dummyComponent, ChatMessageType.GAME_INFO);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

}
