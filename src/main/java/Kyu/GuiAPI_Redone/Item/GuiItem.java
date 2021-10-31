package Kyu.GuiAPI_Redone.Item;

import Kyu.GuiAPI_Redone.Util.Util;
import Kyu.GuiAPI_Redone.Windows.Window;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.function.Consumer;

public class GuiItem {

    private ItemStack item;
    private Window parentWindow;
    private int slot, page;
    private Consumer<InventoryClickEvent> function = null;
    private GItemType type;

    //-- Maybe this is quite nice to have?

    boolean glow;
    ArrayList<String> lores;

    public GuiItem(ItemStack item, int slot, Window parentWindow) {
        this.item = item;
        this.parentWindow = parentWindow;
        this.slot = slot;
        type = GItemType.DEFAULT;
        lores = new ArrayList<>();
    }

    public GuiItem(ItemStack item, int slot, GItemType type, Window parentWindow) {
        this.item = item;
        this.parentWindow = parentWindow;
        this.slot = slot;
        this.type = type;
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

    /**
     * Not intended for public use!
     * @param slot
     */
    public void setSlot(int slot) {
        this.slot = slot;
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

    public GItemType getType() {
        return type;
    }

    public void setType(GItemType type) {
        this.type = type;
    }

    public int getPage() {
        return page;
    }

    //Not intended for public use
    public void setPage(int page) {
        this.page = page;
    }

    public enum GItemType {
        PLACEHOLDER, DEFAULT;
    }

    public void setLore(String lore) {

        int max = 50; //50 for max chars on a line for example
        int temp = 0;

        for(int i = 1; i <= lore.length() - max; i = temp){
            int y = i + max;
            if(y > lore.length()){
                y = lore.length() -1;
            }
            int x = 0;

            while(!Character.isWhitespace(lore.charAt(y))){
                y -= 1;
                if(x > 20){
                    y = y + x;
                    break;
                }
                x++;
            }
            String s = lore.substring(temp, y);
            temp = y+1;
            if(x > 20){
                s = s + "-";
                temp = y;
            }
            lores.add(Util.color(s));
        }
        lores.add(Util.color(lore.substring(temp)));
    }
}
