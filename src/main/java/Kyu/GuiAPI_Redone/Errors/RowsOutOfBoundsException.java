package Kyu.GuiAPI_Redone.Errors;

public class RowsOutOfBoundsException extends RuntimeException {

    public RowsOutOfBoundsException() {
        super("Number of rows can't be lower than 1 or higher than 6!");
    }

}
