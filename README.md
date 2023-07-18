# GuiAPI-Redone

This API speeds up and improves the process of creating GUIs for Minecraft plugins.

It is designed with customizeability and expandability in mind, if you don't like the preexisting Windows, you can easily expand upon them or make your own.

## Dependencies

Depends on [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)

## Import via jitpack

[Jitpack](https://jitpack.io/#Skiftstar/GuiAPI-Redone)

## Wiki

The wiki can be found [here](https://github.com/Skiftstar/GuiAPI-Redone/wiki)

## JavaDoc

https://skiftstar.github.io/GuiAPI-Redone/

## Documentation

Creating a new Window can be accomplished by extending one of the Window Classes

All preexisting Window Types can be found in the Window.WindowImpl Package (see [here](https://skiftstar.github.io/GuiAPI-Redone/Kyu/GuiAPI_Redone/Window/WindowImpl/package-summary.html))

## Examples 

Single Window:

```
public class TestWindow extends ChestWindow {

    public TestWindow(GUI gui, Player player) {
        super(gui, 6, "Title");

        //You could set Settings here
        setPreventClose(false);

        build();
    }

    //You don't need a seperate build function, but it's good to split things up
    private void build() {
        
        //Create a new Item
        GuiItem item = new GuiItem(Material.OAK_PLANKS, "Name", 1)
            //Listener when player clicks on the item
            .withListener(e -> {
                getGui().getHolder().setHealth(0);
            });

        //Another item
        GuiItem renameItem = new GuiItem(Material.OAK_PLANKS, "Name", 1);
        renameItem.withListener(e -> {
                //You can also change the items' properties
                renameItem.setName("Test");
            });

        //Add items to window
        setItem(item, 13);
        setItem(renameItem, 15);
    }   
}
```

MultiWindow:

```
public class MultiWindowTest extends MultiWindow {

    public MultiWindowTest(GUI gui, Player player) {
        super(gui, new PaginationBar());

        //You could change settings here in the constructor
        getPaginationBar().setToolbarItems(Material.WHITE_STAINED_GLASS_PANE, Material.OAK_SIGN);
        build();
    }

    //You don't need a seperate build function, but it's good to split things up
    private void build() {
        //Item to add a Page
        GuiItem addPageItem = new GuiItem(Material.DIAMOND, "New Page", 1)
            .withListener(e -> {
                ChestWindow window = new TestWindow(getGui(), getGui().getHolder());
                addWindow(window);
            });
        
        //Item to remove a page
        GuiItem removePageItem = new GuiItem(Material.DIAMOND, "Remove Page", 1)
            .withListener(e -> {
                //We don't need to make checks if for example the last Window gets closed, the API handles all of it
                removeWindow(getWindows().size() - 1);
            });

        //Inital Window
        ChestWindow window = new TestWindow(getGui(), getGui().getHolder());
        addWindow(window);

        //add Items
        window.setItem(removePageItem, 1);
        window.setItem(addPageItem, 0);
    }   
}
```

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
