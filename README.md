# GuiAPI-Redone

This API speeds up and improves the process of creating GUIs for Minecraft plugins.

It is designed with customizeability and expandability in mind, if you don't like the preexisting Windows, you can easily expand upon them or make your own.

## Dependencies

depends on [ProtocolLib](https://www.spigotmc.org/resources/protocollib.1997/)

## Import via jitpack

[Jitpack](https://jitpack.io/#Skiftstar/GuiAPI-Redone)

## JavaDoc

https://skiftstar.github.io/GuiAPI-Redone/

## Documentation

Creating a new Window can be accomplished by extending one of the Window Classes

All preexisting Window Types can be found in the Window.WindowImpl Package (see [here](https://skiftstar.github.io/GuiAPI-Redone/Kyu/GuiAPI_Redone/Window/WindowImpl/package-summary.html))

For example a simple single Window:
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

Or an exmaple for a MultiWindow would be the following:
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
