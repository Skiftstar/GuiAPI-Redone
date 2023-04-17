package Kyu.GuiAPI_Redone.Window;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import Kyu.GuiAPI_Redone.Item.GuiItem;

public class WindowListener implements Listener {

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (shouldIgnoreInventoryEvent(e.getClickedInventory())) return;

        Window clickedWindow = (Window) e.getClickedInventory().getHolder();

        if (clickedWindow.isDisableClickEvent()) {
            e.setCancelled(true);
        }

        int slot = e.getSlot();
        
        GuiItem item = clickedWindow.getItem(slot);
        if (item == null || item.getListener() == null) {
            return;
        }

        item.getListener().onClick(e);
    }

    private static boolean shouldIgnoreInventoryEvent(Inventory inventory) {
        return (inventory == null ||
                 inventory.getHolder() == null ||
                 !(inventory.getHolder() instanceof Window));
    }
    
}
