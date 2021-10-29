package Kyu.GuiAPI_Redone.Windows;

import Kyu.GuiAPI_Redone.Errors.NoMultiPageOnException;
import Kyu.GuiAPI_Redone.Errors.PageOutOfBoundsException;
import Kyu.GuiAPI_Redone.Errors.RowsOutOfBoundsException;
import Kyu.GuiAPI_Redone.Errors.SlotOutOfBoundsException;
import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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

    protected DefaultWindow(String title, int rows, GUI gui) {
        this.title = title;
        this.gui = gui;
        if (rows < 0 || rows > 6) {
            throw new RowsOutOfBoundsException();
        }
        this.rows = rows;
        pages.put(1, new GuiItem[rows * 9]);
        currPage = 1;
    }

    public GuiItem setItem(ItemStack item, int slot) {
        checkSlotBounds(slot);
        GuiItem gItem = new GuiItem(item, slot, this);
        pages.get(currPage)[slot] = gItem;
        return gItem;
    }

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

    private void checkSlotBounds(int slot) {
        if (slot < 0 || slot > rows * 9 - 1) {
            throw new SlotOutOfBoundsException(slot, rows * 9 -1);
        }
    }

    public GuiItem setItemAtPage(ItemStack item, int slot, int page) {
        return null;
    }

    public GuiItem setItemAtPage(Material itemType, @Nullable String name, int slot, int page) {
        return null;
    }

    public void open() {

    }

    public void close() {

    }

    public void removeItem(int slot) {
        checkSlotBounds(slot);
        pages.get(currPage)[slot] = null;
    }

    public ItemStack getItem(int slot) {
        checkSlotBounds(slot);
        GuiItem item = pages.get(currPage)[slot];
        return item == null ? null : item.getItemStack();
    }

    public GuiItem getGuiItem(int slot) {
        checkSlotBounds(slot);
        return pages.get(currPage)[slot];
    }

    public String getTitle() {
        return title;
    }

    public Player getHolder() {
        return null;
    }

    public GUI getGUI() {
        return null;
    }

    public void setPage(int page) {
        checkMultiPage();
        checkPageBounds(page);
        currPage = page;
        refreshWindow();
    }

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

    public boolean isMultiPage() {
        return isMultiPage;
    }

    public void setMultiPage(boolean value) {
        isMultiPage = value;
    }

    public int getCurrentPage() {
        return currPage;
    }

    public int getRows() {
        return rows;
    }

    public void clearPage(int page) {
        checkMultiPage();
        checkPageBounds(page);
        pages.remove(page);
        pages.put(page, new GuiItem[rows * 9]);
        if (currPage == page) {
            refreshWindow();
        }
    }

    public void clearAllPages() {
        pages.clear();
        pages.put(1, new GuiItem[rows * 9]);
        currPage = 1;
        refreshWindow();
    }

    public ItemStack getItemFromPage(int slot, int page) {
        checkMultiPage();
        checkPageBounds(page);
        checkSlotBounds(slot);
        GuiItem gItem = pages.get(page)[slot];
        return gItem == null ? null : gItem.getItemStack();
    }

    public GuiItem getGuiItemFromPage(int slot, int page) {
        checkMultiPage();
        checkPageBounds(page);
        checkSlotBounds(slot);
        return pages.get(page)[slot];
    }

    public void refreshWindow() {

    }

}
