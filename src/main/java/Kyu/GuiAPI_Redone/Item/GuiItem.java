package Kyu.GuiAPI_Redone.Item;

import org.bukkit.inventory.ItemStack;

public class GuiItem {

    private GuiItemClickListener listener;
    private ItemStack itemStack;

    public GuiItem(ItemStack stack) {
        this.itemStack = stack;
    }

    
    public GuiItem withListener(GuiItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    public void setListener(GuiItemClickListener listener) {
        this.listener = listener;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public GuiItemClickListener getListener() {
        return listener;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}
