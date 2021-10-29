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
        GuiItem item = window.setItem(Material.FEATHER, "&ctest", 15);
        item.setOnClick(e -> {
            e.getWhoClicked().sendMessage(Component.text("Moin."));
        });
        GuiItem item2 = window.setItem(Material.REDSTONE, "&aremove Item", 14);
        item2.setOnClick(e -> {
            window.removeItem(15);
        });
        window.open();
        return true;
    }
}
