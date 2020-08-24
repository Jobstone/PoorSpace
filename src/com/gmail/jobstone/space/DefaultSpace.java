package com.gmail.jobstone.space;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DefaultSpace extends Space {

    private String player;

    public DefaultSpace(String player, int world) {
        this.player = player;
        this.world = world;
        this.file = SpacePlayer.getDefaultWorldFile(player, world);
        if (file.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            group1 = config.getStringList("group1");
            group2 = config.getStringList("group2");
            group3 = config.getStringList("group3");
            permission1 = config.getString("permission1").toCharArray();
            permission2 = config.getString("permission2").toCharArray();
            permission3 = config.getString("permission3").toCharArray();
            permission4 = config.getString("permission4").toCharArray();
        }
        else
            permission4 = new char[]{'0', '0', '0', '0', '0', '1', '1', '1', '1'};
    }

    public String player() {
        return this.player;
    }

    @Override
    public String toString() {
        return "默认空间";
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DefaultSpace) {
            DefaultSpace space = (DefaultSpace)o;
            return space.world() == this.world && space.player().equals(this.player);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + this.world;
        result = 31 * result + this.player().hashCode();
        return result;
    }

}
