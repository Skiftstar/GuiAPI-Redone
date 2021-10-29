package Kyu.GuiAPI_Redone;

import Kyu.GuiAPI_Redone.KyuTesting.KyuTestCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        new KyuTestCommand(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
