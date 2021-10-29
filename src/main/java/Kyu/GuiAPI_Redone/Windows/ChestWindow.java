package Kyu.GuiAPI_Redone.Windows;

import Kyu.GuiAPI_Redone.GUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ChestWindow extends DefaultWindow{

    /**
     * Creates a new Default (Chest) Window
     * @param title Nullable - Title of the Window
     * @param rows Amount of rows. Throws Exception if out of bounds
     * @param gui GUI the window is part of
     * @param plugin Your Plugin
     */
    public ChestWindow(@Nullable String title, int rows, GUI gui, JavaPlugin plugin) {
        super(title, rows, gui, plugin);
    }
}
