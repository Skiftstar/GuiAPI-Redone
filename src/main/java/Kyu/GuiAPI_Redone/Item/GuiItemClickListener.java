package Kyu.GuiAPI_Redone.Item;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface GuiItemClickListener {

    /**
     * The event handler that should be executed when an {@link GuiItem} is clicked.
     * This is intended to implemented by lambda when you create an {@link GuiItem}.
     *
     * @param event The Bukkit/Spigot API's {@link InventoryClickEvent}.
     */
    void onClick(InventoryClickEvent event);

}