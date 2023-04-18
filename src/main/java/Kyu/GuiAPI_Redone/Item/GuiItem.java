package Kyu.GuiAPI_Redone.Item;

import org.bukkit.inventory.ItemStack;

public class GuiItem {

    private GuiItemClickListener listener;
    private ItemStack itemStack;

    /**
     * Creates a new Item with the specified ItemStack
     * @param stack
     */
    public GuiItem(ItemStack stack) {
        this.itemStack = stack;
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
     * 
     * @param itemStack the new ItemStack
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
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

}
