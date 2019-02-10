package com.gmail.jobstone;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpaceGroup extends SpaceOwner {

    private List<String> players = new ArrayList<>();

    public SpaceGroup(String name) {
        this.name = name;
        this.folder = new File(PoorSpace.plugin.getDataFolder(), "groups/"+name);
    }

    public boolean exists() {
        return this.folder.exists();
    }

    public boolean contains(String player) {
        if (this.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.folder, "data.yml"));
            return config.getStringList("players").contains(player);
        }
        else
            return false;
    }

    public void create() {
        if (this.exists())
            return;
        this.folder.mkdirs();

    }

    @Override
    public OwnerType getType() {
        return OwnerType.GROUP;
    }

}
