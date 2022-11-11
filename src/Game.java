public class Game implements ItemPicker, RoomSwitcher, ActionHandler {

    // Maximum number of different items in the game
    private static final int MAX_ITEMS = 15;

    private int pickedItemCount = 0;
    private final String[] pickedItems = new String[MAX_ITEMS];
    private final Room[] rooms;
    private Room prevRoom = null;

    /**
     * Game constructor
     * creates all different rooms and adds them to the rooms array
     */
    public Game() {
        Room firstRoom = new RoomBuilder(this, this, this)
                .setName("Raum1")
                .setRoomBackOptionIfAvailable(true)
                .setQuitOption(true)
                .addOption("Wechsle zu Raum 2.", "Raum2", Action.CHANGE_ROOM)
                .addOption("Nehme Item 1 auf.", "Item1", Action.PICK_ITEM)
                .addConditionalOption("Gehe in den versteckten Raum", "HiddenRoom",
                        Action.CHANGE_ROOM, new OptionCondition() {
                            @Override
                            public boolean checkCondition(String[] items) {
                                for (String item : items) {
                                    if (item.equals("Schlüssel"))
                                        return true;
                                }
                                return false;
                            }
                        })
                .addOption("Wechsle zum Testraum.", "Testraum", Action.CHANGE_ROOM)
                .build();

        Room secondRoom = new RoomBuilder(this, this, this)
                .setName("Raum2")
                .setQuitOption(true)
                .setRoomBackOptionIfAvailable(true)
                .addOption("Wechsle zu Raum 1.", firstRoom)
                .addItemOptionIfNotPicked("Nehme Schlüssel auf.", "Schlüssel")
                .addOption("Wechsle zum Testraum.", "Testraum", Action.CHANGE_ROOM)
                .build();

        Room testRoom = new RoomBuilder(this, this, this)
                .setName("Testraum")
                .setQuitOption(true)
                .setRoomBackOptionIfAvailable(true)
                .addOption("Go to Room1", firstRoom)
                .build();

        Room hiddenRoom = new RoomBuilder(this, this, this)
                .setName("HiddenRoom")
                .setQuitOption(true)
                .setRoomBackOptionIfAvailable(true)
                .addOption("Du bist im versteckten Raum", "HIDDEN_ROOM", Action.CUSTOM)
                .build();

        rooms = new Room[] {
                firstRoom,
                secondRoom,
                testRoom,
                hiddenRoom
        };
    }

    /**
     * starting the game by visiting the first room in the rooms array
     */
    public void start() {
        Room room = rooms[0];
        room.visit(false);
    }

    /**
     * implementation from ItemPicker
     * called from a room when an item is picked up by the user
     * @param item the name of the item stored in the optionData of the Options object
     */
    @Override
    public void pickItem(String item) {
        if (pickedItemCount >= MAX_ITEMS) {
            System.err.println("Max picked objects reached! Increase MAX_OBJECT in your Game impl!");
            return;
        }

        if(isItemInList(item))
            return;

        pickedItems[pickedItemCount++] = item;
    }


    /**
     * checks if an item is already picked up by the user
     * @param item the item to check
     * @return true if the item is already picked up
     */
    private boolean isItemInList(String item) {
        for (int i = 0; i < pickedItemCount; i++) {
            if (pickedItems[i].equals(item))
                return true;
        }

        return false;
    }

    /**
     * implementation of the item picker for returning all picked up items
     * @return an array of all picked up items
     */
    @Override
    public String[] getAllItems() {
        return pickedItemsCopy();
    }

    /**
     * copies all items from pickedItems into a new array to remove all null values
     * @return a string array containing all picked up items without null values
     */
    private String[] pickedItemsCopy() {
        String[] itemList = new String[pickedItemCount];
        for (int i = 0; i < pickedItemCount; i++) {
            itemList[i] = pickedItems[i];
        }
        return itemList;
    }

    /**
     * implementation from RoomSwitcher to get the room object from its name
     * @param to the name of the room switching to
     * @return the room for the name
     */
    @Override
    public Room SwitchRoom(String to) {
        for (Room room : rooms) {
            if (room.name.equals(to)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public void SetPrevRoom(Room prev) {
        prevRoom = prev;
    }

    /**
     * returns the previously visited room
     * @return the previous room
     */
    @Override
    public Room GetPrevRoom() {
        return prevRoom;
    }

    /**
     * implementation for the ActionHandler to handle custom actions
     * @param action the data string stored in an options element
     */
    @Override
    public void handleAction(String action) {
        if (action.equals("HIDDEN_ROOM")) {
            System.out.println("Du hast den Schlüssel in Raum2 gefunden!");
        } else if (action.equals("QUIT")) {
            System.exit(0);
        }
    }
}
