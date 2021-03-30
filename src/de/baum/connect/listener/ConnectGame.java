package de.baum.connect.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Skull;

import java.util.ArrayList;
import java.util.HashMap;

public final class ConnectGame extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player p1;
    private Player p2;
    public static HashMap<Player, Inventory> invhash = new HashMap<>();
    public static ItemStack red = new ItemStack(Material.RED_CONCRETE);
    public static ItemStack blue = new ItemStack(Material.BLUE_CONCRETE);
    public static ItemStack turnblue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
    public static ItemStack turnred = new ItemStack(Material.RED_STAINED_GLASS_PANE);
    public static ItemStack grayglass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);

    public ConnectGame(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;

        Inventory inv = Bukkit.createInventory(null, 9*6, "§8Connect 4!");
        invhash.put(p1,inv);

        p1.openInventory(invhash.get(p1));
        p2.openInventory(invhash.get(p1));

        ItemMeta metared = red.getItemMeta();
        metared.setDisplayName("§cRED");
        red.setItemMeta(metared);

        ItemMeta metablue = blue.getItemMeta();
        metablue.setDisplayName("§9BLUE");
        blue.setItemMeta(metablue);

        //Ränder
        ItemMeta metabitch = grayglass.getItemMeta();
        metabitch.setDisplayName(" ");
        grayglass.setItemMeta(metabitch);

        //Köpfe
        ItemStack head1 = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta head1skull = (SkullMeta) head1.getItemMeta();
        head1skull.setOwningPlayer(p1);
        head1skull.setDisplayName("§9" + p1.getName());
        head1.setItemMeta(head1skull);

        ItemStack head2 = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta head2skull = (SkullMeta) head2.getItemMeta();
        head2skull.setOwningPlayer(p2);
        head2skull.setDisplayName("§c" + p2.getName());
        head2.setItemMeta(head2skull);

        //tutorial book
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta metabook = book.getItemMeta();
        metabook.setDisplayName("Connect 4!");
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("§7Place blocks with RIGHT-CLICK");
        lore.add("");
        lore.add("§7Have 4 of the same blocks");
        lore.add("§7diagonal/vertical/horizontal");
        lore.add("§7to win!");
        metabook.setLore(lore);
        book.setItemMeta(metabook);

        //turn things
        //Item Meta
        ItemMeta metabluet = turnblue.getItemMeta();
        metabluet.setDisplayName("§9It's Blue's Turn");
        turnblue.setItemMeta(metabluet);

        ItemMeta metaredt = turnred.getItemMeta();
        metaredt.setDisplayName("§cIt's Red's Turn");
        turnred.setItemMeta(metaredt);

        for(int i = 0; i < 6 ; i++) {
            inv.setItem(i*9+8,grayglass);
        }
        for(int i = 0; i < 6 ; i++) {
            inv.setItem(i*9,grayglass);
        }

        inv.setItem(53,head1);
        inv.setItem(45,head2);

        inv.setItem(0,book);

        inv.setItem(36,turnred);

    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
