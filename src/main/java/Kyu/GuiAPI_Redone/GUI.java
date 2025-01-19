package Kyu.GuiAPI_Redone;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Kyu.GuiAPI_Redone.Window.Openable;
import Kyu.GuiAPI_Redone.Window.WindowListener;


public class GUI {

    private Player holder;
    private WindowListener listener;
    private JavaPlugin plugin;
    private Openable currentWindow;

    /**
     * Create a new GUI component
     * @param holder The Main Viewer of the Inventory
     * @param plugin Your Plugin
     */
    public GUI(Player holder, JavaPlugin plugin) {
        this.holder = holder;
        this.plugin = plugin;
        this.listener = new WindowListener(plugin, this);
    }

    /**
     * 
     * @param window {@link Openable} to open
     */
    public void openWindow(Openable window) {
        // Cache ignoreCloseEvent, because otherwise it will always be true if changing between windows
        Openable oldWindow = currentWindow;
        boolean cachedIgnoreCloseEvent = oldWindow == null ? false : oldWindow.isIgnoreCloseEvent();
        if (currentWindow != null) {
            currentWindow.setIgnoreCloseEvent(true);
        }
        this.currentWindow = window;
        window.open();

        // Set cached value for previous Window
        if (oldWindow != null) {
            oldWindow.setIgnoreCloseEvent(cachedIgnoreCloseEvent);
        }
    }

        /**
     * 
     * Closes current Windows and unregisters Listener
     */
    public void closeCurrent() {
        boolean cachedIgnoreCloseEvent = currentWindow == null ? false : currentWindow.isIgnoreCloseEvent();
        if (currentWindow != null) {
            currentWindow.setIgnoreCloseEvent(true);
            holder.closeInventory();
            currentWindow.setIgnoreCloseEvent(cachedIgnoreCloseEvent);
            unregisterListener();
        }
    }

    /**
     * Unregisters the Window Listener, not recommended to call while a window is still open, as this breaks most of the functionality
     */
    public void unregisterListener() {
        listener.unregister();
    }

    /**
     * 
     * @return The Main Viewer of the Inventory
     */
    public Player getHolder() {
        return holder;
    }

    /**
     * 
     * @return The plugin the GUI is connected to
     */
    public JavaPlugin getPlugin() {
        return plugin;
    }

}
