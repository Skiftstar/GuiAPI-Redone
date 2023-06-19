package Kyu.GuiAPI_Redone.Testing;

import org.bukkit.Bukkit;
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
        
        GuiItem statsItem = new GuiItem(Material.OAK_PLANKS, "Name", 1);
        statsItem.withListener(e -> {
                statsItem.setName("test");
        });

        //FIXME: Listener on the new Window isn't working
        setOnClose(e -> {
            getGui().openWindow(new TestWindow(getGui(), player));
        });

        setItem(statsItem, 13);
    }   
}
