package Kyu.GuiAPI_Redone.Window;

import java.util.function.Consumer;

import org.bukkit.event.inventory.InventoryCloseEvent;

import Kyu.GuiAPI_Redone.GUI;

public abstract class Openable {
    
    protected Consumer<InventoryCloseEvent> onClose;
    protected boolean disableClickEvent = true, isIgnoreCloseEvent = false, preventClose = true;
    protected GUI gui;

    public Openable(GUI gui) {
        this.gui = gui;
    }

    public abstract void open();

    /**
     * Whether or not to cancel the OnInventoryClick event, preventing users from taking/placing items from the inventory
     * @return whether disableClickEvent is on or off
     */
    public boolean isDisableClickEvent() {
        return disableClickEvent;
    }

    /**
     * Set whether or not InventoryClickEvent is canceled or not. Canceling prevents users from taking/placing items from the inventory
     * @param disableClickEvent Whether or not to cancel InventoryClickEvent
     */
    public void setDisableClickEvent(boolean disableClickEvent) {
        this.disableClickEvent = disableClickEvent;
    }

    /**
     *
     * @return Whether or not the user can close the window with ESC
     */
    public boolean isPreventClose() {
        return preventClose;
    }

    /**
     * 
     * @param preventClose Whether or not the user shall be able to close the window with ESC
     */
    public void setPreventClose(boolean preventClose) {
        this.preventClose = preventClose;
    }

    /**
     * 
     * @return Whether or not the close Event will be ignored. If true, the onClose Consumer will not be called
     */
    public boolean isIgnoreCloseEvent() {
        return isIgnoreCloseEvent;
    }

    /**
     * 
     * @param isIgnoreCloseEvent Whether or not the close Event will be ignored. If true, the onClose Consumer will not be called
     * <b>Keep in mind that setting this to true will also result in the {@link WindowListener} not being unregistered on Inventory closed, so you will have to handle this manually</b>
     */
    public void setIgnoreCloseEvent(boolean isIgnoreCloseEvent) {
        this.isIgnoreCloseEvent = isIgnoreCloseEvent;
    }

    /**
     * 
     * @return Function to call when the inventory gets closed and {@link Openable#isIgnoreCloseEvent()} is False. Or Null if no function is set
     */
    public Consumer<InventoryCloseEvent> getOnClose() {
        return onClose;
    }

    /**
     * 
     * @param onClose Function to call when the inventory gets closed and {@link Openable#isIgnoreCloseEvent()} is False.
     * Automatically disables {@link Openable#isPreventClose()}
     */
    public void setOnClose(Consumer<InventoryCloseEvent> onClose) {
        this.preventClose = false;
        this.onClose = onClose;
    }

    /**
     * @return The {@link GUI} the Window is connected to
     */
    public GUI getGui() {
        return gui;
    }

}
