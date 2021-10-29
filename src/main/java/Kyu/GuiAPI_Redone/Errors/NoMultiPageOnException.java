package Kyu.GuiAPI_Redone.Errors;

public class NoMultiPageOnException extends RuntimeException {

    public NoMultiPageOnException() {
        super("You have to turn on multiPages before using this!");
    }

}
