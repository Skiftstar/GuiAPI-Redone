# GuiAPI-Redone

This API speeds up and improves the process of creating GUIs for Minecraft plugins.

It is designed with customizeability and expandability in mind, if you don't like the preexisting Windows, you can easily expand upon them or make your own.

## Dependencies

Depends on [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/) if the [TextWindow](https://skiftstar.github.io/GuiAPI-Redone/Kyu/GuiAPI_Redone/Window/WindowImpl/TextWindow/TextWindow.html) is used

## Import via jitpack

[Jitpack](https://jitpack.io/#Skiftstar/GuiAPI-Redone)

## Wiki

The wiki can be found [here](https://github.com/Skiftstar/GuiAPI-Redone/wiki)

## JavaDoc

https://skiftstar.github.io/GuiAPI-Redone/

## Documentation and Exmaples

Please Check the Wiki for this

TextWindow (for UserInput):

```
public class TextWindowTest extends TextWindow {
    
    public TextWindowTest(GUI gui) {
        super(gui);

        //Initial Lines displayed when the sign opens
        setInitalLines("Line 1", "", "Line 3", "Line 4", "Line 5");
        
        //Function that gets executed when the user submits input
        //lines is a List<String> with Size 4
        setOnSubmit(lines -> {
            Bukkit.broadcastMessage(lines.get(0));   
        });
    }

}
```
