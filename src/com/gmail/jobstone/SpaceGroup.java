package com.gmail.jobstone;

import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SpaceGroup {

    private String name;
    private File folder;

    public SpaceGroup(String name) {
        this.name = name;
        this.folder = new File(PoorSpace.plugin.getDataFolder(), "groups/"+name);
    }

    public boolean exists() {
        return this.folder.exists();
    }


    public String getName() {
        return this.name;
    }


    public boolean contains(String player) {
        if (this.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.folder, "data.yml"));
            return config.getString("owner").equals(player) || config.getStringList("ops").contains(player) || config.getStringList("members").contains(player);
        }
        else
            return false;
    }


    public List<String> getMembers() {
        if (this.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.folder, "data.yml"));
            return config.getStringList("members");
        }
        else
            return null;
    }


    public void addMembers(Set<String> names) {
        new Thread(() -> {
            File file = new File(this.folder, "data.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            String owner = getOwner();
            List<String> ops = config.getStringList("ops");
            List<String> members = config.getStringList("members");
            for (String name : names) {
                if (!(owner.equals(name) || ops.contains(name) || members.contains(name))) {
                    members.add(name);
                    SpacePlayer player = new SpacePlayer(name);
                    if (player.exists())
                        player.joinGroup(this.name);
                    else {
                        player.createFiles();
                        player.joinGroup(this.name);
                    }
                }
            }
            config.set("members", members);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void removeMembers(Set<String> names) {
        new Thread(() -> {
            File file = new File(this.folder, "data.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<String> members = config.getStringList("members");
            for (String name : names) {
                if (members.contains(name)) {
                    SpacePlayer player = new SpacePlayer(name);
                    if (player.exists())
                        player.quitGroup(this.name);
                }
            }
            members.removeAll(names);
            config.set("members", members);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public List<String> getOps() {
        if (this.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.folder, "data.yml"));
            return config.getStringList("ops");
        }
        else
            return null;
    }


    public void setOp(String name) {
        new Thread(() -> {
            File file = new File(this.folder, "data.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<String> ops = config.getStringList("ops");
            List<String> members = config.getStringList("members");
            if (members.remove(name)) {
                config.set("members", members);
                config.set("ops", ops.add(name));
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void deOp(String name) {
        new Thread(() -> {
            File file = new File(this.folder, "data.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<String> ops = config.getStringList("ops");
            List<String> members = config.getStringList("members");
            if (ops.remove(name)) {
                config.set("ops", ops);
                config.set("members", members.add(name));
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void removeOne(String name) {
        new Thread(() -> {
            File file = new File(this.folder, "data.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<String> ops = config.getStringList("ops");
            if (ops.contains(name)) {
                config.set("ops", ops.remove(name));
                SpacePlayer player = new SpacePlayer(name);
                player.quitGroup(this.name);
            }
            else {
                List<String> members = config.getStringList("members");
                if (members.contains(name)) {
                    config.set("members", members.remove(name));
                    SpacePlayer player = new SpacePlayer(name);
                    player.quitGroup(this.name);
                }
            }
        }).start();
    }


    public void removeAll(Set<String> names) {
        new Thread(() -> {
            File file = new File(this.folder, "data.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<String> ops = config.getStringList("ops");
            List<String> members = config.getStringList("members");
            for (String name : names) {
                if (members.contains(name)) {
                    SpacePlayer player = new SpacePlayer(name);
                    if (player.exists())
                        player.quitGroup(this.name);
                }
                if (ops.contains(name)) {
                    SpacePlayer player = new SpacePlayer(name);
                    if (player.exists())
                        player.quitGroup(this.name);
                }
            }
            members.removeAll(names);
            ops.removeAll(names);
            config.set("members", members);
            config.set("ops", ops);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public String getOwner() {
        if (this.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.folder, "data.yml"));
            return config.getString("owner");
        }
        else
            return null;
    }


    public void setOwner(String name) {
        new Thread(() -> {
            File file = new File(this.folder, "data.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            List<String> ops = config.getStringList("ops");
            if (ops.remove(name)) {
                config.set("ops", ops.add(getOwner()));
                config.set("owner", name);
            }
        }).start();
    }


    public GroupRole getRole(String name) {
        if (this.getOwner().equals(name))
            return GroupRole.OWNER;
        else if (this.getOps().contains(name))
            return GroupRole.OP;
        else if (this.getMembers().contains(name))
            return GroupRole.MEMBER;
        else
            return GroupRole.NON;
    }


    public boolean create(Material material, String owner) {
        if (this.exists())
            return false;
        this.folder.mkdirs();

        File file = new File(this.folder, "data.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set("material", material.name());
        config.set("owner", owner);
        config.set("ops", new ArrayList<String>());
        config.set("members", new ArrayList<String>());
        try {
            config.save(file);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void remove() {
        new Thread(() -> {
            for (String name : getMembers()) {
                SpacePlayer player = new SpacePlayer(name);
                player.quitGroup(this.name);
            }
            for (String name : getOps()) {
                SpacePlayer player = new SpacePlayer(name);
                player.quitGroup(this.name);
            }
            SpacePlayer player = new SpacePlayer(getOwner());
            player.quitGroup(this.name);
            this.folder.delete();
        }).start();
    }


    public ItemStack getDoor() {

        if (!this.exists())
            return null;
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.folder, "data.yml"));
        ItemStack item = new ItemStack(Material.valueOf(config.getString("material")));
        return item;

    }


    public ItemStack toItem() {

        if (!this.exists())
            return null;
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(this.folder, "data.yml"));
        ItemStack item = new ItemStack(Material.valueOf(config.getString("material")));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§e§l"+this.name);
        List<String> lore = new ArrayList<>();
        lore.add("§a群主："+config.getString("owner"));
        List<String> ops = config.getStringList("ops");
        List<String> members = config.getStringList("members");
        int total = ops.size()+members.size();
        if (total == 0)
            lore.add("§a该群组暂无成员");
        else {
            lore.add("§a包括：");
            if (total <= 10) {
                for (String member : ops)
                    lore.add("§a" + member + "§b[管理员]");
                for (String member : members)
                    lore.add("§a" + member);
            }
            else {
                int i = 0;
                for (String member : ops) {
                    if (i == 10)
                        break;
                    lore.add("§a" + member + "§b[管理员]");
                    i++;
                }
                for (String member : members) {
                    if (i == 10)
                        break;
                    lore.add("§a" + member);
                    i++;
                }
                lore.add("§a等一共"+total+"名成员");
            }
        }
        lore.add("§e点击进入该群组界面");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;

    }


    public static String[] getAllGroups() {
        return (new File(PoorSpace.plugin.getDataFolder(), "groups")).list();
    }


    public enum GroupRole {
        OWNER, OP, MEMBER, NON
    }

}
