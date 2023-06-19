package Kyu.GuiAPI_Redone.Testing;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Kyu.GuiAPI_Redone.GUI;

/*
 * FOR TESTING ONLY
 */
public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("test").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        GUI gui = new GUI(p, this);

        TestWindow window = new TestWindow(gui, p);
        
        gui.openWindow(window);

        return true;
    }
}
