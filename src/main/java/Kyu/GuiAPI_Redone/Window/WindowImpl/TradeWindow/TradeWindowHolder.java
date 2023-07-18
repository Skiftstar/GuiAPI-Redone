package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
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
    private String inventoryFullError, tradeCanceledError, tradeWindowFullError;
    private Consumer<Void> onCancel, onFinish;

    /**
     * A new Holder (or Manager) for the Trade between 2 Players
     * @param gui The {@link GUI} the windows belong to
     * @param partner the partner of the trade, the other player will be {@link GUI#getHolder()}
     * @param title Title of both windows, will be color translated
     */
    public TradeWindowHolder(GUI gui, Player partner, String title) {
        this(gui, partner, title, title);
    }

    /**
     * See {@link TradeWindowHolder#TradeWindowHolder(GUI, Player, String)} but this allows seperate window titles
     * @param gui
     * @param partner
     * @param titleMain
     * @param titlePartner
     */
    public TradeWindowHolder(GUI gui, Player partner, String titleMain, String titlePartner) {
        super(gui);
        this.partner = partner;

        titleMain = formatTitle(titleMain, partner);
        titlePartner = formatTitle(titlePartner, gui.getHolder());

        tradeItems = new TradeItems(this, getGui().getHolder());

        mainWindow = new TradeWindow(this, getGui().getHolder(), TextUtil.color(titleMain));
        partnerWindow = new TradeWindow(this, partner, TextUtil.color(titlePartner));

        setToolbar(new TradeToolbar());
        setSpacer(Material.GRAY_STAINED_GLASS_PANE, " ");
        setTradeCanceledError("&4%p &ccanceled the trade!");
        setTradeWindowFullError("&cCan't add any more items to the trade!");
        setInventoryFullError("&4%p's &cinventory is full!");

        listener = new TradeWindowListener(this);
    }

    /**
     * Opens the windows
     */
    public void open() {
        getGui().getHolder().openInventory(mainWindow.getInventory());
        partner.openInventory(partnerWindow.getInventory());
    }

    /**
     * Closes the windows, unregisters the {@link TradeWindowListener}, calls {@link TradeWindowHolder#getOnCancel()} or {@link TradeWindowHolder#getOnFinish()} and handles the items if the trade was canceled
     * @param canceled Whether or not the trade should be seen as canceled
     */
    public void close(boolean canceled) {
        HandlerList.unregisterAll(listener);

        mainWindow.setIgnoreCloseEvent(true);
        partnerWindow.setIgnoreCloseEvent(true);
        getGui().getHolder().closeInventory();
        partner.closeInventory();

        if (canceled) {
            for (GuiItem item : tradeItems.getOwnItems(getGui().getHolder())) {
                getGui().getHolder().getInventory().addItem(item.getItemStack());
            }
            for (GuiItem item : tradeItems.getOwnItems(partner)) {
                partner.getInventory().addItem(item.getItemStack());
            }

            if (getOnCancel() != null) {
                getOnCancel().accept(null);
            } else {
                gui.unregisterListener();
            }
        } else {
            if (getOnFinish() != null) {
                getOnFinish().accept(null);
            } else {
                gui.unregisterListener();
            }
        }
    }

    /**
     * Acts as if the player has canceled the trade, calls {@link TradeWindowHolder#close(boolean)} afterwards
     * @param p The player who canceled the trade
     */
    public void handlePlayerClose(Player p) {
        close(true);
        sendInfo(getTradeCanceledError().replace("%p", ((TextComponent) p.displayName()).content()));
    }

    /**
     * Completes the trade, exchanges items and calls {@link TradeWindowHolder#close(boolean)} afterwards.
     */
    public void finishTrade() {
        List<GuiItem> mainPartyItems = tradeItems.getOwnItems(getGui().getHolder());
        List<GuiItem> otherPartyItems = tradeItems.getOwnItems(partner);
        if (!giveItems(partner, mainPartyItems) || !giveItems(getGui().getHolder(), otherPartyItems)) {
            return;
        }
        tradeItems.clear();
        close(false);
    }

    /**
     * Adds a trade to the items, gives an error to the player if his trade space is full. Automatically calls {@link TradeWindowHolder#forceUnready()}
     * @param p Player that the item is from
     * @param item the item
     */
    public void addTradeItem(Player p, ItemStack item) {
        if (!(p.equals(partner) || p.equals(getGui().getHolder()))) return;
        
        TradeWindow window = p.equals(partner) ? partnerWindow : mainWindow;

        tradeItems.addItem(item, window, p);
        mainWindow.reloadItems();
        partnerWindow.reloadItems();

        p.getInventory().remove(item);
        forceUnready();
    }

    /**
     * Removes an item from the trade and returns it to the players inventory. Automatically calls {@link TradeWindowHolder#forceUnready()}
     * @param p Player that the item belongs to
     * @param item the item
     */
    public void removeTradeItem(Player p, GuiItem item) {
        tradeItems.removeItem(item, p);
        mainWindow.reloadItems();
        partnerWindow.reloadItems();
        p.getInventory().addItem(item.getItemStack());
        forceUnready();
    }

    /**
     * @return the {@link TradeItems} class responsesible for handling the trade items
     */
    public TradeItems getTradeItems() {
        return tradeItems;
    }

    /**
     * Sets a new {@link TradeToolbar}, keep in mind that the toolbar itself has customization as well so you don't need to set a complete new one
     * @param toolbar the toolbar
     */
    public void setToolbar(TradeToolbar toolbar) {
        this.toolbar = toolbar;
        mainWindow.setToolbar();
        partnerWindow.setToolbar();
    }

    /**
     * @return the currently used {@link TradeToolbar}
     */
    public TradeToolbar getToolbar() {
        return toolbar;
    }

    /**
     * @return the item currently used as a spacer between the 2 trade offers
     */
    public ItemStack getSpacerItem() {
        return spacerItem;
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

    /**
     * Returns whether or not the players are ready with the first entry always being if the player supplied is ready, then the other party
     * @param p Player whose ready status should be first in the array
     * @return boolean[] of length 2 with the first element being whether or not he supplied player is ready, then the other party
     */
    public boolean[] getArePartiesReady(Player p) {
        boolean el1 = p.equals(getGui().getHolder()) ? mainPartyReady : otherPartyReady;
        boolean el2 = p.equals(getGui().getHolder()) ? otherPartyReady : mainPartyReady;
        return new boolean[]{el1, el2};
    }

    /**
     * Changes the supplied players ready status, if both parties are ready, the trade will be completed
     * @param p The player to change
     */
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
            finishTrade();
        }
    }

    /**
     * Sets both parties to "not ready"
     */
    public void forceUnready() {
        mainPartyReady = false;
        otherPartyReady = false;

        mainWindow.setToolbar();
        partnerWindow.setToolbar();
    }

    /**
     * Sends a message to both players
     * @param message the message to send
     */
    public void sendInfo(String message) {
        getGui().getHolder().sendMessage(Component.text(message));
        partner.sendMessage(Component.text(message));
    }

    /**
     * The error shown when someone cancels the trade
     * @param tradeCanceledError The error, supports '%p' for the playername who canceled. Will be color translated
     */
    public void setTradeCanceledError(String tradeCanceledError) {
        this.tradeCanceledError = TextUtil.color(tradeCanceledError);
    }

    /**
     * The error shown when someones inventory is full
     * @param inventoryFullError The error, supports '%p' for the playername whose inventory is full. Will be color translated
     */
    public void setInventoryFullError(String inventoryFullError) {
        this.inventoryFullError = TextUtil.color(inventoryFullError);
    }

    /**
     * The error shown when the space for Trade Items is full
     * @param tradeWindowFullError The error. Will be color translated
     */
    public void setTradeWindowFullError(String tradeWindowFullError) {
        this.tradeWindowFullError = TextUtil.color(tradeWindowFullError);
    }

    /**
     * @return The error shown when the space for Trade Items is full
     */
    public String getTradeWindowFullError() {
        return tradeWindowFullError;
    }

    /**
     * @return The error shown when someones inventory is full
     */
    public String getInventoryFullError() {
        return inventoryFullError;
    }
    
    /**
     * @return The error shown when someone cancels the trade
     */
    public String getTradeCanceledError() {
        return tradeCanceledError;
    }

    /**
     * Set a function that shall be executed when the trade has been canceled. This will prevent the listeners from being unregistered automatically, so you have to call {@link GUI#unregisterListener()} yourself to clean up 
     * @param onCancel The function to call
     */
    public void setOnCancel(Consumer<Void> onCancel) {
        this.onCancel = onCancel;
    }

    /**
     * Set a function that shall be executed when the trade has been completed. This will prevent the listeners from being unregistered automatically, so you have to call {@link GUI#unregisterListener()} yourself to clean up 
     * @param onFinish the function to call
     */
    public void setOnFinish(Consumer<Void> onFinish) {
        this.onFinish = onFinish;
    }

    /**
     * @return Function called when the trade has been canceled
     */
    public Consumer<Void> getOnCancel() {
        return onCancel;
    }

    /**
     * @return Function called when the trade has been completed
     */
    public Consumer<Void> getOnFinish() {
        return onFinish;
    }

    private boolean giveItems(Player p, List<GuiItem> items) {
        int freeSlots = 4 * 9;
        for (ItemStack is : p.getInventory().getContents()) {
            if (is != null && is.getType() != Material.AIR) {
                freeSlots--;
            }
        }

        if (freeSlots < items.size()) {
            sendInfo(getInventoryFullError().replace("%p", ((TextComponent) p.displayName()).content()));
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
