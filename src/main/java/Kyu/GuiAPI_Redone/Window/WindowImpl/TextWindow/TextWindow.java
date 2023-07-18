package Kyu.GuiAPI_Redone.Window.WindowImpl.TextWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Window.Openable;
import Kyu.GuiAPI_Redone.Window.WindowImpl.TextWindow.API.SignGUIAPI;

public abstract class TextWindow extends Openable {
    
    private List<String> initalLines = new ArrayList<>(Arrays.asList("", "", "", ""));
    private Consumer<List<String>> onSubmit = null;

    public TextWindow(GUI gui) {
        super(gui);
        
    }

    /**
     * Calling directly can result in {@link GUI} not setting the current Window correctly!
     * Use {@link GUI#openWindow(Kyu.GuiAPI_Redone.Window.Openable)} instead
     */
    public void open() {
        Player p = getGui().getHolder();
        SignGUIAPI.builder()
                .action(event -> {
                    if (onSubmit == null) return;
                    onSubmit.accept(event.getLines()); 
                })
                .withLines(initalLines)
                .uuid(p.getUniqueId())
                .plugin(getGui().getPlugin())
                .build()
                .open();

    }

    /**
     * Set the inital content displayed when the sign opens
     * @param lines Lines of the sign, anything above 4 lines will be cut
     */
    public void setInitalLines(String... lines) {
        List<String> initalLines = new ArrayList<>(Arrays.asList(lines));
        if (initalLines.size() < 4) {
            for (int i = initalLines.size(); i < 4; i++) {
                initalLines.add("");
            }
        }
        else if (initalLines.size() > 4) {
            initalLines = initalLines.subList(0, 4);
        }
        this.initalLines = initalLines;
    }

    /**
     * 
     * @return The function executed once the player submits the input
     */
    public Consumer<List<String>> getOnSubmit() {
        return onSubmit;
    }

    /**
     * Set the function to execute once the player submits the input
     * @param onSubmit Function to execute once input is submitted
     */
    public void setOnSubmit(Consumer<List<String>> onSubmit) {
        this.onSubmit = onSubmit;
    }

    /**
     * Disabled for this window
     */
    public void setDisableClickEvent(boolean disableClickEvent) {}

    /**
     * Disabled for this window, Closing the sign just calls {@link TextWindow#getOnSubmit()} 
     */
    public void setOnClose(Consumer<InventoryCloseEvent> onClose) {}

}
