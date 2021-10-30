package Kyu.GuiAPI_Redone.Windows;

import Kyu.GuiAPI_Redone.Errors.NoMultiPageOnException;
import Kyu.GuiAPI_Redone.Errors.PageOutOfBoundsException;
import Kyu.GuiAPI_Redone.Errors.RowsOutOfBoundsException;
import Kyu.GuiAPI_Redone.Errors.SlotOutOfBoundsException;
import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

abstract class DefaultWindow implements Window {

    private GUI gui;
    private Inventory inv;

    private String title;
    private int rows;
    private Map<Integer, GuiItem[]> pages = new HashMap<>();
    private int currPage;
    private boolean isMultiPage = false, preventItemGrab = true, preventItemPlace = true;


    protected DefaultWindow(@Nullable String title, int rows, GUI gui, JavaPlugin plugin) {
        this.title = title;
        this.gui = gui;
        if (rows < 0 || rows > 6) {
            throw new RowsOutOfBoundsException();
        }
        if (title == null) {
            inv = Bukkit.createInventory(gui.getHolder(), rows * 9);
        } else {
            inv = Bukkit.createInventory(gui.getHolder(), rows * 9, Component.text(Util.color(title)));
        }
        this.rows = rows;
        pages.put(1, new GuiItem[rows * 9]);
        currPage = 1;
    }

    /*
    =============================================================================

                                User Accessible

    =============================================================================
     */


    /**
     * Opens the window
     */
    public void open() {
        refreshWindow();
        gui.getHolder().openInventory(inv);
    }

    /**
     * Closes the window
     */
    public void close() {

    }

    /*
    =============================================================================

                               Item Related

    =============================================================================
     */

    /**
     * Sets Item on Current Page at specified Slot
     * @param item the item appearing in the GUI
     * @param slot Slot the item will be placed in. Throws Exception when out of bounds
     * @return Added Item as GuiItem
     */
    public GuiItem setItem(ItemStack item, int slot) {
        checkSlotBounds(slot);
        GuiItem gItem = new GuiItem(item, slot, this);
        pages.get(currPage)[slot] = gItem;
        inv.setItem(slot, item);
        return gItem;
    }

    /**
     * Sets Item on Current Page at specified Slot
     * @param itemType Item Material
     * @param name Nullable - Name of the Item
     * @param slot Slot the item will be placed in. Throws Exception when out of bounds
     * @return Added Item as GuiItem
     */
    public GuiItem setItem(Material itemType, @Nullable String name, int slot) {
        checkSlotBounds(slot);
        ItemStack is = new ItemStack(itemType);
        if (name != null) {
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(Util.color(name)));
            is.setItemMeta(im);
        }
        GuiItem gItem = new GuiItem(is, slot, this);
        pages.get(currPage)[slot] = gItem;
        inv.setItem(slot, is);
        return gItem;
    }

    /**
     * Sets Item on Specified Page at specified Slot
     * Requires multiPage to be true, will throw Exception otherwise
     * @param item Item appearing
     * @param slot Slot the Item will be placed in. Throws Exception if out of bounds
     * @param page Page the Item will be placed on. Throws Exception if out of bounds
     * @return Added Item as GuiItem
     */
    public GuiItem setItemAtPage(ItemStack item, int slot, int page) {
        checkMultiPage();
        checkPageBounds(page);
        checkSlotBounds(slot);

        GuiItem gItem = new GuiItem(item, slot, this);
        pages.get(page)[slot] = gItem;
        return gItem;
    }

    /**
     * Sets Item on Specified Page at specified Slot
     * Requires multiPage to be true, will throw Exception otherwise
     * @param itemType Item Material
     * @param name Nullable - Name of the Item
     * @param slot Slot the Item will be placed in. Throws Exception if out of bounds
     * @param page Page the Item will be placed in. Throws Exception if out of bounds
     * @return Added Item as GuiItem
     */
    public GuiItem setItemAtPage(Material itemType, @Nullable String name, int slot, int page) {
        checkMultiPage();
        checkPageBounds(page);
        checkSlotBounds(slot);
        ItemStack is = new ItemStack(itemType);
        if (name != null) {
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(Util.color(name)));
            is.setItemMeta(im);
        }
        GuiItem gItem = new GuiItem(is, slot, this);
        pages.get(page)[slot] = gItem;
        return gItem;
    }

    /**
     * Removes Item from specified slot of the current Page
     * @param slot Slot that will be emptied. Throws Exception if out of bounds
     */
    public void removeItem(int slot) {
        checkSlotBounds(slot);
        pages.get(currPage)[slot] = null;
        inv.setItem(slot, new ItemStack(Material.AIR));
    }

    /**
     * Returns item of specified slot from the current page
     * @param slot Slot the item will be grabbed from. Throws Exception if out of bounds
     * @return ItemStack of Slot or null if slot is empty
     */
    public ItemStack getItem(int slot) {
        checkSlotBounds(slot);
        GuiItem item = pages.get(currPage)[slot];
        return item == null ? null : item.getItemStack();
    }

    /**
     * Returns GuiItem of specified slot from the current Page
     * @param slot Slot the GuiItem will be grabbed from. Throws Exception if out of bounds
     * @return GuiItem of Slot or null if slot is empty
     */
    public GuiItem getGuiItem(int slot) {
        checkSlotBounds(slot);
        return pages.get(currPage)[slot];
    }

    /**
     * Returns item of specified slot from specified page
     * @param slot Slot the item will be grabbed from. Throws Exception if out of bounds
     * @param page Page the item will be grabbed from. Throws Exception if out of bounds
     * @return ItemStack of Slot or null if slot is empty
     */
    public ItemStack getItemFromPage(int slot, int page) {
        checkMultiPage();
        checkPageBounds(page);
        checkSlotBounds(slot);
        GuiItem gItem = pages.get(page)[slot];
        return gItem == null ? null : gItem.getItemStack();
    }

    /**
     * Returns GuiItem of specified slot from specified page
     * @param slot Slot the item will be grabbed from. Throws Exception if out of bounds
     * @param page Page the item will be grabbed from. Throws Exception if out of bounds
     * @return GuiItem of Slot or null if slot is empty
     */
    public GuiItem getGuiItemFromPage(int slot, int page) {
        checkMultiPage();
        checkPageBounds(page);
        checkSlotBounds(slot);
        return pages.get(page)[slot];
    }


    /*
    =============================================================================

                               Page Related

    =============================================================================
     */

    /**
     * Changes Page to specified page
     * Requires multiPage to be true, will throw Exception otherwise
     * @param page Page to change to. Throws Exception if out of bounds
     */
    public void setPage(int page) {
        checkMultiPage();
        checkPageBounds(page);
        currPage = page;
        refreshWindow();
    }

    /**
     * Clears specified page
     * @param page Page to be cleared. Requires multiPage to be true if greater than 1. Throws Exception if out of bounds
     */
    public void clearPage(int page) {
        if (page > 1) {
            checkMultiPage();
        }
        checkPageBounds(page);
        pages.remove(page);
        pages.put(page, new GuiItem[rows * 9]);
        if (currPage == page) {
            refreshWindow();
        }
    }

    /**
     * Clears all Pages and removes them (except first)
     */
    public void clearAllPages() {
        pages.clear();
        pages.put(1, new GuiItem[rows * 9]);
        currPage = 1;
        refreshWindow();
    }

    /**
     * Changes whether the inventory is allowed to have multiple pages
     * If set from true to false, the current Page will be set to 1
     * the other pages will not be deleted tho
     * @param value
     */
    public void setMultiPage(boolean value) {
        isMultiPage = value;
    }

    /**
     *
     * @return Current Page Number
     */
    public int getCurrentPage() {
        return currPage;
    }

    /**
     *
     * @return Map<PageNumber, Array of GuiItems> of all Pages
     */
    public Map<Integer, GuiItem[]> getPages() {
        return pages;
    }

    /**
     *
     * @return Whether the inventory is allowed to have multiple pages
     */
    public boolean isMultiPage() {
        return isMultiPage;
    }

    /**
     * Reloads all Items on the current page
     */
    public void refreshWindow() {
        GuiItem[] items = pages.get(currPage);
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                continue;
            }
            inv.setItem(i, items[i].getItemStack());
        }
    }

    /*
    =============================================================================

                                Getters/Setters

    =============================================================================
     */

    /**
     * Set whether taking items from the inventory should be prevented
     * @param value
     */
    public void setPreventItemGrab(boolean value) {
        preventItemGrab = value;
    }

    /**
     *
     * @return Whether taking items from the inventory is prevented
     */
    public boolean isPreventItemGrab() {
        return preventItemGrab;
    }

    /**
     * Sets whether Placing Items in the inventory should be prevented
     * Note: Placed items will <b>not</b> be added to the items list so far.
     * @param preventItemPlace
     */
    public void setPreventItemPlace(boolean preventItemPlace) {
        this.preventItemPlace = preventItemPlace;
    }

    /**
     *
     * @return Whether Placing Items in the inventory is prevented
     * Note: Placed items will <b>not</b> be added to the items list so far.
     */
    public boolean isPreventItemPlace() {
        return preventItemPlace;
    }

    /**
     *
     * @return Title of the Inventory
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @return Inventory Holder
     */
    public Player getHolder() {
        return gui.getHolder();
    }

    /**
     *
     * @return GUI the Window is part of
     */
    public GUI getGUI() {
        return gui;
    }

    /**
     *
     * @return amount of rows of the inventory
     */
    public int getRows() {
        return rows;
    }

    /**
     *
     * @return the inventory of the window
     */
    public Inventory getInv() {
        return inv;
    }


    /*
    =============================================================================

                           Package Private Methods

    =============================================================================
     */

    void checkPageBounds(int page) {
        if (page < 1 || page > pages.size()) {
            throw new PageOutOfBoundsException(page, pages.size());
        }
    }

    void checkMultiPage() {
        if (!isMultiPage) {
            throw new NoMultiPageOnException();
        }
    }

    void checkSlotBounds(int slot) {
        if (slot < 0 || slot > rows * 9 - 1) {
            throw new SlotOutOfBoundsException(slot, rows * 9 -1);
        }
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

    void handleInvClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || !e.getClickedInventory().equals(getInv())) {
            return;
        }
        if (!e.getWhoClicked().equals(getHolder())) {
            return;
        }
        System.out.println(e.getAction());
        if (isPreventItemPlace() && isInvMove(e)) {
            e.setCancelled(true);
        }
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }
        if (isPreventItemGrab()) {
            e.setCancelled(true);
        }
        int slot = e.getSlot();
        GuiItem item = getPages().get(getCurrentPage())[slot];
        if (item == null) {
            return;
        }
        item.executeOnClick(e);
    }

}
