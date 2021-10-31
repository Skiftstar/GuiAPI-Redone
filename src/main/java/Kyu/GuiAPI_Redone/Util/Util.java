package Kyu.GuiAPI_Redone.Util;

import net.minecraft.server.level.EntityPlayer;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Util {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static EntityPlayer getNMSPlayer(Player player){
        return ((CraftPlayer) player).getHandle();
    }
}
