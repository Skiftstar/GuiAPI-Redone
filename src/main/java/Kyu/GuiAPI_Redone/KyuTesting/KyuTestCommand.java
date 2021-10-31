package Kyu.GuiAPI_Redone.KyuTesting;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Main;
import Kyu.GuiAPI_Redone.Windows.ChestWindow;
import Kyu.GuiAPI_Redone.Windows.TaskbarStyles;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import javax.annotation.Nonnull;

import java.util.concurrent.atomic.AtomicInteger;

public class KyuTestCommand implements CommandExecutor {

    private Main plugin;

    public KyuTestCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("kTest").setExecutor(this);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Player p = (Player) sender;
        GUI gui = new GUI(p, plugin);
        ChestWindow window = gui.createChestWindow("test", 6);
        window.setMultiPage(true);
        window.setTaskBarEnabled(true);
        window.setTaskbarStyle(TaskbarStyles.MIDDLE);
        window.setPagePlaceholders(Material.PAPER, "next", "back");
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
        GuiItem item4 = window.setItem(Material.WRITABLE_BOOK, "&aSet Item at Page 12", 12);
        item4.setOnClick(e -> {
            window.setItemAtPage(Material.GRAY_STAINED_GLASS_PANE, " ", 20, 12, true);
        });
        GuiItem item5 = window.setItem(Material.END_ROD, "&aAdd Wool", 11);
        AtomicInteger i = new AtomicInteger();
        item5.setOnClick(e -> {
            window.addItem(Material.BLUE_WOOL, "&b" + i);
            i.getAndIncrement();
        });
        GuiItem item6 = window.setItem(Material.BARRIER, "&cRemove Empty Pages", 10);
        item6.setOnClick(e -> {
            window.removeEmptyPages();
        });
        window.open();
        return true;
    }
}
