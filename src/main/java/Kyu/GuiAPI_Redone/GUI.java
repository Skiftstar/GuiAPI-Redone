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
    private Window currentWindow;

    /**
     * Create a new GUI component
     * @param holder The Main Viewer of the Inventory
     * @param plugin Your Plugin
     */
    public GUI(Player holder, JavaPlugin plugin) {
        this.holder = holder;
        this.plugin = plugin;
        this.listener = new WindowListener();
    }

    /**
     * 
     * @param window {@link Window} to open
     */
    public void openWindow(Window window) {
        currentWindow.setIgnoreCloseEvent(true);
        this.currentWindow = window;
        holder.openInventory(window.getInventory());
    }

    /**
     * 
     * @return The Main Viewer of the Inventory
     */
    public Player getHolder() {
        return holder;
    }

    /**
     * Set the default toolbar Items for pages with {@link Window#isMultiPage()} set to True
     * @param placeholderItem Material of the Placeholder Item
     * @param pageItem Material of the Item to change Pages
     */
    public void setToolbarItems(Material placeholderItem, Material pageItem) {
        this.placeholderItem = placeholderItem;
        this.pageItem = pageItem;
    }

    /**
     * Set the default toolbar Item Names for pages with {@link Window#isMultiPage()} set to True
     * <p>
     * Will be color translated
     * @param placeholderText Text for the Placeholder items
     * @param pageForwText Text for the NextPage Item, supports <b>%page</b> for Number of the next Page and <b>%max</b> for the maximum number of pages
     * @param pageBackText Text for the PreviousPage Item, supports <b>%page</b> for Number of the previous Page and <b>%max</b> for the maximum number of pages
     */
    public void setToolbarTexts(String placeholderText, String pageForwText, String pageBackText) {
        this.placeholderText = placeholderText;
        this.pageForwText = pageForwText;
        this.pageBackText = pageBackText;
    }

    /**
     * 
     * @return The Item Name for the placeholder Item
     */
    public String getPlaceholderText() {
        return placeholderText;
    }

    /**
     * 
     * @return The Item Name for the next Page Item
     */
    public String getPageForwText() {
        return pageForwText;
    }
    
    /**
     * 
     * @return The Item Name for the previous Page Item
     */
    public String getPageBackText() {
        return pageBackText;
    }

    /**
     * 
     * @return The Material for the placeholder Item
     */
    public Material getPlaceholderItem() {
        return placeholderItem;
    }

    /**
     * 
     * @return The Material for the PageChange Items
     */
    public Material getPageItem() {
        return pageItem;
    }

}
