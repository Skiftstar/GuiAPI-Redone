package Kyu.GuiAPI_Redone.Window.WindowImpl.MultiWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.event.inventory.InventoryCloseEvent;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Exceptions.MultiWindowEmptyException;
import Kyu.GuiAPI_Redone.Exceptions.NoPaginationPossibleException;
import Kyu.GuiAPI_Redone.Exceptions.NoSuchPageException;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.Openable;
import Kyu.GuiAPI_Redone.Window.Window;
import Kyu.GuiAPI_Redone.Window.WindowListener;
import Kyu.GuiAPI_Redone.Window.WindowImpl.ChestWindow;

public abstract class MultiWindow extends Openable {
    
    private List<ChestWindow> windows = new ArrayList<>();
    private int currIndex = 0;
    private PaginationBar paginationBar;

    /**
     * Creates a new MultiWindow
     * @param gui The {@link GUI} the MultiWindow is connected to
     * @param paginationBar The {@link PaginationBar} to use
     */
    public MultiWindow(GUI gui, PaginationBar paginationBar) {
        super(gui);
        this.paginationBar = paginationBar;
    }

    /** 
     * Calling directly can result in {@link GUI} not setting the current Window correctly!
     * Use {@link GUI#openWindow(Kyu.GuiAPI_Redone.Window.Openable)} instead
     * Opens the Window, either on index 0 if first open or the window it was previously on if opened again
     * @throws MultiWindowEmptyException If the MultiWindow has not had any {@link Window}s added yet
     */
    public void open() {
        if (windows.isEmpty()) {
            throw new MultiWindowEmptyException();
        }

        if (currIndex > windows.size()) {
            currIndex = windows.size() - 1;
        }

        getGui().getHolder().openInventory(windows.get(currIndex).getInventory());
    }

    /**
     * Opens a specific Page
     * @throws NoSuchPageException If the index is out of bounds
     * @param index The index of the Page to open (starts at 0)
     */
    public void openPage(int index) {
        if (index < 0 || index > windows.size() - 1) {
            throw new NoSuchPageException();
        }

        currIndex = index;
        refreshPaginationBar();
        getGui().openWindow(windows.get(index));
    }

    /**
     * @return The next {@link Window} or null if no next window
     */
    public Window getNextWindow() {
        return windows.size() - 1 > currIndex ? windows.get(currIndex + 1) : null;
    }

    /**
     * @return The previous {@link Window} or null if no previous window
     */
    public Window getPreviousWindow() {
        return currIndex > 0 ? windows.get(currIndex - 1) : null;
    }

    /**
     * 
     * @param window The {@link Window} to add
     * @param indexArg Optional argument, the index of where to add the window
     * @throws NoPaginationPossibleException If the provided inventory has only 1 Row
     * @return
     */
    public MultiWindow addWindow(ChestWindow window, int... indexArg) {
        if (window.getRows() == 1) {
            throw new NoPaginationPossibleException();
        }

        if (indexArg.length > 0) {
            int index = indexArg[0];
            windows.add(index, window);
        } else {
            windows.add(window);
            
        }
        refreshPaginationBar();
        return this;
    }

    /**
     * Removes a {@link Window} and closes the inventory if no more windows are present
     * @param window The window to remove
     */
    public void removeWindow(ChestWindow window) {
        int removedIndex = windows.indexOf(window);
        removeWindow(removedIndex);
    }

    /**
     * Removes a {@link Window} and closes the inventory if no more windows are present
     * @param index The index of the window to remove
     */
    public void removeWindow(int index) {
        windows.remove(index);
        if (!closeIfNoWindows()) {
            if (index == currIndex) {
                handleCloseCurrentWindow();
            } else {
                refreshPaginationBar();
            }
        }
    }

    /**
     * @return The {@link PaginationBar} for the {@link Window}s
     */
    public PaginationBar getPaginationBar() {
        return paginationBar;
    }

    /**
     * @return The {@link Window}s in the MultiWindow 
     */
    public List<ChestWindow> getWindows() {
        return windows;
    }

    /**
     * @return The index of the currently opened {@link Window}
     */
    public int getCurrIndex() {
        return currIndex;
    }

    /**
     * 
     * @param isIgnoreCloseEvent Whether or not the close Event will be ignored. If true, the onClose Consumer will not be called
     * <p>
     * Does not do anything for the MultiWindow, but sets the flag for <b>ALL WINDOWS</b> contained in this MultiWindow
     * <b>Keep in mind that setting this to true will also result in the {@link WindowListener} not being unregistered on Inventory closed, so you will have to handle this manually</b>
     */
    public void setIgnoreCloseEvent(boolean isIgnoreCloseEvent) {
        super.setIgnoreCloseEvent(isIgnoreCloseEvent);
        for (Window window : windows) {
            window.setIgnoreCloseEvent(isIgnoreCloseEvent);
        }
    }

    /**
     * 
     * @param onClose Function to call when the inventory gets closed and {@link Openable#isIgnoreCloseEvent()} is False.
     * Automatically disables {@link Openable#isPreventClose}
     * <p>
     * Does not do anything for the MultiWindow, but sets the onClose Function for <b>ALL WINDOWS</b> container in this MultiWindow
     */
    public void setOnClose(Consumer<InventoryCloseEvent> onClose) {
        super.setOnClose(onClose);
        for (Window window : windows) {
            window.setOnClose(onClose);
        }
    }

    /**
     * 
     * @param preventClose Whether or not the user shall be able to close the window with ESC
     * <p>
     * Does not do anything for the MultiWindow, but sets the flag for <b>ALL WINDOWS</b> contained in this MultiWindow
     */
    public void setPreventClose(boolean preventClose) {
        super.setPreventClose(preventClose);
        for (Window window : windows) {
            window.setPreventClose(preventClose);
        }
    }

    /**
     * Set whether or not InventoryClickEvent is canceled or not. Canceling prevents users from taking/placing items from the inventory
     * <p>
     * Does not do anything for the MultiWindow, but sets the flag for <b>ALL WINDOWS</b> contained in this MultiWindow
     * @param disableClickEvent Whether or not to cancel InventoryClickEvent
     */
    public void setDisableClickEvent(boolean disableClickEvent) {
        super.setDisableClickEvent(disableClickEvent);
        for (Window window : windows) {
            window.setDisableClickEvent(disableClickEvent);
        }
    }


    private boolean closeIfNoWindows() {
        if (windows.isEmpty()) {
            getGui().getHolder().closeInventory();
            return true;
        }
        return false;
    }

    private void refreshPaginationBar() {
        ChestWindow window = windows.get(currIndex);
        GuiItem[] paginationItems = paginationBar.buildPaginationBar(this, currIndex, windows.size(), getNextWindow(), getPreviousWindow(), getGui());

        int firstSlotOfLastRow = (window.getRows() - 1) * 9;
        for (int itemIndex = 0; itemIndex < paginationItems.length; itemIndex++) {
            window.setItem(paginationItems[itemIndex], firstSlotOfLastRow + itemIndex);
        }
    }

    private void handleCloseCurrentWindow() {
        Window window;
        if (currIndex == 0) {
            window = windows.get(++currIndex);
        } else {
            window = windows.get(--currIndex);
        }
        refreshPaginationBar();
        getGui().openWindow(window);
    }

}
