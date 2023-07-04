package Kyu.GuiAPI_Redone.Window;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Exceptions.RowsOutOfBoundsException;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.WindowImpl.ChestWindow;

public abstract class Window extends Openable implements InventoryHolder {
    
    private String title;
    private Inventory inv;
    private Map<Integer, GuiItem> items = new HashMap<>();


    /**
     * Creates a new window to be used in a gui
     * @param gui The {@link GUI} to use
     * @param rows The amount of Rows per Page (1 - 6)
     * @param title The title of the Window (will be color translated)
     * @throws RowsOutOfBoundsException if rows are less than 1 or greater than 6
     */
    public Window(GUI gui, String title) {
        super(gui);
        this.title = title;
    }

    /**
     * Add a {@link GuiItem} to the Inventory, it will find the nearest free slot
     * and place it there
     * @param item The item to add
     */
    public void addItem(GuiItem item) {
        int nextFreeSlot = getNextFreeSlot();
        if (nextFreeSlot == -1) {
            return;
        }
        setItem(item, nextFreeSlot);
    }

    /**
     * Same as {@link ChestWindow#addItem(GuiItem)} but accepts a Slot
     * <p>
     * If the slot is used, it will override the item
     * @param item The item to add
     * @param slot The slot to place it in
     * @throws SlotOufOfBoundsException if the slot is less than 0 or greater than the inventory size
     */
    public void setItem(GuiItem item, int slot) {
        item.addWindow(this);
        set(item, slot);
    }

    /*
     * Actual Logic of the item setting
     */
    protected abstract void set(GuiItem item, int slot);

    /**
     * Removes the item from the provided slot
     * @param slot Slot to remove the item from
     */
    public void removeItem(int slot) {
        GuiItem item = items.get(slot);
        item.removeWindow(this);
        items.remove(slot);
    }

    /**
     * @return The inventory of the window that will be shown to the player when using {@link GUI#openWindow(Window)}
     */
    public Inventory getInventory() {
        return inv;
    }

    /**
     * 
     * @return A list of {@link GuiItem} placed in the Inventory
     */
    public Map<Integer, GuiItem> getItems() {
        return items;
    }

    /**
     * Get a {@link GuiItem} from a specific slot
     * @param slot the slot to get the item from
     * @return the GuiItem from that slot or Null if slot is empty
     */
    public GuiItem getItem(int slot) {
        return items.getOrDefault(slot, null);
    }

    /**
     * 
     * @return Title of the Window
     */
    public String getTitle() {
        return title;
    }

    /**
     * Refreshes the Inventory
     */
    public void refreshInventory() {
        inv.clear();
        for (int slot : items.keySet()) {
            GuiItem item = items.get(slot);
            inv.setItem(slot, item.getItemStack());
        }
    }

    protected void setInventory(Inventory inv) {
        this.inv = inv;
    }

    protected int getNextFreeSlot() {
        int counter = 0;
        for (ItemStack is : inv.getContents()) {
            if (is == null || is.getType().equals(Material.AIR)) {
                return counter;
            }
            counter++;
        }

        return -1;
    }
}
