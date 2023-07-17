package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.TextUtil;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.Openable;
import net.kyori.adventure.text.TextComponent;

public class TradeWindowHolder extends Openable {

    private TradeWindow mainWindow, partnerWindow;
    private Player partner;
    private TradeToolbar toolbar = new TradeToolbar();
    private ItemStack spacerItem = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    private String spacerName = " ";
    private GUI gui;

    public TradeWindowHolder(GUI gui, Player partner, String title) {
        this(gui, partner, title, title);
    }

    public TradeWindowHolder(GUI gui, Player partner, String titleMain, String titlePartner) {
        super(gui);
        titleMain = formatTitle(titleMain, partner);
        titlePartner = formatTitle(titlePartner, gui.getHolder());

        mainWindow = new TradeWindow(this, getGui().getHolder(), 6, TextUtil.color(titleMain));
        partnerWindow = new TradeWindow(this, partner, 6, TextUtil.color(titlePartner));
    }

    public void open() {

    }

    public void close() {
        
    }

    public void addTradeItem(Player p, ItemStack item) {
        if (!(p.equals(partner) || p.equals(getGui().getHolder()))) return;
        
        TradeWindow window = p.equals(partner) ? partnerWindow : mainWindow;

        if (window.getNextFreeSlot() == -1) {
            //TODO: throw changeable error
            return;
        }
        GuiItem guiItem = new GuiItem(item);
        guiItem.withListener(e -> {
            removeTradeItem(guiItem);
        });
        window.addItem(guiItem);
        mainWindow.reloadItems();
        partnerWindow.reloadItems();
    }

    public void removeTradeItem(GuiItem item) {
        int slot = 
    }

    public void removeTradeItem(int slot) {

    }

    public List<GuiItem> getTradeItems() {
        return tradeItems;
    }

    public void toggleReady(Player p) {
        if (!(p.equals(partner) || p.equals(getGui().getHolder()))) return;
    }

    public void onPlayerClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player)) return;

        Player p = (Player) e.getPlayer();

        if (!(p.equals(partner) || p.equals(getGui().getHolder()))) return;

        if (!isPreventClose()) {
            close();
            return;
        }

        if (p.equals(partner)) {
            partnerWindow.open();
        } else {
            mainWindow.open();
        }
    }

    /**
     * Set the spacer separating the two sides of the trade window
     * @param item item of the spacer
     * @param name name of the spacer, will be color translated
     */
    public void setSpacer(ItemStack item, String name) {
        this.spacerItem = item;
        this.spacerName = TextUtil.color(name);
    }

    /**
     * See {@link TradeWindowHolder#setSpacer(ItemStack, String)}
     * @param mat
     * @param name
     */
    public void setSpacer(Material mat, String name) {
        this.setSpacer(new ItemStack(mat, 1), name);
    }

    public void setToolbar(TradeToolbar toolbar) {
        this.toolbar = toolbar;
    }

    public TradeToolbar getToolbar() {
        return toolbar;
    }

    public GUI getGui() {
        return gui;
    }

    private String formatTitle(String in, Player oppositePlayer) {
        return TextUtil.color(in.replace("%player", ((TextComponent) oppositePlayer.displayName()).content()));
    }
    
}
