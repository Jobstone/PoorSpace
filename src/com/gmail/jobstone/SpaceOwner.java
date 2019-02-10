package com.gmail.jobstone;

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
        switch (world) {
            case 0:
                return new File(this.folder, "Overworld.yml");
            case 1:
                return new File(this.folder, "Nether.yml");
            case 2:
                return new File(this.folder, "End.yml");
            case 3:
                return new File(this.folder, "Creative.yml");
            default:
                return null;
        }
    }

    public File getDefaultWorldFile(int world) {
        switch (world) {
            case 0:
                return new File(this.folder, "Default_Overworld.yml");
            case 1:
                return new File(this.folder, "Default_Nether.yml");
            case 2:
                return new File(this.folder, "Default_End.yml");
            case 3:
                return new File(this.folder, "Default_Creative.yml");
            default:
                return null;
        }
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
        PLAYER,
        GROUP
    }

}
