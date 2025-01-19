package Kyu.GuiAPI_Redone.Window;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;

/**
 * Listens for all Events connected to {@link Window}s
 */
public class WindowListener implements Listener {

    private JavaPlugin plugin;
    private GUI connectedGUI;

    public WindowListener(JavaPlugin plugin, GUI gui) {
        this.plugin = plugin;
        this.connectedGUI = gui;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    private void onInventoryClose(InventoryCloseEvent e) {
        if (shouldIgnoreInventoryEvent(e.getInventory())) return;

        Window closedWindow = (Window) e.getInventory().getHolder();

        if (closedWindow.isIgnoreCloseEvent()) {
            return;
        }

        if (closedWindow.isPreventClose()) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                e.getPlayer().openInventory(closedWindow.getInventory());
            });
            return;
        }
        
        if (closedWindow.unregisterOnClose) {
            unregister();
            return;
        }

        if (closedWindow.getOnClose() == null) {
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            closedWindow.getOnClose().accept(e);
        });
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent e) {
        if (shouldIgnoreInventoryEvent(e.getClickedInventory())) return;

        Window clickedWindow = (Window) e.getClickedInventory().getHolder();

        if (clickedWindow.isDisableClickEvent()) {
            e.setCancelled(true);
        }

        if (!clickedWindow.getGui().equals(connectedGUI)) {
            e.setCancelled(true);
            return;
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
