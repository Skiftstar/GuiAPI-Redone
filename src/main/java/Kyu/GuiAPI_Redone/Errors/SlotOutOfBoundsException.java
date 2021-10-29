package Kyu.GuiAPI_Redone.Errors;

public class SlotOutOfBoundsException extends RuntimeException{

    public SlotOutOfBoundsException(int slot, int max) {
        super("Slot is out of bounds!\nProvided: " + slot + "\nMaximum: " + max);
    }

}
