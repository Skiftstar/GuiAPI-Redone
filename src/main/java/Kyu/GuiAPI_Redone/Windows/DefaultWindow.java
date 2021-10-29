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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

abstract class DefaultWindow implements Window {

    private String title;
    private Map<Integer, GuiItem[]> pages = new HashMap<>();
    private int rows;
    private boolean isMultiPage = false;
    private int currPage;
    private GUI gui;
    private Inventory inv;

    /**
     * Creates a new Default (Chest) Window
     * @param title Title of the Window
     * @param rows Amount of rows. Throws Exception if out of bounds
     * @param gui GUI the window is part of
     */
    protected DefaultWindow(String title, int rows, GUI gui) {
        this.title = title;
        this.gui = gui;
        if (rows < 0 || rows > 6) {
            throw new RowsOutOfBoundsException();
        }
        inv = Bukkit.createInventory(gui.getHolder(), rows * 9);
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
        GuiItem gItem;
        if (name == null) {
            gItem = new GuiItem(is, slot, this);
        } else {
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(Util.color(name)));
            is.setItemMeta(im);
            gItem = new GuiItem(is, slot, this);
        }
        pages.get(currPage)[slot] = gItem;
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
        return null;
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
        return null;
    }

    /**
     * Removes Item from specified slot of the current Page
     * @param slot Slot that will be emptied. Throws Exception if out of bounds
     */
    public void removeItem(int slot) {
        checkSlotBounds(slot);
        pages.get(currPage)[slot] = null;
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
     * @return Whether the inventory is allowed to have multiple pages
     */
    public boolean isMultiPage() {
        return isMultiPage;
    }

    /**
     * Reloads all Items on the current page
     */
    public void refreshWindow() {

    }

    /*
    =============================================================================

                                Getters/Setters

    =============================================================================
     */

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
        return null;
    }

    /**
     *
     * @return GUI the Window is part of
     */
    public GUI getGUI() {
        return null;
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

                                Private Methods

    =============================================================================
     */

    private void checkPageBounds(int page) {
        if (page < 1 || page > pages.size()) {
            throw new PageOutOfBoundsException(page, pages.size());
        }
    }

    private void checkMultiPage() {
        if (!isMultiPage) {
            throw new NoMultiPageOnException();
        }
    }

    private void checkSlotBounds(int slot) {
        if (slot < 0 || slot > rows * 9 - 1) {
            throw new SlotOutOfBoundsException(slot, rows * 9 -1);
        }
    }


}
