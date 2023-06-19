package Kyu.GuiAPI_Redone.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Kyu.GuiAPI_Redone.TextUtil;
import Kyu.GuiAPI_Redone.Window.Window;
import net.kyori.adventure.text.Component;

public class GuiItem {

    private GuiItemClickListener listener;
    private HashSet<Window> windows = new HashSet<>();
    private ItemStack itemStack;

    /**
     * Creates a new Item with the specified ItemStack
     * @param stack
     */
    public GuiItem(ItemStack stack) {
        this.itemStack = stack;
    }

    /**
     * Creates a new Item with the specified Values
     * @param stack ItemStack of the Item
     * @param title Name of the Item (will be color translated)
     * @param lore Lore of the item (if wanted), each string is a new line
     */
    public GuiItem(ItemStack stack, String title, String... lore) {
        ItemMeta itemMeta = stack.getItemMeta();
        itemMeta.displayName(Component.text(TextUtil.color(title)));

        List<Component> itemLore = new ArrayList<>();
        for (String line : lore) {
            itemLore.add(Component.text(TextUtil.color(line)));
        }
        if (itemLore.size() > 0) itemMeta.lore(itemLore);

        stack.setItemMeta(itemMeta);
        this.itemStack = stack;
    }

    /**
     * Creates a new Item with the specified Values
     * @param material Material of the Item
     * @param name Name of the Item (will be color translated)
     * @param amountInStack Size of the ItemStack
     * @param lore Lore of the item (if wanted), each string is a new line
     */
    public GuiItem(Material material, String name, int amountInStack, String... lore) {
        ItemStack itemStack = new ItemStack(material, amountInStack);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(TextUtil.color(name)));

        List<Component> itemLore = new ArrayList<>();
        for (String line : lore) {
            itemLore.add(Component.text(TextUtil.color(line)));
        }
        if (itemLore.size() > 0) itemMeta.lore(itemLore);

        itemStack.setItemMeta(itemMeta);
        this.itemStack = itemStack;
    }

    /**
     * 
     * @param listener What to be called when the item gets clicked in the inventory
     * @return the item again for continued building
     */
    public GuiItem withListener(GuiItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Manually set the Listener for when the item is clicked
     * @param listener What to be called when the item gets clicked in the inventory
     */
    public void setListener(GuiItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * Sets a new ItemStack and updates all Windows with the item
     * @param itemStack the new ItemStack
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        updateLinkedWindows();
    }

    /**
     * Sets the lore of the Item and updates all Windows with the item
     * @param lore the new lore, each string is a new line (will be color translated)
     */
    public void setLore(String... lore) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<Component> itemLore = new ArrayList<>();
        for (String line : lore) {
            itemLore.add(Component.text(TextUtil.color(line)));
        }
        itemMeta.lore(itemLore);

        itemStack.setItemMeta(itemMeta);
        updateLinkedWindows();
    }

    /**
     * Change the name of the itemstack
     * @param name
     */
    public void setName(String name) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(TextUtil.color(name)));
        itemStack.setItemMeta(itemMeta);
        updateLinkedWindows();
    }

    /**
     * 
     * @return Listener for when the item gets clicked in the inventory
     */
    public GuiItemClickListener getListener() {
        return listener;
    }

    /**
     * 
     * @return the itemstack of the item
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * Add the link between item and window, that causes the item to refresh this window
     * if changes are made to the item
     * @param window
     */
    public void addWindow(Window window) {
        windows.add(window);
    }

    /**
     * Remove the link between item and window, that causes the item not the refresh in the specified window
     * if changes are made
     * @param window
     */
    public void removeWindow(Window window) {
        windows.remove(window);
    }

    private void updateLinkedWindows() {
        for (Window window : windows) {
            window.refreshInventory();
        }
    }

}
