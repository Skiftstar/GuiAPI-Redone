package Kyu.GuiAPI_Redone.Window.WindowImpl.TextWindow.API;

import java.util.List;
import org.bukkit.entity.Player;

public final class SignCompletedEvent {

    private final Player player;
    private final List<String> lines;

    public SignCompletedEvent(Player player, List<String> lines) {
        this.player = player;
        this.lines = lines;
    }

    public Player getPlayer() {
        return player;
    }

    public List<String> getLines() {
        return lines;
    }
}