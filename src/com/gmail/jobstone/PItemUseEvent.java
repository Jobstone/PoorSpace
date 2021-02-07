package com.gmail.jobstone;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public class PItemUseEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;
    private final Player player;
    private final ItemStack item;

    public PItemUseEvent(Player player, ItemStack item) {
        this.player = player;
        this.item = item;
        this.isCancelled = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
}
