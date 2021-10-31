package Kyu.GuiAPI_Redone.Windows;

public enum TaskbarStyles {
    LEFT(0, 1),
    RIGHT(7, 8),
    BOTH(0, 8),
    MIDDLE(3, 5);

    private final int prev;
    private final int next;

    TaskbarStyles(int prev, int next) {
        this.next = next;
        this.prev = prev;
    }

    public int prev() {
        return prev;
    }

    public int next() {
        return next;
    }

}