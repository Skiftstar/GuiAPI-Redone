package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.WindowImpl.ChestWindow;

public class TradeWindow extends ChestWindow {

    private Player player;
    private boolean ready;
    private TradeWindowHolder parentWindow;

    public TradeWindow(TradeWindowHolder holder, Player player, String title) {
        super(holder.getGui(), 6, title);
        this.player = player;
        this.parentWindow = holder;

        setPreventClose(true);
    }

    public void reloadItems() {
        final Map<Player, List<GuiItem>> items = parentWindow.getTradeItems();
        List<GuiItem> ownItems, otherItems;
        for (Player p : items.keySet()) {
            if (p.equals(this.player)) {
                ownItems = items.get(p);
            } else {
                otherItems = items.get(p);
            }
        }
        //TODO: Set actual items
        
        
    }

    public void setToolbar() {
        //TODO: this
    }

    public void setSpacer() {
        GuiItem spacerItem = new GuiItem(parentWindow.getSpacerItem());
        for (int i = 4; i < 9 * 5; i += 9) {
            setItem(spacerItem, i);
        }
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public int getNextFreeSlot() {
        //TODO: Change logic cuz this shit is not functional
        return parentWindow.getTradeItems().get(player).size() < 5 * 4 ? parentWindow.getTradeItems().get(player).size() : -1;
    }

    public Player getPlayer() {
        return player;
    }
    

}
