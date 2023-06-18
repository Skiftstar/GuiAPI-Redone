package Kyu.GuiAPI_Redone;

import net.md_5.bungee.api.ChatColor;

public class TextUtil {
    
    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

}
