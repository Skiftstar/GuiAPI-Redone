package Kyu.GuiAPI_Redone.Window.WindowImpl.MultiWindow;

import java.util.ArrayList;
import java.util.List;

import Kyu.GuiAPI_Redone.GUI;
import Kyu.GuiAPI_Redone.Exceptions.MultiWindowEmptyException;
import Kyu.GuiAPI_Redone.Exceptions.NoPaginationPossibleException;
import Kyu.GuiAPI_Redone.Exceptions.NoSuchPageException;
import Kyu.GuiAPI_Redone.Item.GuiItem;
import Kyu.GuiAPI_Redone.Window.Window;
import Kyu.GuiAPI_Redone.Window.WindowImpl.ChestWindow;

public abstract class MultiWindow {
    
    private List<ChestWindow> windows = new ArrayList<>();
    private int currIndex = 0;
    private GUI gui;
    private PaginationBar paginationBar;

    /**
     * Creates a new MultiWindow
     * @param gui The {@link GUI} the MultiWindow is connected to
     * @param paginationBar The {@link PaginationBar} to use
     */
    public MultiWindow(GUI gui, PaginationBar paginationBar) {
        this.gui = gui;
        this.paginationBar = paginationBar;
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
        gui.openWindow(windows.get(index));
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
     * @return The {@link GUI} the Window is connected to
     */
    public GUI getGui() {
        return gui;
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

    private boolean closeIfNoWindows() {
        if (windows.isEmpty()) {
            gui.getHolder().closeInventory();
            return true;
        }
        return false;
    }

    private void refreshPaginationBar() {
        ChestWindow window = windows.get(currIndex);
        GuiItem[] paginationItems = paginationBar.buildPaginationBar(this, currIndex, windows.size(), getNextWindow(), getPreviousWindow(), gui);

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
        gui.openWindow(window);
    }

}
