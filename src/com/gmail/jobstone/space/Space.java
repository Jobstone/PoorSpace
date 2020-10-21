package com.gmail.jobstone.space;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Space {

    int world;
    File file;
    List<String> group1 = new ArrayList<>();
    List<String> group2 = new ArrayList<>();
    List<String> group3 = new ArrayList<>();
    char[] permission1 = {'1', '1', '1', '1', '1', '1', '1', '1'};
    char[] permission2 = {'1', '1', '1', '1', '1', '1', '1', '1'};
    char[] permission3 = {'1', '1', '1', '1', '1', '1', '1', '1'};
    char[] permission4 = {'0', '1', '1', '1', '1', '1', '1', '1', '1', '0'};


    public File file() {
        return file;
    }

    public int world() {
        return world;
    }

    public List<String> group(int i) {
        switch(i) {
            case 1:
                return group1;
            case 2:
                return group2;
            case 3:
                return group3;
        }
        return null;
    }

    public char[] permission(int i) {
        switch(i) {
            case 1:
                return permission1;
            case 2:
                return permission2;
            case 3:
                return permission3;
            case 4:
                return permission4;
        }
        return null;
    }

    public void setGroup(int group, List<String> list) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        switch(group) {
            case 1:
                group1 = list;
                config.set("group1", group1);
                break;
            case 2:
                group2 = list;
                config.set("group2", group2);
                break;
            case 3:
                group3 = list;
                config.set("group3", group3);
                break;
        }
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //返回值：0.失败 1.成功 2.人数已满
    public int addGroup(int group, List<String> names) {
        switch (group) {
            case 1:
            case 2:
            case 3:
                List<String> original = this.group(group);
                int repeat = 0;
                int size = names.size();
                boolean[] exists = new boolean[size];
                for (int i = 0; i < size; ++i)
                    exists[i] = false;
                for (int i = 0; i < size; ++i) {
                    if (original.contains(names.get(i))) {
                        repeat++;
                        exists[i] = true;
                    }
                }
                if (names.size() + original.size() - repeat > 10)
                    return 2;
                for (int i = 0; i < size; i++) {
                    if (!exists[i])
                        original.add(names.get(i));
                }
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("group" + group, original);
                try {
                    config.save(file);
                    return 1;
                } catch (IOException e) {
                    e.printStackTrace();
                    return 0;
                }
            default:
                return 0;
        }
    }


    //返回值：0.失败 1.成功
    public boolean removeGroup(int group, List<String> names) {
        switch (group) {
            case 1:
            case 2:
            case 3:
                List<String> original = this.group(group);
                original.removeAll(names);
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                config.set("group" + group, original);
                try {
                    config.save(file);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            default:
                return false;
        }
    }


    public void setPermission(int i, char[] pm) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                char[] original = this.permission(i);
                if (original.length != pm.length)
                    return;
                for (int j = 0; j < original.length; ++j) {
                    if (pm[j] != '-')
                        original[j] = pm[j];
                }
                config.set("permission" + i, String.valueOf(original));
                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public boolean canExplode() {
        return permission4[8] == '0';
    }

    public boolean canFire() {
        return permission4[9] == '0';
    }



    public static boolean isGroupLegal(String group) {
        switch(group) {
            case "1":
            case "2":
            case "3":
                return true;
            default:
                return false;
        }
    }

    public static int getWorldId(String world) {
        switch(world) {
            case "world":
                return 0;
            case "world_nether":
                return 1;
            case "world_the_end":
                return 2;
            case "creative":
                return 3;
            case "minigame":
                return -1;
            default:
                return -1;
        }
    }

    public static String getWorldName(int world) {
        switch(world) {
            case 0:
                return "world";
            case 1:
                return "world_nether";
            case 2:
                return "world_the_end";
            case 3:
                return "creative";
            case 4:
                return "minigame";
            default:
                return null;
        }
    }

}
