package com.gmail.jobstone.listener;

import com.gmail.jobstone.PoorSpace;
import com.gmail.jobstone.space.NormalSpace;
import com.gmail.jobstone.space.SpaceManager;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class GeneralListener implements Listener {

    public GeneralListener() {
        PoorSpace.plugin.getServer().getPluginManager().registerEvents(this, PoorSpace.plugin);
    }

    /*
    @EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
    public void chunkLoad(ChunkLoadEvent e) {
        String world = e.getWorld().getName();
        SpaceManager manager = SpaceManager.getSpaceManager(world);
        Chunk chunk = e.getChunk();
        int max = Space.getWorldMax(Space.getWorldid(world));
        for (int i = 0; i <= max; i++) {
            manager.load(chunk.getX()+"."+chunk.getZ()+"."+i);
        }
    }
    */

    @EventHandler (priority = EventPriority.HIGH, ignoreCancelled = true)
    public void chunkUnload(ChunkUnloadEvent e) {
        String world = e.getWorld().getName();
        SpaceManager manager = SpaceManager.getSpaceManager(world);
        Chunk chunk = e.getChunk();
        int max = NormalSpace.getWorldMax(NormalSpace.getWorldId(world));
        for (int i = 0; i <= max; i++) {
            manager.unload(chunk.getX()+"."+chunk.getZ()+"."+i);
        }
    }

}
