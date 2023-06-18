package Kyu.GuiAPI_Redone.Exceptions;


public class MultiWindowEmptyException extends RuntimeException {

    public MultiWindowEmptyException() {
        super("Tried opening a MultiWindow with 0 Windows");
    }
    
}
