package Kyu.GuiAPI_Redone.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;

public abstract class Window implements InventoryHolder {
    
    private GUI gui;
    private String title;
    private Inventory inv;
    private Map<Integer, GuiItem> items = new HashMap<>();
    private Consumer<InventoryCloseEvent> onClose;
    private boolean disableClickEvent = true, isIgnoreCloseEvent = false, preventClose = true;

    /**
     * Creates a new window to be used in a gui
     * @param gui The {@link GUI} to use
     * @param rows The amount of Rows per Page (1 - 6)
     * @param title The title of the Window (will be color translated)
     * @throws RowsOutOfBoundsException if rows are less than 1 or greater than 6
     */
    public Window(GUI gui, String title) {
        this.gui = gui;
        this.title = title;
    }

    public abstract void addItem(GuiItem item);

    public abstract void setItem(GuiItem item, int slot);

    /**
     * Removes the item from the provided slot
     * @param slot Slot to remove the item from
     */
    public void removeItem(int slot) {
        items.remove(slot);
    }

    /**
     * Whether or not to cancel the OnInventoryClick event, preventing users from taking/placing items from the inventory
     * @return whether disableClickEvent is on or off
     */
    public boolean isDisableClickEvent() {
        return disableClickEvent;
    }

    /**
     * Set whether or not InventoryClickEvent is canceled or not. Canceling prevents users from taking/placing items from the inventory
     * @param disableClickEvent Whether or not to cancel InventoryClickEvent
     */
    public void setDisableClickEvent(boolean disableClickEvent) {
        this.disableClickEvent = disableClickEvent;
    }

    /**
     *
     * @return Whether or not the user can close the window with ESC
     */
    public boolean isPreventClose() {
        return preventClose;
    }

    /**
     * 
     * @param preventClose Whether or not the user shall be able to close the window with ESC
     */
    public void setPreventClose(boolean preventClose) {
        this.preventClose = preventClose;
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
     * @return Whether or not the close Event will be ignored. If true, the onClose Consumer will not be called
     */
    public boolean isIgnoreCloseEvent() {
        return isIgnoreCloseEvent;
    }

    /**
     * 
     * @param isIgnoreCloseEvent Whether or not the close Event will be ignored. If true, the onClose Consumer will not be called
     */
    public void setIgnoreCloseEvent(boolean isIgnoreCloseEvent) {
        this.isIgnoreCloseEvent = isIgnoreCloseEvent;
    }

    /**
     * 
     * @return Function to call when the inventory gets closed and {@link Window#isIgnoreCloseEvent} is False. Or Null if no function is set
     */
    public Consumer<InventoryCloseEvent> getOnClose() {
        return onClose;
    }

    /**
     * 
     * @param onClose Function to call when the inventory gets closed and {@link Window#isIgnoreCloseEvent} is False.
     * Automatically disables {@link Window#isPreventClose}
     */
    public void setOnClose(Consumer<InventoryCloseEvent> onClose) {
        this.preventClose = false;
        this.onClose = onClose;
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

    /**
     * @return The {@link GUI} the Window is connected to
     */
    public GUI getGui() {
        return gui;
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
