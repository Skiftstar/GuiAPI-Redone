package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.TextUtil;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.Openable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

public class TradeWindowHolder extends Openable {

    private TradeWindow mainWindow, partnerWindow;
    private Player partner;
    private TradeWindowListener listener;
    private TradeItems tradeItems;
    private boolean mainPartyReady = false, otherPartyReady = false;
    private TradeToolbar toolbar;
    private ItemStack spacerItem;
    private String spacerName;

    public TradeWindowHolder(GUI gui, Player partner, String title) {
        this(gui, partner, title, title);
    }

    public TradeWindowHolder(GUI gui, Player partner, String titleMain, String titlePartner) {
        super(gui);
        this.partner = partner;

        titleMain = formatTitle(titleMain, partner);
        titlePartner = formatTitle(titlePartner, gui.getHolder());

        tradeItems = new TradeItems(getGui().getHolder());

        mainWindow = new TradeWindow(this, getGui().getHolder(), TextUtil.color(titleMain));
        partnerWindow = new TradeWindow(this, partner, TextUtil.color(titlePartner));

        setToolbar(new TradeToolbar());
        setSpacer(Material.GRAY_STAINED_GLASS_PANE, " ");

        listener = new TradeWindowListener(this);
    }

    public void open() {
        getGui().getHolder().openInventory(mainWindow.getInventory());
        partner.openInventory(partnerWindow.getInventory());
    }

    public void close() {
        //TODO:: unregister Listener, add back all items, etc.
        HandlerList.unregisterAll(listener);

        for (GuiItem item : tradeItems.getOwnItems(getGui().getHolder())) {
            getGui().getHolder().getInventory().addItem(item.getItemStack());
        }
        for (GuiItem item : tradeItems.getOwnItems(partner)) {
            partner.getInventory().addItem(item.getItemStack());
        }

        setIgnoreCloseEvent(true);
        getGui().getHolder().closeInventory();
        partner.closeInventory();
    }

    public void handlePlayerClose(Player p) {
        close();
        //TODO: Send Message to other player
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
            if (e.getWhoClicked() instanceof Player && ((Player) e.getWhoClicked()).equals(p)) {
                removeTradeItem(p, guiItem);
            }
        });

        tradeItems.addItem(guiItem, p);
        mainWindow.reloadItems();
        partnerWindow.reloadItems();

        p.getInventory().remove(item);
        forceUnready();
    }

    public void removeTradeItem(Player p, GuiItem item) {
        tradeItems.removeItem(item, p);
        mainWindow.reloadItems();
        partnerWindow.reloadItems();
        p.getInventory().addItem(item.getItemStack());
        forceUnready();
    }

    public TradeItems getTradeItems() {
        return tradeItems;
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
        this.spacerName = TextUtil.color(name);

        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(spacerName));
        item.setItemMeta(meta);

        this.spacerItem = item;

        mainWindow.setSpacer();
        partnerWindow.setSpacer();
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
        mainWindow.setToolbar();
        partnerWindow.setToolbar();
    }

    public TradeToolbar getToolbar() {
        return toolbar;
    }

    public ItemStack getSpacerItem() {
        return spacerItem;
    }

    public boolean[] getArePartiesReady(Player p) {
        boolean el1 = p.equals(getGui().getHolder()) ? mainPartyReady : otherPartyReady;
        boolean el2 = p.equals(getGui().getHolder()) ? otherPartyReady : mainPartyReady;
        return new boolean[]{el1, el2};
    }

    public void toggleReady(Player p) {
        if (p.equals(getGui().getHolder())) {
            mainPartyReady = !mainPartyReady;
        } else if (p.equals(partner)) {
            otherPartyReady = !otherPartyReady;
        } else {
            return;
        }

        mainWindow.setToolbar();
        partnerWindow.setToolbar();

        if (mainPartyReady && otherPartyReady) {
            //TODO: Add Delay with sound ig
            finishTrade();
        }
    }

    public void forceUnready() {
        mainPartyReady = false;
        otherPartyReady = false;

        mainWindow.setToolbar();
        partnerWindow.setToolbar();
    }

    public void finishTrade() {
        List<GuiItem> mainPartyItems = tradeItems.getOwnItems(getGui().getHolder());
        if (!giveItems(partner, mainPartyItems)) {
            //TODO: Not enough space error
            return;
        }
        List<GuiItem> otherPartyItems = tradeItems.getOwnItems(partner);
        if (!giveItems(getGui().getHolder(), otherPartyItems)) {
            //TODO: Not enough space error
            return;
        }
        tradeItems.clear();
        close();
    }

    private boolean giveItems(Player p, List<GuiItem> items) {
        //TODO: fix this counter, it does weird shit
        int freeSlots = 0;
        Bukkit.broadcastMessage("" + p.getInventory().getContents().length);
        for(ItemStack item : p.getInventory().getContents()){
            Bukkit.broadcastMessage("test" + item.getType().toString());
            if (item.getType().equals(Material.AIR)){
                    freeSlots++;
            }
        }

        if (freeSlots < items.size()) {
            return false;
        }

        for (GuiItem item : items) {
            p.getInventory().addItem(item.getItemStack());
        }
        return true;
    }

    private String formatTitle(String in, Player oppositePlayer) {
        return TextUtil.color(in.replace("%player", ((TextComponent) oppositePlayer.displayName()).content()));
    }
    
}
