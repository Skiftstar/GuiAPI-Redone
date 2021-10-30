package Kyu.GuiAPI_Redone.KyuTesting;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Main;
import Kyu.GuiAPI_Redone.Windows.ChestWindow;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class KyuTestCommand implements CommandExecutor {

    private Main plugin;

    public KyuTestCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("kTest").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        GUI gui = new GUI(p, plugin);
        ChestWindow window = gui.createChestWindow("test", 6);
        window.setMultiPage(true);
        GuiItem item = window.setItem(Material.FEATHER, "&cAdd Page", 15);
        item.setOnClick(e -> {
            window.addPage();
        });
        GuiItem item2 = window.setItem(Material.REDSTONE, "&aremove second to last Page", 14);
        item2.setOnClick(e -> {
            window.removePage(window.getPages().size() - 1);
        });
        GuiItem item3 = window.setItem(Material.REDSTONE_TORCH, "&aremove last Page", 13);
        item3.setOnClick(e -> {
            window.removePage(window.getPages().size());
        });
        window.open();
        return true;
    }
}
