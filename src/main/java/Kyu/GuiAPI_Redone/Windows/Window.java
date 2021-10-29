package Kyu.GuiAPI_Redone.Windows;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Window {

    void open();

    void close();

    GuiItem setItem(ItemStack item, int slot);

    GuiItem setItem(Material itemType, @Nullable String name, int slot);

    void removeItem(int slot);

    ItemStack getItem(int slot);

    GuiItem getGuiItem(int slot);

    String getTitle();

    Player getHolder();

    GUI getGUI();

    void refreshWindow();

}
