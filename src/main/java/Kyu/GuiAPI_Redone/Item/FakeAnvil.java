package Kyu.GuiAPI_Redone.Item;

import Kyu.GuiAPI_Redone.Util.Util;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.inventory.ContainerAnvil;
import org.bukkit.entity.Player;

public class FakeAnvil extends ContainerAnvil {

    int cost;

    public FakeAnvil(Player player, String title) {
        super(Util.getNMSPlayer(player).nextContainerCounter(), Util.getNMSPlayer(player).getInventory());
        cost = 0;
    }


}
