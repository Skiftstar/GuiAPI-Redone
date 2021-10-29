package Kyu.GuiAPI_Redone;

import Kyu.GuiAPI_Redone.Windows.ChestWindow;
import Kyu.GuiAPI_Redone.Windows.Window;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class GUI {

    private Player holder;
    private Window currWindow;
    private List<Window> windows = new ArrayList<>();
    private JavaPlugin plugin;

    public GUI(Player holder, JavaPlugin plugin) {
        this.holder = holder;
        this.plugin = plugin;
    }

    public ChestWindow createChestWindow(String title, int rows) {
        ChestWindow window = new ChestWindow(title, rows, this, plugin);
        windows.add(window);
        return window;
    }

    public void openWindow(Window window) {
        currWindow = window;
        window.open();
    }

    public Player getHolder() {
        return holder;
    }
}
