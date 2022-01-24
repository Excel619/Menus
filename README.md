# Menus API By Excel

Menus runs on the Bukkit API for Minecraft and allows developers to create custom GUI menus with ease, and does nothing on its own. It is written in Kotlin and Gradle, and is compatible with most versions of Minecraft. The API is still compatible with java-based projects despite not being written in java.

This project is currently unfinished and still has some critical issues preventing it from being complete.


## Installation

Download the a jar from the Releases section and add it to your build path. For maven and gradle, the group-id is `com.gmail.excel8392.menus` and artifact-id is `Menus`. If you are using kotlin in your project, beware that the jar has the kotlin runtime dependencies bundled into it, which is known to cause issues with some IDEs (Intellij). The current temporary solution is to unzip the jar, delete the kotlin dependencies and zip it again.

To bundle the dependency into your plugin, use [Maven Shade](https://maven.apache.org/plugins/maven-shade-plugin/) or [Gradle Shadow](https://github.com/johnrengelman/shadow).

## Usage


### Getting started
In the `onEnable` block for your plugin, create a new `Menus` object and pass in your plugin instance as the only parameter. Save this object for use later to create and open menus.
<small>Note that you cannot create more than one of these objects with a single plugin instance.</small>

### Creating a basic menu
When creating a menu, use your `Menus` singleton for creating a **MenuBuilder** like so:
```java
MenuBuilder myMenuBuilder = menus.basicMenuBuilder("My Custom Menu", 27); // Menu title and size
```
You can now use your menu builder to modify the menu's items and more.  For example, setting items in the menu GUI can either be done by adding an ItemStack (`myMenuBuilder.setItem(slot, itemStack)`), or by using this API's custom menu item builder like so:
```java
MenuItemBuilder myItemBuilder = new MenuItemBuilder(Material.DIAMOND, 1, "&6Color Coded Name", "&7Optional vararg lore"); // material, amount, name, lore
myMenuBuilder.setItem(slot, myItemBuilder.build());
```
Adding an on-click action to menu items is very simple, you must use the MenuItemBuilder:
```java
myItemBuilder.addAction(new CloseMenuAction()); // Refer to "on-click menu item actions" for more options
```
Finally, to open the menu for a player, use
```java
myMenuBuilder().build().openMenu(player);
```
You will find that the BasicMenuBuilder, MenuItemBuilder, PagedMenuBuilder, and MenuAnimationsBuilder all have more functions that allow you to customize your menu however you see fit. For example, you can add a menu border by using `myMenuBuilder.addBorder()`.

 In addition, all functions with text parameters will use the commonly accepted Bukkit color code character, the ampersand.

### On-click menu item actions
To set a custom action to be performed when clicking on an icon in a custom menu, use a `MenuItemBuilder` like so:
```java
myItemBuilder.addAction(new CustomMenuAction((event) -> {
    // Custom code ran on click, use the consumer's InventoryClickevent.
}); 
```
There are four options for determined on click actions: 
- `ChangePageAction` for changing pages in paged menus,`ChangePageAction.NEXT_PAGE`
- `CloseMenuAction` for closing closing the current menu, `new CloseMenuAction()`
- `OpenMenuAction` for opening a new menu, `new OpenMenuAction(() -> menu)`
- `CustomMenuAction` for handling the click with a custom action, `new CustomMenuAction((event) -> {})`

<small>A new menu action for playing a sound and displaying text will be coming a future version</small>

Multiple actions can be added to a single menu item. Menu actions other than custom ones should not be added to menu items that have interactions blocked set to false.

### Paged menus
Paged menus can also be made with ease by using Menus. A variation of the basic menu builder exists for this:
```java
PagedMenuBuilder myPagedMenuBuilder = menus.pagedMenuBuilder("My Paged Menu", 27); // Title, size
```
First, it is important to recognize that many of the properties set for paged menus (like title, size) are simply defaults for all pages in the menu. That doesn't mean that each page can have a different title however, it just needs to be set manually. One primary setting that **is** menu-wide is the interactions blocked. <small>*Note that this is planned to change in future versions</small>

When setting items in a paged menu using `setItem`, it will default to setting these items **across all pages**, and in any pages created in the future. To set items for a specific page, specify the page number like so: `setItem(slot, pageNumber, item)`. **Page numbers are counted starting from zero, like an array**.

To have pages of different sizes or titles, you can use `setPageSize(pageNumber, newSize)` and `setPageTitle(title, pageNumberOne, pageNumberTwo)`. <small>The order of arguments for setPageSize will change in a future version to allow a vararg of pages to be modified.</small>

To modify the number of pages in the menu, use `setMenuLength(length)` or `addPage(amount, size)`. New pages will be created with the menu-wide items previously set.

One common example for a usage of the PagedMenuBuilder would be to create arrows that switch between pages. We have to set arrow icons to turn to the next page for all but the last, and icons to turn to the previous page for all but the first. We can do this like so: 
```java
PagedMenuBuilder myPagedMenuBuilder = menus.pagedMenuBuilder("My paged menu", 27);
myPagedMenuBuilder.setMenuLength(5); // Number of pages = 5
MenuItem previousPage = new MenuItemBuilder(Material.ARROW, 1, "&ePrevious Page").addAction(ChangePageAction.PREVIOUS_PAGE).build();
myPagedMenuBuilder.setStaticItem(0, previousPage, 0); // last variable is vararg of page numbers to exclude
MenuItem nextPage = new MenuItemBuilder(Material.ARROW, 1, "&eNext Page").addAction(ChangePageAction.NEXT_PAGE).build();
myPagedMenuBuilder.setStaticItem(8, nextPage, 4); // last variable is vararg of page numbers to exclude
```

<small>Currently there isn't a function to do this easily but there will be in a future version.</small> 


### On-close and on-click menu-wide handlers
TODO

### Blocking interactions in menus
By default, all menus do not allow the user to modify their contents by dragging items in the GUI. This default can be changed for the entire menu by running 
```java
myMenuBuilder.setInteractionsBlocked(false);
```
This will allow viewers to move **all** items inside the inventory. You may want to block this function for only some items, and this can be done by overriding the default setting for specific menu items. Each MenuItem defaults to use the same `interactionsBlocked` value as their parent menu. By running the following you can change this for a single item: 
```java
myItemBuilder.setIteractionsBlocked(false);
```

### Menu generators and menu item generators
TODO

### Shop menus
TODO

### Animated menus
TODO

### Custom menu builders
TODO



