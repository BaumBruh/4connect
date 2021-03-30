package de.baum.connect.commands;

import de.baum.connect.files.Data;
import de.baum.connect.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;

        if(!player.hasPermission("4connect.admin")) return false;

        try {
            Main.getPlugin().saveConfig();
            Data.reload();
            player.sendMessage("§a[4connect] Data and Config reloaded");
            return true;
        } catch(Exception e) {
            player.sendMessage("§cERROR: Couldnt reload.");
            return false;
        }
    }
}
