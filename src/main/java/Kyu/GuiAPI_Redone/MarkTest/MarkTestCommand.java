package Kyu.GuiAPI_Redone.MarkTest;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Main;
import Kyu.GuiAPI_Redone.Util.Util;
import Kyu.GuiAPI_Redone.Windows.AnvilWindow;
import Kyu.GuiAPI_Redone.Windows.ChestWindow;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MarkTestCommand implements CommandExecutor {

    private Main plugin;

    public MarkTestCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getCommand("mTest").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        /*Player p = (Player) commandSender;
        AnvilWindow window = new AnvilWindow("depression", p, "more depression", plugin);
        window.setItem(Material.BARRIER, "test", 0).setLore(20,);
        window.open();*/

        Player p = (Player) commandSender;
        GUI gui = new GUI(p, plugin);
        ChestWindow window = gui.createChestWindow("test", 6);
        window.setMultiPage(true);
        GuiItem item = window.setItem(Material.FEATHER, "&cAdd Page", 15);


        //item.setLore("Lorem Ipsum/n is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
        item.setLoreRedo(40, "&c&lContrary to popular belief, Lorem Ipsum is &a&onot simply random text. &c&l&n&mIt has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.");

        item.setOnClick(e -> {
            window.addPage();
        });
        window.open();
        return false;
    }

    public static void reformat(int max, String s){

        String patternStr = "\n ";
        String[] paras = Pattern.compile(patternStr, Pattern.MULTILINE).split(s);
        ArrayList<String> lores = new ArrayList<>();

        String lastColorCharacter;

        for(String string : paras){
            int length = string.length();
            int temp = 0;

            for(int i = 0; i < length - max; i = temp){
                int y = i + max;

                if(y >= length){
                    y = length - 1;
                }

                boolean reached = false;

                while(!Character.isWhitespace(string.charAt(y))){
                    y -= 1;
                    if(temp + y < i + max - (max/4)){
                        y = i + max;
                        reached = true;
                        break;
                    }
                }

                System.out.println(temp + "  :  " + y);

                String subS = string.substring(temp, y);
                temp = y + 1; //Remove the space
                if(reached){
                    subS = subS + "-";
                    temp = y; //No space so we don't need to remove the first char
                }

                lores.add(Util.color(subS));

                String lastColors = ChatColor.getLastColors(subS);
            }
            lores.add(Util.color(string.substring(temp)));
        }


        for(String st : lores){
            Bukkit.broadcastMessage(st);
        }
    }

}
