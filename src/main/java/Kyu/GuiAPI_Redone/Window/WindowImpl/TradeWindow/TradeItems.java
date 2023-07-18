package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Kyu.GuiAPI_Redone.Item.GuiItem;
import net.kyori.adventure.text.Component;

public class TradeItems {
    
    private List<GuiItem> mainPartyItems = new ArrayList<>(), otherPartyItems = new ArrayList<>();
    private Player mainParty;
    private TradeWindowHolder parentWindow;
    private final int sizeLimit = 5 * 4;

    /**
     * Handles the items of the trade
     * @param parentWindow {@link TradeWindowHolder} parent of the windows
     * @param mainParty the initiator of the trade
     */
    public TradeItems(TradeWindowHolder parentWindow, Player mainParty) {
        this.parentWindow = parentWindow;
        this.mainParty = mainParty;
    }

    /**
     * Adds an item to the trade, should go through the {@link TradeWindowHolder#addTradeItem(Player, ItemStack)} though
     * @param item item to add
     * @param window window to check for open slots
     * @param p the player the item belongs to
     */
    public void addItem(ItemStack item, TradeWindow window, Player p) {
        if (window.getNextFreeSlot() == -1) {
            p.sendMessage(Component.text(parentWindow.getTradeWindowFullError()));
            return;
        }
        GuiItem guiItem = new GuiItem(item);
        guiItem.withListener(e -> {
            if (e.getWhoClicked() instanceof Player && ((Player) e.getWhoClicked()).equals(p)) {
                parentWindow.removeTradeItem(p, guiItem);
            }
        });

        List<GuiItem> list = p.equals(mainParty) ? mainPartyItems : otherPartyItems;
        if (list.size() >= sizeLimit) return;
        list.add(guiItem);
    } 

    /**
     * Removes an item from the trade. Should go through the {@link TradeWindowHolder#removeTradeItem(Player, GuiItem)} route though
     * @param item Item to remove
     * @param p Player it belongs to
     */
    public void removeItem(GuiItem item, Player p) {
        List<GuiItem> list = p.equals(mainParty) ? mainPartyItems : otherPartyItems;
        list.remove(item);
    }

    /**
     * Gets the items the provided player has added to the trade
     * @param p player to get items from
     * @return items the provided player has added to the trade
     */
    public List<GuiItem> getOwnItems(Player p) {
        return p.equals(mainParty) ? mainPartyItems : otherPartyItems;
    }

    /**
     * Gets the items the other party has added to the trade
     * @param p player you <b>don't</b> want the items from
     * @return items the other party has added to the trade
     */
    public List<GuiItem> getOtherItems(Player p) {
        return p.equals(mainParty) ? otherPartyItems : mainPartyItems;
    }

    /**
     * Clears all trade items (does not refresh the windows as this is intended for post-trade cleanup)
     */
    public void clear() {
        otherPartyItems.clear();
        mainPartyItems.clear();
    }
}
