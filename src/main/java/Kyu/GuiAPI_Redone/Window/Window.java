package Kyu.GuiAPI_Redone.Window;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Util;
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
    private boolean disableClickEvent = true, isMultiPage = false;

    public Window(GUI gui, int rows, String title) {
        if (rows < 0 || rows > 6) {
            //TODO: Throw Exception
        }
        this.gui = gui;
        this.title = title;
        this.rows = rows;
        inv = Bukkit.createInventory(gui.getHolder(), rows * 9, Component.text(Util.color(title)));
    }

    public void open() {
        gui.getHolder().openInventory(this.inv);
    }

    public void addItem(GuiItem item) {
        int nextFreeSlot = getNextFreeSlot();
        if (nextFreeSlot == -1) {
            if (isMultiPage()) {
                if (nextPage == null) {
                    nextPage = new Window(gui, rows, title);
                }
                nextPage.addItem(item);
                nextPage.setPrevPage(this);
            }
            return;
        }
        setItem(item, nextFreeSlot);
    }

    public void setItem(GuiItem item, int slot) {
        if (slot < 0 || slot > rows * 6 - 1) {
            //TODO: Throw Warning
            return;
        }
        items.put(slot, item);
    }

    public void setPrevPage(Window prevPage) {
        this.prevPage = prevPage;
    }

    public void setNextPage(Window nextPage) {
        this.nextPage = nextPage;
    }

    public Window getPrevPage() {
        return prevPage;
    }

    public Window getNextPage() {
        return nextPage;
    }

    public void removeItem(int slot) {
        items.remove(slot);
    }

    public boolean isDisableClickEvent() {
        return disableClickEvent;
    }

    public void setDisableClickEvent(boolean disableClickEvent) {
        this.disableClickEvent = disableClickEvent;
    }

    public Inventory getInventory() {
        return inv;
    }

    public Map<Integer, GuiItem> getItems() {
        return items;
    }

    public GuiItem getItem(int slot) {
        return items.getOrDefault(slot, null);
    }

    public boolean isMultiPage() {
        return isMultiPage;
    }

    public void setMultiPage(boolean isMultiPage) {
        if (rows == 1) {
            //TODO: Throw Warning
        }
        this.isMultiPage = isMultiPage;
        if (isMultiPage()) addToolbar();
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
            .replace("%page", "" + getCurrPageNumber())
            .replace("%max", "" + getMaxPages()))));
        pageForwItem.setItemMeta(meta);

        ItemStack pageBackItem = new ItemStack(gui.getPageItem());
        meta = pageForwItem.getItemMeta();
        meta.displayName(Component.text(Util.color(gui.getPageBackText()
            .replace("%page", "" + getCurrPageNumber())
            .replace("%max", "" + getMaxPages()))));
        pageForwItem.setItemMeta(meta);

        GuiItem placeholder = new GuiItem(placeholderItem);
        GuiItem pageForw = new GuiItem(pageForwItem).withListener(e -> {
            gui.openWindow(getNextPage());
        });
        GuiItem pageBack = new GuiItem(pageBackItem).withListener(e -> {
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
