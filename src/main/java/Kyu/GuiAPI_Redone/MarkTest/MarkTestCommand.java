package Kyu.GuiAPI_Redone.MarkTest;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Main;
import Kyu.GuiAPI_Redone.Windows.AnvilWindow;
import Kyu.GuiAPI_Redone.Windows.ChestWindow;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MarkTestCommand implements CommandExecutor {

    private Main plugin;

    public MarkTestCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("mTest").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        /*Player p = (Player) commandSender;
        AnvilWindow window = new AnvilWindow("depression", p, "more depression", plugin);
        window.setItem(Material.BARRIER, "test", 0);
        window.open();*/

        Player p = (Player) commandSender;
        GUI gui = new GUI(p, plugin);
        ChestWindow window = gui.createChestWindow("test", 6);
        window.setMultiPage(true);
        GuiItem item = window.setItem(Material.FEATHER, "&cAdd Page", 15);
        item.setLore("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        item.setOnClick(e -> {
            window.addPage();
        });
        window.open();
        return false;
    }

}
