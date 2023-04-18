package Kyu.GuiAPI_Redone.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Util;
import Kyu.GuiAPI_Redone.Exceptions.NoPaginationPossibleException;
import Kyu.GuiAPI_Redone.Exceptions.RowsOutOfBoundsException;
import Kyu.GuiAPI_Redone.Exceptions.SlotOutOfBoundsException;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import net.kyori.adventure.text.Component;

public class Window implements InventoryHolder {
    
    private GUI gui;
    private Inventory inv;
    private int rows;
    private String title;
    private Window nextPage;
    private Window prevPage;
    private Map<Integer, GuiItem> items = new HashMap<>();
    private Consumer<InventoryCloseEvent> onClose;
    private boolean disableClickEvent = true, isMultiPage = false, isIgnoreCloseEvent = false, preventClose = true;

    /**
     * Creates a new window to be used in a gui
     * @param gui The {@link GUI} to use
     * @param rows The amount of Rows per Page (1 - 6)
     * @param title The title of the Window (will be color translated)
     * @throws RowsOutOfBoundsException if rows are less than 1 or greater than 6
     */
    public Window(GUI gui, int rows, String title) {
        if (rows < 1 || rows > 6) {
            throw new RowsOutOfBoundsException();
        }

        this.gui = gui;
        this.title = title;
        this.rows = rows;
        inv = Bukkit.createInventory(gui.getHolder(), rows * 9, Component.text(Util.color(title)));
    }

    /**
     * Add a {@link GuiItem} to the Inventory, it will find the nearest free slot
     * and place it there
     * <p>
     * If No Slot is free and Multipage is enabled, it will create a new Page
     * @param item The item to add
     */
    public void addItem(GuiItem item) {
        int nextFreeSlot = getNextFreeSlot();
        if (nextFreeSlot == -1) {
            if (isMultiPage()) {
                if (nextPage == null) {
                    nextPage = new Window(gui, rows, title);
                    addToolbar(); //Readd toolbar because new page was added
                }
                nextPage.addItem(item);
                nextPage.setPrevPage(this);
            }
            return;
        }
        setItem(item, nextFreeSlot);
    }

    /**
     * Same as {@link Window#addItem(GuiItem)} but accepts a Slot
     * <p>
     * If the slot is used, it will override the item
     * @param item The item to add
     * @param slot The slot to place it in
     * @throws SlotOufOfBoundsException if the slot is less than 0 or greater than the inventory size
     */
    public void setItem(GuiItem item, int slot) {
        int maxSlot = rows * 6 - 1;
        if (slot < 0 || slot > maxSlot) {
            throw new SlotOutOfBoundsException(slot, maxSlot);
        }
        items.put(slot, item);
        inv.setItem(slot, item.getItemStack());
    }

    /**
     * Removes the item from the provided slot
     * @param slot Slot to remove the item from
     */
    public void removeItem(int slot) {
        items.remove(slot);
    }

    /**
     * Manually set a Previous Page. Will override the current Previous Page
     * @param prevPage The page to set
     */
    public void setPrevPage(Window prevPage) {
        this.prevPage = prevPage;
    }

    /**
     * Manually set a Next Page. Will override the current Next Page
     * @param nextPage
     */
    public void setNextPage(Window nextPage) {
        this.nextPage = nextPage;
    }

    /**
     * Gets the Page that is linked to before this one
     * @return The page prior to the current one
     */
    public Window getPrevPage() {
        return prevPage;
    }

    /**
     * Gets the page that is linked to after this one
     * @return The page after the current one
     */
    public Window getNextPage() {
        return nextPage;
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
     * @return Whether or not this Window supports multiple pages
     */
    public boolean isMultiPage() {
        return isMultiPage;
    }

    /**
     * 
     * @param isMultiPage Whether or not this window supports multiple pages
     */
    public void setMultiPage(boolean isMultiPage) {
        if (rows == 1) {
            throw new NoPaginationPossibleException();
        }
        this.isMultiPage = isMultiPage;
        if (isMultiPage()) addToolbar();
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
     */
    public void setOnClose(Consumer<InventoryCloseEvent> onClose) {
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

        if (isMultiPage()) {
            addToolbar();
        }
    }

    private int getMaxPages() {
        Window currPage;
        if (prevPage == null) {
            if (nextPage == null) return 1;
            currPage = this;
        } else {
            do {
                currPage = getPrevPage();
            } while (currPage.getPrevPage() != null);
        }
        int counter = 1;
        do {
            currPage = currPage.getNextPage();
            counter++;
        } while (currPage.getNextPage() != null);
        return counter;
    }

    private int getCurrPageNumber() {
        Window currPage;
        if (prevPage == null) {
            return 1;
        } else {
            int counter = 1;
            do {
                currPage = getPrevPage();
                counter++;
            } while (currPage.getPrevPage() != null);
            return counter;
        }
    }

    private int getNextFreeSlot() {
        int counter = 0;
        for (ItemStack is : inv.getContents()) {
            if (is == null || is.getType().equals(Material.AIR)) {
                return counter;
            }
            counter++;
        }

        return -1;
    }

    private void addToolbar() {
        ItemStack placeholderItem = new ItemStack(gui.getPlaceholderItem());
        ItemMeta meta = placeholderItem.getItemMeta();
        meta.displayName(Component.text(Util.color(gui.getPlaceholderText())));
        placeholderItem.setItemMeta(meta);

        int currPageNumber = getCurrPageNumber();
        int maxPages = getMaxPages();

        ItemStack pageForwItem = new ItemStack(gui.getPageItem());
        meta = pageForwItem.getItemMeta();
        meta.displayName(Component.text(Util.color(gui.getPageForwText()
            .replace("%page", "" + (getCurrPageNumber() - 1))
            .replace("%max", "" + getMaxPages()))));
        pageForwItem.setItemMeta(meta);

        ItemStack pageBackItem = new ItemStack(gui.getPageItem());
        meta = pageForwItem.getItemMeta();
        meta.displayName(Component.text(Util.color(gui.getPageBackText()
            .replace("%page", "" + (getCurrPageNumber() + 1))
            .replace("%max", "" + getMaxPages()))));
        pageForwItem.setItemMeta(meta);

        GuiItem placeholder = new GuiItem(placeholderItem);
        GuiItem pageForw = new GuiItem(pageForwItem).withListener(e -> {
            getNextPage().refreshInventory();
            gui.openWindow(getNextPage());
        });
        GuiItem pageBack = new GuiItem(pageBackItem).withListener(e -> {
            getPrevPage().refreshInventory();
            gui.openWindow(getPrevPage());
        });

        int firstSlotOfLastRow = rows - 1 * 9;

        for (int i = firstSlotOfLastRow; i < rows * 9; i++) {
            setItem(placeholder, i);
        }

        if (currPageNumber < maxPages) {
            setItem(pageForw, firstSlotOfLastRow + 5);
        }

        if (currPageNumber > 1) {
            setItem(pageBack, firstSlotOfLastRow + 3);
        }
    }

}
