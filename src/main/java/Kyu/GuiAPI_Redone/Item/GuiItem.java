package Kyu.GuiAPI_Redone.Item;

import Kyu.GuiAPI_Redone.Windows.Window;
import org.bukkit.inventory.ItemStack;

public class GuiItem {

    private ItemStack item;
    private Window parentWindow;
    private int slot;

    public GuiItem(ItemStack item, int slot, Window parentWindow) {
        this.item = item;
        this.parentWindow = parentWindow;
        this.slot = slot;
    }

    public ItemStack getItemStack() {
        return item;
    }

    public int getSlot() {
        return slot;
    }

    public Window getParentWindow() {
        return parentWindow;
    }
}
