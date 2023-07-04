package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import org.bukkit.entity.Player;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.TextUtil;
import Kyu.GuiAPI_Redone.Window.Openable;
import net.kyori.adventure.text.TextComponent;

public class TradeWindowHolder extends Openable {

    private TradeWindow mainWindow, partnerWindow;
    private Player partner;
    private GUI gui;

    public TradeWindowHolder(GUI gui, Player partner, String title) {
        this(gui, partner, title, title);
    }

    public TradeWindowHolder(GUI gui, Player partner, String titleMain, String titlePartner) {
        super(gui);
        titleMain = formatTitle(titleMain, partner);
        titlePartner = formatTitle(titlePartner, gui.getHolder());

        
    }

    public void open() {

    }

    public void close() {
        
    }

    public GUI getGui() {
        return gui;
    }

    private String formatTitle(String in, Player oppositePlayer) {
        return TextUtil.color(in.replace("%player", ((TextComponent) oppositePlayer.displayName()).content()));
    }
    
}
