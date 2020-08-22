package com.gmail.jobstone.space;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class SpaceManager {

    private static Map<String, SpaceManager> spaceWorlds = new HashMap<>();

    private final int world;

    public SpaceManager(String world) {
        this.world = Space.getWorldid(world);
    }

    private Map<String, Space> loadedSpaces = new HashMap<>();

    public boolean load(String id) {
        if (isLoaded(id))
            return false;
        loadedSpaces.put(id, new Space(id, world));
        return true;
    }

    public void update(String id, Space space) {
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

    public Space getSpace(String id) {
        if (isLoaded(id)) {
            return loadedSpaces.get(id);
        }
        else {
            Space space = new Space(id, this.world);
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

    public static Space getSpace(Location loc) {
        return SpaceManager.getSpaceManager(loc.getWorld().getName()).getSpace(Space.getSpaceid(loc));
    }

}
