package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.TextUtil;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import net.kyori.adventure.text.Component;

public class TradeToolbar {
    
    private String setReadyText = TextUtil.color("&aReady"), 
                        setNotReadyText = TextUtil.color("&cNot Ready"), 
                        abortText = TextUtil.color("&4Cancel"), 
                        readyIndicatorText = " ", 
                        notReadyIndicatorText = " ";
    private ItemStack setReadyItem = new ItemStack(Material.GREEN_WOOL, 1), 
                        setNotReadyItem = new ItemStack(Material.RED_WOOL, 1), 
                        readyIndicator = new ItemStack(Material.GREEN_STAINED_GLASS_PANE, 1), 
                        notReadyIndicator = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1),
                        abortItem = new ItemStack(Material.BARRIER, 1);

    /**
     * Set the items for the toolbar
     * @param setReadyItem For changing the status to "ready"
     * @param setNotReadyItem For changing the status to "not ready"
     * @param readyIndicator Shows when a party is "ready"
     * @param notReadyIndicator Shows when a party is "not ready"
     * @param abortItem For aborting the trade
     */
    public void setItems(ItemStack setReadyItem, ItemStack setNotReadyItem, ItemStack readyIndicator, ItemStack notReadyIndicator, ItemStack abortItem) {
        this.setReadyItem = setReadyItem;
        this.setNotReadyItem = setNotReadyItem;
        this.notReadyIndicator = notReadyIndicator;
        this.readyIndicator = readyIndicator; 
        this.abortItem = abortItem;
    }

    /**
     * See {@link TradeToolbar#setItems(ItemStack, ItemStack, ItemStack, ItemStack, ItemStack)}
     */
    public void setItems(Material setReadyItem, Material setNotReadyItem, Material readyIndicator, Material notReadyIndicator, Material abortItem) {
        setItems(new ItemStack(setReadyItem, 1), new ItemStack(setNotReadyItem, 1), new ItemStack(readyIndicator, 1), new ItemStack(notReadyIndicator, 1), new ItemStack(abortItem, 1));
    }

    /**
     * Set the item names of the toolbar, will be color translated
     * @param readyText
     * @param notReadyText
     * @param abortText
     * @param readyIndicatorText
     * @param notReadyIndicatorText
     */
    public void setItemNames(String readyText, String notReadyText, String abortText, String readyIndicatorText, String notReadyIndicatorText) {
        this.setReadyText = TextUtil.color(readyText);
        this.setNotReadyText = TextUtil.color(notReadyText);
        this.abortText = TextUtil.color(abortText);
        this.readyIndicatorText = TextUtil.color(readyIndicatorText);
        this.notReadyIndicatorText = TextUtil.color(notReadyIndicatorText);
    }

    public GuiItem[] buildTradeToolBar(boolean ownPartyReady, boolean otherPartyReady, GUI gui) {
        GuiItem[] items = new GuiItem[9];

        ItemMeta meta = setReadyItem.getItemMeta();
        meta.displayName(Component.text(setReadyText));
        setReadyItem.setItemMeta(meta);

        meta = setNotReadyItem.getItemMeta();
        meta.displayName(Component.text(setNotReadyText));
        setNotReadyItem.setItemMeta(meta);

        meta = abortItem.getItemMeta();
        meta.displayName(Component.text(abortText));
        abortItem.setItemMeta(meta);

        meta = notReadyIndicator.getItemMeta();
        meta.displayName(Component.text(notReadyIndicatorText));
        notReadyIndicator.setItemMeta(meta);

        meta = readyIndicator.getItemMeta();
        meta.displayName(Component.text(readyIndicatorText));
        readyIndicator.setItemMeta(meta);

        items[0] = new GuiItem(abortItem);
        items[4] = new GuiItem(ownPartyReady ? setNotReadyItem : setReadyItem);
        for (int i = 1; i < 4; i++) {
            items[i] = new GuiItem(ownPartyReady ? readyIndicator : notReadyIndicator);
        }
        for (int i = 5; i < 9; i++) {
            items[i] = new GuiItem(otherPartyReady ? readyIndicator : notReadyIndicator);
        }

        return items;
    }

}
