package Kyu.GuiAPI_Redone.Windows;

import Kyu.GuiAPI_Redone.Errors.SlotOutOfBoundsException;
import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.FakeAnvil;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Util.Util;
import net.kyori.adventure.text.Component;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;

public class AnvilWindow implements Window, Listener{

    GUI gui;
    GuiItem[] items = new GuiItem[3];
    Inventory inv;
    String message;
    String Anviltitle;
    FakeAnvil anvil;

    public AnvilWindow(String message, Player player, String title, JavaPlugin plugin) {
        gui = new GUI(player, plugin);
        this.Anviltitle = title;
        inv = Bukkit.createInventory(getHolder(), InventoryType.ANVIL, Component.text(Util.color(title)));
        this.message = message;
        //this.anvil = new FakeAnvil(player, ,title);
    }

    @Override
    public void open() {
        refreshWindow();

    }

    @Override
    public void close() {

    }

    @Override
    public GuiItem setItem(ItemStack item, int slot) {
        checkSlotBounds(slot);
        GuiItem guiItem = new GuiItem(item, slot, this);
        items[slot] = guiItem;
        return guiItem;
    }

    @Override
    public GuiItem setItem(Material itemType, @Nullable String name, int slot) {
        checkSlotBounds(slot);
        ItemStack item = new ItemStack(itemType);
        if(name != null){
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(Util.color(name)));
            item.setItemMeta(meta);
        }
        GuiItem guiItem = new GuiItem(item, slot, this);
        items[slot] = guiItem;
        return guiItem;
    }

    @Override
    public void removeItem(int slot) {
        checkSlotBounds(slot);
        items[slot] = null;
    }

    @Override
    public ItemStack getItem(int slot) {
        GuiItem guiItem = items[slot];
        return guiItem.getItemStack();
    }

    @Override
    public GuiItem getGuiItem(int slot) {
        return items[slot];
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public Player getHolder() {
        return gui.getHolder();
    }

    @Override
    public GUI getGUI() {
        return gui;
    }

    @Override
    public void refreshWindow() {
        System.out.println(items[0].getItemStack().getType());
        inv.setItem(0, items[0].getItemStack());
    }

    @EventHandler
    private void onInvClick(InventoryClickEvent e) {
        System.out.println("test");
        handleInvClick(e);
    }

    boolean isInvMove(InventoryClickEvent e) {
        switch (e.getAction()) {
            case MOVE_TO_OTHER_INVENTORY:
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
            case HOTBAR_SWAP:
                return true;
            default:
                return false;
        }
    }

    void handleInvClick(InventoryClickEvent e){
        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(inv)) {
            return;
        }
        if (!e.getWhoClicked().equals(getHolder())) {
            return;
        }
        System.out.println(e.getAction());
        if (isInvMove(e)) {
            e.setCancelled(true);
        }
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }

        int slot = e.getSlot();
        GuiItem item = items[slot];
        if(item != null){
            item.executeOnClick(e);
        }

        if(slot == 2){
            //ACCEPT
        } else if(slot == 0){
            //NO
        }
    }

    void checkSlotBounds(int slot) {
        if (slot < 0 || slot > 1) {
            throw new SlotOutOfBoundsException(slot, 1);
        }
    }

}