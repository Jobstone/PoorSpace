package com.gmail.jobstone.space;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class SpaceOwner {

    protected String name;
    protected File folder;

    public String getName() {
        return this.name;
    }

    public File getFolder() {
        return folder;
    }

    public File getWorldFile(int world) {
        return new File(this.folder, Space.getWorldName(world) + ".yml");
    }

    public File getDefaultWorldFile(int world) {
        return new File(this.folder, "default_" + Space.getWorldName(world) + ".yml");
    }

    public List<String> getSpaces(int world) {
        return YamlConfiguration.loadConfiguration(getWorldFile(world)).getStringList("list");
    }

    public boolean addSpace(int world, String id) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(getWorldFile(world));
        List<String> spaces = config.getStringList("list");
        if (spaces.contains(id))
            return false;
        else {
            spaces.add(id);
            config.set("list", spaces);
            try {
                config.save(getWorldFile(world));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public boolean removeSpace(int world, String id) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(getWorldFile(world));
        List<String> spaces = config.getStringList("list");
        if (spaces.contains(id)) {
            spaces.remove(id);
            config.set("list", spaces);
            try {
                config.save(getWorldFile(world));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else
            return false;
    }

    public abstract OwnerType getType();

    public enum OwnerType {
        PLAYER
    }

}
