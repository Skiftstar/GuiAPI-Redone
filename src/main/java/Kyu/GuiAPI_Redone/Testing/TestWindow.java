package Kyu.GuiAPI_Redone.Testing;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.WindowImpl.ChestWindow;

public class TestWindow extends ChestWindow {

    private Player player;

    public TestWindow(GUI gui, Player player) {
        super(gui, 6, "Title");
        this.player = player;
        build();
    }

    private void build() {
        setPreventClose(false);

        GuiItem statsItem = new GuiItem(Material.OAK_PLANKS, "Name", 1)
            .withListener(e -> {
                player.setHealth(0);
        });
        setItem(statsItem, 13);
    }   
}
