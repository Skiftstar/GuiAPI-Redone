package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class TradeWindowListener implements Listener {
    
    private TradeWindowHolder inventory;

    public TradeWindowListener(TradeWindowHolder inventory) {
        this.inventory = inventory;
        inventory.getGui().getPlugin().getServer().getPluginManager().registerEvents(this, inventory.getGui().getPlugin());
    }

    private boolean shouldIgnoreCloseEvent(Inventory inventory) {
        return (inventory == null ||
                 inventory.getHolder() == null ||
                 !(inventory.getHolder() instanceof TradeWindowHolder) ||
                 !(((TradeWindowHolder) inventory).equals(this.inventory)));
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent e) {
        if (shouldIgnoreCloseEvent(e.getInventory())) return;

        inventory.onPlayerClose(e);
    }


}
