package Kyu.GuiAPI_Redone.Window.WindowImpl;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.TextUtil;
import Kyu.GuiAPI_Redone.Exceptions.SlotOutOfBoundsException;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.Window;
import net.kyori.adventure.text.Component;

public abstract class AnvilWindow extends Window {

    public AnvilWindow(GUI gui, String title) {
        super(gui, title);
        setInventory(Bukkit.createInventory(this, InventoryType.ANVIL, Component.text(TextUtil.color(title))));
        // setInventory(Bukkit.createInventory(this, rows * 9, Component.text(TextUtil.color(title))));
    }

    
    /**
     * Places an item in the windows, <b>without</b> linking the item to the window, use {@link Window#setItem(GuiItem, int)} instead
     */
    protected void set(GuiItem item, int slot) {
        int maxSlot = 3;
        if (slot < 0 || slot > maxSlot) {
            throw new SlotOutOfBoundsException(slot, maxSlot);
        }
        item.addWindow(this);
        getItems().put(slot, item);
        getInventory().setItem(slot, item.getItemStack());
    }
    
}
