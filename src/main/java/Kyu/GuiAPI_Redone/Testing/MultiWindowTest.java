package Kyu.GuiAPI_Redone.Testing;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Item.PaginationBar;
import Kyu.GuiAPI_Redone.Window.MultiWindow;
import Kyu.GuiAPI_Redone.Window.Windows.ChestWindow;

public class MultiWindowTest extends MultiWindow {

    private Player player;

    public MultiWindowTest(GUI gui, Player player) {
        super(gui, new PaginationBar());
        getPaginationBar().setToolbarItems(Material.WHITE_STAINED_GLASS_PANE, Material.OAK_SIGN);
        this.player = player;
        build();
    }

    private void build() {
        GuiItem addPageItem = new GuiItem(Material.DIAMOND, "New Page", 1)
            .withListener(e -> {
                ChestWindow window = new TestWindow(getGui(), player);
                addWindow(window);
            });

        GuiItem removePageItem = new GuiItem(Material.DIAMOND, "Remove Page", 1)
            .withListener(e -> {
                removeWindow(getWindows().size() - 1);
            });

        ChestWindow window = new TestWindow(getGui(), player);
        window.setItem(removePageItem, 1);
        window.setItem(addPageItem, 0);
        addWindow(window);
    }   
}
