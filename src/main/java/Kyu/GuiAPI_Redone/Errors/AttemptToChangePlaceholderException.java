package Kyu.GuiAPI_Redone.Errors;

public class AttemptToChangePlaceholderException extends RuntimeException {

    public AttemptToChangePlaceholderException() {
        super("You cannot edit/replace a placeholder.");
    }

}
