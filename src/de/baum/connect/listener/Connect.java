package de.baum.connect.listener;

import de.baum.connect.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

import java.util.*;


public class Connect implements Listener {

    public static HashMap<Player, Player> ConnectPlayers = new HashMap<>();
    public static HashMap<Player, Player> PlayingPlayers = new HashMap<>();
    //using the value of PlayingPlayers
    public static HashMap<Player, Integer> turn = new HashMap<>();

    //ily sipsi133 on spigotmc for the fix <3
    @EventHandler
    public void invClose(InventoryCloseEvent e) {
        if(PlayingPlayers.containsKey(e.getPlayer())) {
            Player other = PlayingPlayers.get(e.getPlayer());
            Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    e.getPlayer().openInventory(ConnectGame.invhash.get(other));
                }
            }, 1);
        }

        if(PlayingPlayers.containsValue(e.getPlayer())) {
            Main.getPlugin().getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
                @Override
                public void run() {
                    e.getPlayer().openInventory(ConnectGame.invhash.get(e.getPlayer()));
                }
            }, 1);
        }
    }

    @EventHandler
    public void onMove(InventoryClickEvent e) {
        if(PlayingPlayers.containsKey(e.getWhoClicked())) {
            Player valuetrol = PlayingPlayers.get(e.getWhoClicked());
            if(e.getClickedInventory() != ConnectGame.invhash.get(valuetrol)) return;
        }
        if(PlayingPlayers.containsValue(e.getWhoClicked())) {
            if(e.getClickedInventory() != ConnectGame.invhash.get(e.getWhoClicked())) return;
        }
            if(e.getClick().isRightClick() && e.getCurrentItem() == null) {
                if(PlayingPlayers.containsKey(e.getWhoClicked())) {
                    Player other = PlayingPlayers.get(e.getWhoClicked());

                    if(turn.get(other) % 2 == 0) {
                        if (isBlockUnderU(e.getSlot(), e.getInventory())) return;
                        ConnectGame.invhash.get(other).setItem(e.getSlot(), ConnectGame.red);
                        turn.replace(other, turn.get(other) + 1);
                        ConnectGame.invhash.get(other).setItem(44, ConnectGame.turnblue);
                        ConnectGame.invhash.get(other).setItem(36, ConnectGame.grayglass);

                        //Now check if you won
                        if(haveUWon(e.getSlot(),Material.RED_CONCRETE, e.getClickedInventory())) {
                            endGame((Player) e.getWhoClicked(), other, true);
                        }

                        e.setCancelled(true);
                    } else return;
                }
                if(PlayingPlayers.containsValue(e.getWhoClicked())) {
                    if(turn.get(e.getWhoClicked()) % 2 == 1) {
                        if (isBlockUnderU(e.getSlot(), e.getInventory())) return;
                        //but first get the fucking key :'(
                        Player other = getKeyFromValue((Player) e.getWhoClicked());

                        ConnectGame.invhash.get(e.getWhoClicked()).setItem(e.getSlot(), ConnectGame.blue);
                        turn.replace((Player) e.getWhoClicked(), turn.get(e.getWhoClicked()) + 1);
                        ConnectGame.invhash.get(e.getWhoClicked()).setItem(36, ConnectGame.turnred);
                        ConnectGame.invhash.get(e.getWhoClicked()).setItem(44, ConnectGame.grayglass);

                        //Now check if you won
                        if(haveUWon(e.getSlot(),Material.BLUE_CONCRETE, e.getClickedInventory())) {
                            endGame(other, (Player) e.getWhoClicked(), false);
                        }

                        e.setCancelled(true);
                    }
                }

            }
    }

    //cancelling actions
    @EventHandler
    public void normalInvtoInv(InventoryClickEvent e) {
        if(PlayingPlayers.containsKey(e.getWhoClicked()) || PlayingPlayers.containsValue(e.getWhoClicked())) {
            if(e.getClick().isLeftClick()) e.setCancelled(true);
            InventoryAction action = e.getAction();
            switch(action) {
                case MOVE_TO_OTHER_INVENTORY:
                case HOTBAR_SWAP:
                case HOTBAR_MOVE_AND_READD:
                case SWAP_WITH_CURSOR:
                case PLACE_ALL:
                case PLACE_SOME:
                case PLACE_ONE:
                case DROP_ALL_CURSOR:
                case DROP_ALL_SLOT:
                case DROP_ONE_SLOT:
                case DROP_ONE_CURSOR:
                case PICKUP_ALL:
                case PICKUP_HALF:
                case PICKUP_SOME:
                case PICKUP_ONE:
                case CLONE_STACK:
                    e.setCancelled(true);
                    return;
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if(PlayingPlayers.containsKey(e.getWhoClicked()) || PlayingPlayers.containsValue(e.getWhoClicked())) e.setCancelled(true);
    }

    public static boolean isBlockUnderU(int i, Inventory inventory) {
        if(i + 9 > 54) {
            return false;
        } else if(inventory.getItem(i + 9) == null) {
            return true;
        } else return false;
    }

    public static Player getKeyFromValue(Player value) {
        for (Player o : PlayingPlayers.keySet()) {
            if (PlayingPlayers.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    public static boolean endGame(Player p1, Player p2, Boolean whowon) {
        if(whowon) {
            p1.sendMessage("");
            p1.sendMessage("§aCongrats! You won the Connect 4 game against " + p2.getName() + "!");
            p1.sendMessage("");

            p2.sendMessage("");
            p2.sendMessage("§cOh no! You lost the Connect 4 game against " + p1.getName() + "!");
            p2.sendMessage("");

            if(Main.getPlugin().getConfig().getBoolean("4connect.broadcast")) {
                Bukkit.broadcastMessage("§6" + p1 + " §ajust won a match against §6" + p2 + " §a!");
            }

            System.out.println(p1.getName() + " won a Connect 4 game against " + p2.getName());
        } else {
            p2.sendMessage("");
            p2.sendMessage("§aCongrats! You won the Connect 4 game against " + p1.getName() + "!");
            p2.sendMessage("");

            p1.sendMessage("");
            p1.sendMessage("§cOh no! You lost the Connect 4 game against " + p2.getName() + "!");
            p1.sendMessage("");

            if(Main.getPlugin().getConfig().getBoolean("4connect.broadcast")) {
                Bukkit.broadcastMessage("§6" + p2.getName() + " §ajust won a match against §6" + p1.getName() + " §a!");
            }

            System.out.println(p2.getName() + " won a Connect 4 game against " + p1.getName());
        }

        //p1 is key
        PlayingPlayers.remove(p1);
        p1.closeInventory();
        p2.closeInventory();

        ConnectGame.invhash.remove(p2);
        return false;
    }

    public static boolean haveUWon(int i, Material material, Inventory inventory) {
        if(horizontalWin(i,material,inventory)) return true;
        if(verticalWin(i,material,inventory)) return true;
        if(leftDiagonalWin(i,material,inventory)) return true;
        if(rightDiagonalWin(i,material,inventory)) return true;

        return false;
    }

    public static boolean horizontalWin(int i, Material material, Inventory inventory) {
        int next = 0;
        i = i - i % 9;
        for(int y = 0; y < 9; y++) {
            if(inventory.getItem(i + y) == null) {
                next = 0;
                continue;
            }
            if(inventory.getItem(i + y).getType() == material) {
                next++;
            } else next = 0;
            if(next == 4) return true;
        }
        return false;
    }

    public static boolean verticalWin(int i, Material material, Inventory inventory) {
        int next = 0;
        i = i % 9;
        for(int y = 0; y < 6; y++) {
            if(inventory.getItem(i + y*9) == null) {
                next = 0;
                continue;
            }
            if(inventory.getItem(i + y*9).getType() == material) {
                next++;
            } else next = 0;
            if(next == 4) return true;
        }
        return false;
    }

    public static boolean leftDiagonalWin(int i, Material material, Inventory inventory) {
        int next = 0;
        for(int y = 0; y < 7; y++) {
            if(i % 9 == 0 || i < 10) {
                break;
            }
            i = i - 10;
        }

        for(int y = 0; y < 7; y++) {
            if(i + 10*y > 52) {
                break;
            }
            if(inventory.getItem(i + 10*y) == null) {
                next = 0;
                continue;
            }
            if(inventory.getItem(i + 10*y).getType() == material) {
                next++;
            } else next = 0;
            if(next == 4) return true;
        }
        return false;
    }

    public static boolean rightDiagonalWin(int i, Material material, Inventory inventory) {
        int next = 0;
        for(int y = 0; y < 7; y++) {
            if(i % 9 == 8 || i < 10) {
                break;
            }
            i = i - 8;
        }

        //this doesnt work yet
        for(int y = 0; y < 7; y++) {
            if(i + 8*y > 52) {
                break;
            }
            if(inventory.getItem(i + 8*y) == null) {
                next = 0;
                continue;
            }
            if(inventory.getItem(i + 8*y).getType() == material) {
                next++;
            } else next = 0;
            if(next == 4) return true;
        }
        return false;
    }


}
