package Kyu.GuiAPI_Redone.Item;

import Kyu.GuiAPI_Redone.Windows.Window;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class GuiItem {

    private ItemStack item;
    private Window parentWindow;
    private int slot;
    private Consumer<InventoryClickEvent> function = null;

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

    public void setOnClick(Consumer<InventoryClickEvent> consumer) {
        function = consumer;
    }

    public void executeOnClick(InventoryClickEvent e) {
        if (function == null) {
            return;
        }
        function.accept(e);
    }

}
