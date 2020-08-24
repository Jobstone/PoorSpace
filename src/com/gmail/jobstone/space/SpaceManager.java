package com.gmail.jobstone.space;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class SpaceManager {

    private static Map<String, SpaceManager> spaceWorlds = new HashMap<>();

    private final int world;

    public SpaceManager(String world) {
        this.world = NormalSpace.getWorldId(world);
    }

    private Map<String, NormalSpace> loadedSpaces = new HashMap<>();

    public boolean load(String id) {
        if (isLoaded(id))
            return false;
        loadedSpaces.put(id, new NormalSpace(id, world));
        return true;
    }

    public void update(String id, NormalSpace space) {
        loadedSpaces.replace(id, space);
    }

    public boolean unload(String id) {
        if (isLoaded(id)) {
            loadedSpaces.remove(id);
            return true;
        }
        return false;
    }

    private boolean isLoaded(String id) {
        return loadedSpaces.containsKey(id);
    }

    public NormalSpace getSpace(String id) {
        if (isLoaded(id)) {
            return loadedSpaces.get(id);
        }
        else {
            NormalSpace space = new NormalSpace(id, this.world);
            loadedSpaces.put(id, space);
            return space;
        }
    }

    public int getLoadedSpacesSize() {
        return loadedSpaces.size();
    }

    public static void initialize() {
        SpaceManager.spaceWorlds.put("world", new SpaceManager("world"));
        SpaceManager.spaceWorlds.put("world_nether", new SpaceManager("world_nether"));
        SpaceManager.spaceWorlds.put("world_the_end", new SpaceManager("world_the_end"));
        SpaceManager.spaceWorlds.put("creative", new SpaceManager("creative"));
    }

    public static SpaceManager getSpaceManager(String world) {
        return SpaceManager.spaceWorlds.get(world);
    }

    public static SpaceManager getSpaceManager(int world) {
        switch(world) {
            case 0:
                return SpaceManager.spaceWorlds.get("world");
            case 1:
                return SpaceManager.spaceWorlds.get("world_nether");
            case 2:
                return SpaceManager.spaceWorlds.get("world_the_end");
            case 3:
                return SpaceManager.spaceWorlds.get("creative");
            default:
                return null;
        }
    }

    public static NormalSpace getSpace(Location loc) {
        return SpaceManager.getSpaceManager(loc.getWorld().getName()).getSpace(NormalSpace.getSpaceId(loc));
    }

}
