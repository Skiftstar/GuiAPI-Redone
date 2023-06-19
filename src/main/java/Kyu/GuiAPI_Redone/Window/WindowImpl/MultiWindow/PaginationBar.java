package Kyu.GuiAPI_Redone.Window.WindowImpl.MultiWindow;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.TextUtil;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.Window;
import net.kyori.adventure.text.Component;

public class PaginationBar {

    private String placeholderText = " ", pageForwText = "To Page %page/%max", pageBackText = "To Page %page/%max";
    private ItemStack placeholderItem, pageItemForw, pageItemBackw;

    public PaginationBar() {
        buildDefaults();
    }

    /**
     * Set the default toolbar Items for the Windows
     * @param placeholderItem ItemStack of the Placeholder Item
     * @param pageItemForw ItemStack of the Item to go to the next page
     * @param pageItemBackw ItemStack of the Item to go to the previous page
     * @return
     */
    public PaginationBar setToolbarItems(ItemStack placeholderItem, ItemStack pageItemForw, ItemStack pageItemBackw) {
        this.placeholderItem = placeholderItem;
        this.pageItemForw = pageItemForw;
        this.pageItemBackw = pageItemBackw;
        return this;
    }

    /**
     * Set the default toolbar Items for the Windows
     * @param placeholderItem Material of the Placeholder Item
     * @param pageItemForw Material of the Item to go to the next page
     * @param pageItemBackw Material of the Item to go to the previous page
     * @return
     */
     public PaginationBar setToolbarItems(Material placeholderItem, Material pageItemForw, Material pageItemBackw) {
        ItemStack placeholder = new ItemStack(placeholderItem);
        ItemStack pageItemstackForw = new ItemStack(pageItemForw);
        ItemStack pageItemstackBackw = new ItemStack(pageItemBackw);
        return setToolbarItems(placeholder, pageItemstackForw, pageItemstackBackw);
    }

    /**
     * Set the default toolbar Items for the Windows
     * @param placeholderItem Material of the Placeholder Item
     * @param pageItem Material of the Item to change Pages
     */
    public PaginationBar setToolbarItems(Material placeholderItem, Material pageItem) {
        return setToolbarItems(placeholderItem, pageItem, pageItem);
    }

    /**
     * Set the default toolbar Item Names for the Windows
     * Names will be color translated
     * @param placeholderText Text for the Placeholder items
     * @param pageForwText Text for the NextPage Item, supports <b>%page</b> for Number of the next Page and <b>%max</b> for the maximum number of pages
     * @param pageBackText Text for the PreviousPage Item, supports <b>%page</b> for Number of the previous Page and <b>%max</b> for the maximum number of pages
     */
    public PaginationBar setToolbarTexts(String placeholderText, String pageForwText, String pageBackText) {
        this.placeholderText = placeholderText;
        this.pageForwText = pageForwText;
        this.pageBackText = pageBackText;
        return this;
    }

    public GuiItem[] buildPaginationBar(MultiWindow multiWindow, int windowIndex, int maxPages, Window nextPage, Window previosPage, GUI gui) {
        GuiItem[] items = new GuiItem[9];
        int pageNum = windowIndex + 1;

        ItemMeta meta = placeholderItem.getItemMeta();
        meta.displayName(Component.text(TextUtil.color(getPlaceholderText())));
        placeholderItem.setItemMeta(meta);

        meta = pageItemForw.getItemMeta();
        meta.displayName(Component.text(TextUtil.color(getPageForwText()
            .replace("%page", "" + (pageNum + 1))
            .replace("%max", "" + maxPages))));
        pageItemForw.setItemMeta(meta);

        meta = pageItemBackw.getItemMeta();
        meta.displayName(Component.text(TextUtil.color(getPageBackText()
            .replace("%page", "" + (pageNum - 1))
            .replace("%max", "" + maxPages))));
        pageItemBackw.setItemMeta(meta);

        GuiItem placeholder = new GuiItem(placeholderItem);
        GuiItem pageForw = new GuiItem(pageItemForw).withListener(e -> {
            multiWindow.openPage(windowIndex + 1);
        });
        GuiItem pageBack = new GuiItem(pageItemBackw).withListener(e -> {
            multiWindow.openPage(windowIndex - 1);
        });

        for (int i = 0; i < 9; i++) {
            items[i] = placeholder;
        }

        if (nextPage != null) {
            items[6] = pageForw;
        }

        if (previosPage != null) {
            items[2] = pageBack;
        }

        return items;
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
     * @return The ItemStack for the placeholder Item
     */
    public ItemStack getPlaceholderItem() {
        return placeholderItem;
    }

    /**
     * 
     * @return The ItemStack for the Page forward Item
     */
    public ItemStack getPageForwardItem() {
        return pageItemForw;
    }

    /**
     * 
     * @return The ItemStack for the Page backward Item
     */
    public ItemStack getPageBackwardItem() {
        return pageItemBackw;
    }
    
    private void buildDefaults() {
        setToolbarItems(Material.GRAY_STAINED_GLASS_PANE, Material.ARROW);
    }
}
