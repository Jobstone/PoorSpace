package com.gmail.jobstone;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class OpExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ps-op")) {
            if (args.length == 1 && "spacescount".equals(args[0])) {
                int world = SpaceManager.getSpaceManager("world").getLoadedSpacesSize();
                int world_nether = SpaceManager.getSpaceManager("world_nether").getLoadedSpacesSize();
                int world_the_end = SpaceManager.getSpaceManager("world_the_end").getLoadedSpacesSize();
                int creative = SpaceManager.getSpaceManager("creative").getLoadedSpacesSize();
                sender.sendMessage("§7【PoorSpace】当前空间加载情况如下：\n§7 - 主世界: "
                        +world+"\n§7 - 下界: "+world_nether+"\n§7 - 末地: "+world_the_end+"\n§7 - 创造界: "+creative);
            }
        }
        return true;
    }

}
