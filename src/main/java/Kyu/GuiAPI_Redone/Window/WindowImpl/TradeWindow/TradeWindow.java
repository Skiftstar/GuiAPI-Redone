package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import org.bukkit.entity.Player;

import Kyu.GuiAPI_Redone.Window.WindowImpl.ChestWindow;

public class TradeWindow extends ChestWindow {

    private Player player;
    private TradeToolbar toolbar;

    public TradeWindow(TradeWindowHolder holder, Player player, int rows, String title) {
        super(holder.getGui(), rows, title);
        this.player = player;
        this.toolbar = new TradeToolbar();

        setPreventClose(true);
    }

    
    

}
