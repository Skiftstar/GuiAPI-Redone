package Kyu.GuiAPI_Redone.Exceptions;


public class NoSuchPageException extends RuntimeException {

    public NoSuchPageException() {
        super("The provided Page Index is out of bounds!");
    }
    
}
