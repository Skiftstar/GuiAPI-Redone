package Kyu.GuiAPI_Redone.Window.WindowImpl.TradeWindow;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TradeWindowListener implements Listener {
    
    private TradeWindowHolder tradeHolder;

    public TradeWindowListener(TradeWindowHolder tradeHolder) {
        this.tradeHolder = tradeHolder;
        tradeHolder.getGui().getPlugin().getServer().getPluginManager().registerEvents(this, tradeHolder.getGui().getPlugin());
    }

    @EventHandler
    private void onPlayerInvClick(InventoryClickEvent e) {
        Inventory clickedInv = e.getClickedInventory();

        if (clickedInv == null || clickedInv.getHolder() == null) return;
        if (!(e.getWhoClicked() instanceof Player)) return;
        if (!(clickedInv instanceof PlayerInventory)) return;
        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
        
        e.setCancelled(true);

        ItemStack clickedItem = e.getCurrentItem();
        Player p = (Player) e.getWhoClicked();
        tradeHolder.addTradeItem(p, clickedItem);
    }
}
