package Kyu.GuiAPI_Redone.Windows;

import Kyu.GuiAPI_Redone.Errors.RowsOutOfBoundsException;
import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

abstract class DefaultWindow implements Window {

    private String title;
    private Map<Integer, GuiItem[]> pages = new HashMap<>();
    private int rows;
    private boolean isMultiPage = false;
    private int currPage;

    protected DefaultWindow(String title, int rows) {
        this.title = title;
        if (rows < 0 || rows > 6) {
            throw new RowsOutOfBoundsException();
        }
        this.rows = rows;
        pages.put(1, new GuiItem[rows * 9]);
        currPage = 1;
    }

    public GuiItem setItem(ItemStack item, int slot) {
        GuiItem gItem = new GuiItem(item, slot, this);
        pages.get(currPage)[slot] = gItem;
        return gItem;
    }

    public GuiItem setItem(Material itemType, @Nullable String name, int slot) {
        ItemStack is = new ItemStack(itemType);
        return null;
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

    }

    public ItemStack getItem(int slot) {
        return null;
    }

    public String getTitle() {
        return null;
    }

    public Player getHolder() {
        return null;
    }

    public GUI getGUI() {
        return null;
    }

    public void setPage(int page) {

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
        return 0;
    }

    public void clearPage(int page) {

    }

    public void clearAllPages() {

    }

    ItemStack getItemFromPage(int slot, int page) {
        return null;
    }

    public void refreshWindow() {

    }

}
