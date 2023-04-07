package Kyu.GuiAPI_Redone;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class GUI {

    private Player holder;
    private JavaPlugin plugin;

    public GUI(Player holder, JavaPlugin plugin) {
        this.holder = holder;
        this.plugin = plugin;
    }

}
