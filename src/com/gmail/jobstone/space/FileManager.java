package com.gmail.jobstone.space;

import com.gmail.jobstone.PoorSpace;

import java.io.File;

public class FileManager {

    public static File getPlayerWorldFile(String player, int world) {
        return new File(PoorSpace.plugin.getDataFolder(), "players/"+player+"/"+Space.getWorldName(world)+".yml");
    }

    public static File getSpaceFile(int world, String id) {
        String[] splits = id.split("\\.");
        if (splits.length != 3)
            return null;
        try {
            int x = Integer.parseInt(splits[0]);
            int y = Integer.parseInt(splits[1]);

            int groupX = x >> 5;
            int groupY = y >> 5;
            File folder = new File(PoorSpace.plugin.getDataFolder(),
                    "spaces/" + Space.getWorldName(world) + "/" + groupX + "." + groupY);
            return new File(folder, id + ".yml");
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
