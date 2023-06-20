package Kyu.GuiAPI_Redone.Window.WindowImpl;

import org.bukkit.Bukkit;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.TextUtil;
import Kyu.GuiAPI_Redone.Exceptions.RowsOutOfBoundsException;
import Kyu.GuiAPI_Redone.Exceptions.SlotOutOfBoundsException;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.Window;
import net.kyori.adventure.text.Component;

public abstract class ChestWindow extends Window {

    private int rows;

    /**
     * Creates a new window to be used in a gui
     * @param gui The {@link GUI} to use
     * @param rows The amount of Rows per Page (1 - 6)
     * @param title The title of the Window (will be color translated)
     * @throws RowsOutOfBoundsException if rows are less than 1 or greater than 6
     */
    public ChestWindow(GUI gui, int rows, String title) {
        super(gui, title);
        if (rows < 1 || rows > 6) {
            throw new RowsOutOfBoundsException();
        }

        this.rows = rows;
        setInventory(Bukkit.createInventory(this, rows * 9, Component.text(TextUtil.color(title))));
    }

    /**
     * Places an item in the windows, <b>without</b> linking the item to the window, use {@link Window#setItem(GuiItem, int)} instead
     */
    protected void set(GuiItem item, int slot) {
        int maxSlot = rows * 9 - 1;
        if (slot < 0 || slot > maxSlot) {
            throw new SlotOutOfBoundsException(slot, maxSlot);
        }
        item.addWindow(this);
        getItems().put(slot, item);
        getInventory().setItem(slot, item.getItemStack());
    }

    /**
     * @return The amount of rows in the Window
     */
    public int getRows() {
        return rows;
    }
}
