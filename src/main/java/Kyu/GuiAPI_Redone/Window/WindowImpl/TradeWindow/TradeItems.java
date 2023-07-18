package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import Kyu.GuiAPI_Redone.Item.GuiItem;

public class TradeItems {
    
    private List<GuiItem> mainPartyItems = new ArrayList<>(), otherPartyItems = new ArrayList<>();
    private Player mainParty;
    private final int sizeLimit = 5 * 4;

    public TradeItems(Player mainParty) {
        this.mainParty = mainParty;
    }

    public void addItem(GuiItem im, Player p) {
        List<GuiItem> list = p.equals(mainParty) ? mainPartyItems : otherPartyItems;
        if (list.size() >= sizeLimit) return;
        list.add(im);
    } 

    public void removeItem(GuiItem item, Player p) {
        List<GuiItem> list = p.equals(mainParty) ? mainPartyItems : otherPartyItems;
        list.remove(item);
    }

    public List<GuiItem> getOwnItems(Player p) {
        return p.equals(mainParty) ? mainPartyItems : otherPartyItems;
    }

    public List<GuiItem> getOtherItems(Player p) {
        return p.equals(mainParty) ? otherPartyItems : mainPartyItems;
    }

    public void clear() {
        otherPartyItems.clear();
        mainPartyItems.clear();
    }
}
