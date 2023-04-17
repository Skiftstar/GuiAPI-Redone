package Kyu.GuiAPI_Redone;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import Kyu.GuiAPI_Redone.Window.Window;
import Kyu.GuiAPI_Redone.Window.WindowListener;

public class GUI {

    private Player holder;
    private WindowListener listener;
    private JavaPlugin plugin;
    private String placeholderText = " ", pageForwText = "To Page %page/%max", pageBackText = "To Page %page/%max";
    private Material placeholderItem = Material.GRAY_STAINED_GLASS_PANE, pageItem = Material.ARROW;

    public GUI(Player holder, JavaPlugin plugin) {
        this.holder = holder;
        this.plugin = plugin;
        this.listener = new WindowListener();
    }

    public void openWindow(Window window) {
        //TODO: this
    }

    public Player getHolder() {
        return holder;
    }

    public void setToolbarItems(Material placeholderItem, Material pageItem) {
        this.placeholderItem = placeholderItem;
        this.pageItem = pageItem;
    }

    public void setToolbarTexts(String placeholderText, String pageForwText, String pageBackText) {
        this.placeholderText = placeholderText;
        this.pageForwText = pageForwText;
        this.pageBackText = pageBackText;
    }

    public String getPlaceholderText() {
        return placeholderText;
    }

    public String getPageForwText() {
        return pageForwText;
    }
    
    public String getPageBackText() {
        return pageBackText;
    }

    public Material getPlaceholderItem() {
        return placeholderItem;
    }

    public Material getPageItem() {
        return pageItem;
    }

}
