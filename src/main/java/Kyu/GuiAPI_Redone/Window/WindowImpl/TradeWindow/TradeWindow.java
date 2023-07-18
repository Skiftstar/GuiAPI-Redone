package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import java.util.List;

import org.bukkit.entity.Player;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.WindowImpl.ChestWindow;

public class TradeWindow extends ChestWindow {

    private Player player;
    private TradeWindowHolder parentWindow;

    /**
     * Creates a new Windows able to display the trades
     * @param holder {@link TradeWindowHolder} which controls the trade
     * @param player the Player that will view this window
     * @param title the title of the window, will be color translated
     */
    public TradeWindow(TradeWindowHolder holder, Player player, String title) {
        super(holder.getGui(), 6, title);
        this.player = player;
        this.parentWindow = holder;

        setPreventClose(true);
    }

    /**
     * Reloads all the Items in the inventory, as well as sets the toolbar and the spacer again
     */
    public void reloadItems() {
        List<GuiItem> ownItems = parentWindow.getTradeItems().getOwnItems(player);
        List<GuiItem> otherItems = parentWindow.getTradeItems().getOtherItems(player);

        clearItems();

        for (int i = 0; i < ownItems.size(); i++) {
            int slot = Math.floorDiv(i, 4) * 9 + i % 4;
            setItem(ownItems.get(i), slot);
        }

        for (int i = 0; i < otherItems.size(); i++) {
            int slot = (Math.floorDiv(i, 4) * 9 + i % 4) + 5;
            setItem(otherItems.get(i), slot);
        }

        setToolbar();
        setSpacer();
    }

    /**
     * Sets the toolbar at the bottom of the window again
     */
    public void setToolbar() {
        TradeToolbar toolbar = parentWindow.getToolbar();
        boolean[] partiesReady = parentWindow.getArePartiesReady(player);
        GuiItem[] toolbarItems = toolbar.buildTradeToolBar(parentWindow, player, partiesReady[0], partiesReady[1], getGui());
        for (int i = 0; i < toolbarItems.length; i++) {
            GuiItem item = toolbarItems[i];
            int slot = 5 * 9 + i;
            setItem(item, slot);
        }
    }

    /**
     * Sets the spacer inbetween the trade offers
     */
    public void setSpacer() {
        GuiItem spacerItem = new GuiItem(parentWindow.getSpacerItem());
        for (int i = 4; i < 9 * 5; i += 9) {
            setItem(spacerItem, i);
        }
    }

    /**
     * Gets the next free slot on the left hand side of the window
     */
    public int getNextFreeSlot() {
        List<GuiItem> items = parentWindow.getTradeItems().getOwnItems(player);
        int nextFreeSlot = ((Math.floorDiv(items.size(), 4) * 9) + (items.size() % 4));
        return items.size() < 5 * 4 ? nextFreeSlot : -1;
    }

    /**
     * @return The Player that views this inventory
     */
    public Player getPlayer() {
        return player;
    }
    

}
