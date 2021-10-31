package Kyu.GuiAPI_Redone.Windows;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ChestWindow extends DefaultWindow implements Listener {

    /**
     * Creates a new Default (Chest) Window
     * @param title Nullable - Title of the Window
     * @param rows Amount of rows. Throws Exception if out of bounds
     * @param gui GUI the window is part of
     * @param plugin Your Plugin
     */
    public ChestWindow(@Nullable String title, int rows, GUI gui, JavaPlugin plugin) {
        super(title, rows, gui, plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    /*
    =============================================================================

                                Event Handler

    =============================================================================
     */

    @EventHandler
    private void onInvClick(InventoryClickEvent e) {
        handleInvClick(e);
    }
}
