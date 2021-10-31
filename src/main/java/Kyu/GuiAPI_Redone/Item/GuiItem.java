package Kyu.GuiAPI_Redone.Item;

import Kyu.GuiAPI_Redone.Util.Util;
import Kyu.GuiAPI_Redone.Windows.DefaultWindow;
import Kyu.GuiAPI_Redone.Windows.Window;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class GuiItem {

    private ItemStack item;
    private Window parentWindow;
    private int slot, page;
    private Consumer<InventoryClickEvent> function = null;
    private GItemType type;

    public GuiItem(ItemStack item, int slot, Window parentWindow) {
        this.item = item;
        this.parentWindow = parentWindow;
        this.slot = slot;
        type = GItemType.DEFAULT;
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

    /**
     * Not intended for public use!
     * @param slot
     */
    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Window getParentWindow() {
        return parentWindow;
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



    //TODO: Fix internal error if u go under 5
    public void setLore(int max, String s) {

        String patternStr = "\n";
        String[] paras = Pattern.compile(patternStr, Pattern.MULTILINE).split(s);
        ArrayList<Component> lore = new ArrayList<>();

        for(String string : paras){
            int length = string.length();
            int temp = 0;

            for(int i = 0; i < length - max; i = temp){
                int y = i + max;

                if(y >= length){
                    y = length - 1;
                }

                boolean reached = false;

                while(!Character.isWhitespace(string.charAt(y))){
                    y -= 1;
                    if(temp + y < i + max - (max/4)){
                        y = i + max;
                        reached = true;
                        break;
                    }
                }

                String subS = string.substring(temp, y);
                temp = y + 1; //Remove the space
                if(reached){
                    subS = subS + "-";
                    temp = y; //No space so we don't need to remove the first char
                }

                lore.add(Component.text(Util.color(subS)));
            }
            lore.add(Component.text(Util.color(string.substring(temp))));
        }

        ItemMeta meta = item.getItemMeta();
        meta.lore(lore);
        item.setItemMeta(meta);
    }

    public void relocatePage(int slot, int page) {
        GuiItem itemOnOldSlot = ((DefaultWindow) parentWindow).getGuiItemFromPage(getSlot(), getPage());
        if (itemOnOldSlot != null && itemOnOldSlot.equals(this)) {
            ((DefaultWindow) parentWindow).removeItemFromPage(getSlot(), getPage());
        }
        ((DefaultWindow) parentWindow).getPages().get(page)[slot] = this;
    }
}
