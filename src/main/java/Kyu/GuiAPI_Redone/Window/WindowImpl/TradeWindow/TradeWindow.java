package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.WindowImpl.ChestWindow;

public class TradeWindow extends ChestWindow {

    private Player player;
    private boolean ready;
    private List<GuiItem> tradeItems = new ArrayList<>();

    public TradeWindow(TradeWindowHolder holder, Player player, int rows, String title) {
        super(holder.getGui(), rows, title);
        this.player = player;

        setPreventClose(true);
    }

    public void reloadItems() {

    }

    public void setToolbar() {
        
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public int getNextFreeSlot() {
        return super.getNextFreeSlot();
    }
    

}
