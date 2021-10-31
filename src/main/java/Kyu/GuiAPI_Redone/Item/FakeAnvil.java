package Kyu.GuiAPI_Redone.Item;

import Kyu.GuiAPI_Redone.Util.Util;
import net.minecraft.network.chat.ChatMessage;
import net.minecraft.network.protocol.game.PacketPlayOutOpenWindow;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.ContainerAnvil;
import net.minecraft.world.inventory.Containers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.AnvilInventory;

public class FakeAnvil extends ContainerAnvil {

    int cost;
    int id;
    EntityPlayer player;
    String title;

    public FakeAnvil(Player player, int id, String title) {
        super(id, Util.getNMSPlayer(player).getInventory());
        this.id = id;
        this.player = Util.getNMSPlayer(player);
        this.title = title;
        cost = 0;
    }

    public AnvilInventory getInv(){
        return (AnvilInventory) getBukkitView().getTopInventory();
    }

    void open(){
        player.b.sendPacket(new PacketPlayOutOpenWindow(id, Containers.h, new ChatMessage(Util.color(title))));
    }

    void setTitle(String title){
        super.setTitle(new ChatMessage(Util.color(title)));
    }


}
