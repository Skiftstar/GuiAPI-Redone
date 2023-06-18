package Kyu.GuiAPI_Redone;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Kyu.GuiAPI_Redone.Window.Window;
import Kyu.GuiAPI_Redone.Window.WindowListener;


public class GUI {

    private Player holder;
    private WindowListener listener;
    private JavaPlugin plugin;
    private Window currentWindow;

    /**
     * Create a new GUI component
     * @param holder The Main Viewer of the Inventory
     * @param plugin Your Plugin
     */
    public GUI(Player holder, JavaPlugin plugin) {
        this.holder = holder;
        this.plugin = plugin;
        //TODO: Check if automatically removed from listeners when gui no longer used
        this.listener = new WindowListener(plugin);
    }

    /**
     * 
     * @param window {@link Window} to open
     */
    public void openWindow(Window window) {
        if (currentWindow != null) {
            currentWindow.setIgnoreCloseEvent(true);
        }
        this.currentWindow = window;
        holder.openInventory(window.getInventory());
    }

    /**
     * 
     * @return The Main Viewer of the Inventory
     */
    public Player getHolder() {
        return holder;
    }

}
