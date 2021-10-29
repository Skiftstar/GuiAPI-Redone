package Kyu.GuiAPI_Redone;

import Kyu.GuiAPI_Redone.Windows.ChestWindow;
import Kyu.GuiAPI_Redone.Windows.Window;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GUI {

    private Player holder;
    private Window currWindow;
    private List<Window> windows = new ArrayList<>();

    public GUI(Player holder) {
        this.holder = holder;
    }

    public void createChestWindow(String title, int rows) {
        ChestWindow window = new ChestWindow(title, rows, this);
        windows.add(window);
    }

    public void openWindow(Window window) {
        currWindow = window;
        window.open();
    }

}
