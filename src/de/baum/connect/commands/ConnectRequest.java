package de.baum.connect.commands;

import de.baum.connect.listener.Connect;
import de.baum.connect.listener.ConnectGame;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConnectRequest implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            //Request to play
            if(args.length == 1) {

                Player receiver = player.getServer().getPlayer(args[0]);
                if(receiver == null) {
                    player.sendMessage("§cERROR: That player doesn't exist.");
                    return false;
                }
                player.sendMessage("§aPlease wait until the player accepts.");
                receiver.sendMessage("§aDo you want to play a 4connect game with " + player.getName() + "? Type §6/4connect accept " + player.getName());
                Connect.ConnectPlayers.put(receiver,player);

            }

            //Accept to play
            if(args.length == 2) {
                if(args[0].equals("accept")) {
                    Player player2 = player.getServer().getPlayer(args[1]);

                    if(player2 == null) {
                        player.sendMessage("§cPlayer isn't online!");
                        return false;
                    }

                    if(Connect.ConnectPlayers.get(player).equals(player2)) {

                        ConnectGame event = new ConnectGame(player,player2);
                        Bukkit.getServer().getPluginManager().callEvent(event);

                        player2.sendMessage("§a" + player2.getName() + " has accepted! Starting game...");

                        Connect.PlayingPlayers.put(player2,player);
                        Connect.turn.put(player,0);
                        Connect.ConnectPlayers.remove(player2);

                        Connect.turn.put(player2,1);
                    } else return false;
                }
            } else if(args.length > 3) player.sendMessage("§cERROR: Do /4connect [playername]");

        } else
            System.out.println("Only players can play 4connect.");
        return false;
    }
}
