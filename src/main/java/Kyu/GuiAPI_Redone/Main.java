package Kyu.GuiAPI_Redone;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.Window;
import net.kyori.adventure.text.Component;

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

        Window window = new Window(gui, 6, "&bTitle");

        GuiItem item = new GuiItem(new ItemStack(Material.GLASS_PANE)).withListener(e -> {
            p.sendMessage(Component.text("abc"));
        });
        window.addItem(item);

        gui.openWindow(window);
        return true;
    }
}
