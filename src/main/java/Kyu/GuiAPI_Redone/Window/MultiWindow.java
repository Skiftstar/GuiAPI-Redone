package Kyu.GuiAPI_Redone.Window;

import java.util.ArrayList;
import java.util.List;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Exceptions.MultiWindowEmptyException;
import Kyu.GuiAPI_Redone.Exceptions.NoPaginationPossibleException;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Item.Toolbar;
import Kyu.GuiAPI_Redone.Window.Windows.ChestWindow;

public abstract class MultiWindow {
    
    private List<ChestWindow> windows = new ArrayList<>();
    private int currIndex = 0;
    private GUI gui;
    private Toolbar toolbar;

    /**
     * Creates a new MultiWindow
     * @param gui The {@link GUI} the MultiWindow is connected to
     * @param toolbar The {@link Toolbar} to use
     */
    public MultiWindow(GUI gui, Toolbar toolbar) {
        this.gui = gui;
        this.toolbar = toolbar;
    }

    /**
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

        gui.openWindow(windows.get(currIndex));
    }

    /**
     * @return The next {@link Window} or null if no next window
     */
    public Window getNextWindow() {
        return windows.size() - 1 < currIndex ? windows.get(currIndex + 1) : null;
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
        refreshToolbars();
        return this;
    }

    /**
     * Removes a {@link Window} and closes the inventory if no more windows are present
     * @param window The window to remove
     */
    public void removeWindow(ChestWindow window) {
        int removedIndex = windows.indexOf(window);
        windows.remove(window);
        refreshToolbars();
        if (!closeIfNoWindows()) {
            if (removedIndex == currIndex) {
                handleCloseCurrentWindow();
            }
        }
    }

    /**
     * Removes a {@link Window} and closes the inventory if no more windows are present
     * @param index The index of the window to remove
     */
    public void removeWindow(int index) {
        windows.remove(index);
        refreshToolbars();
        if (!closeIfNoWindows()) {
            if (index == currIndex) {
                handleCloseCurrentWindow();
            }
        }
    }

    /**
     * @return The {@link GUI} the Window is connected to
     */
    public GUI getGui() {
        return gui;
    }

    /**
     * @return The Toolbar for the {@link Window}s
     */
    public Toolbar getToolbar() {
        return toolbar;
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

    private boolean closeIfNoWindows() {
        if (windows.isEmpty()) {
            gui.getHolder().closeInventory();
            return true;
        }
        return false;
    }

    private void refreshToolbars() {
        for (int index = 0; index < windows.size(); index++) {
            ChestWindow window = windows.get(index);
            GuiItem[] toolbarItems = toolbar.buildToolbar(index, windows.size(), getPreviousWindow(), getNextWindow(), gui);

            int firstSlotOfLastRow = (window.getRows() - 1) * 9;
            for (int itemIndex = 0; itemIndex < toolbarItems.length; itemIndex++) {
                window.setItem(toolbarItems[itemIndex], firstSlotOfLastRow + itemIndex);
            }
        }
    }

    private void handleCloseCurrentWindow() {
        Window window;
        if (currIndex == 0) {
            window = windows.get(++currIndex);
        } else {
            window = windows.get(--currIndex);
        }
        gui.openWindow(window);
    }

}
