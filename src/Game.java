public class Game implements ItemPicker, RoomSwitcher, ActionHandler {

    private static final int MAX_OBJECTS = 15;

    private int pickedObjectCount = 0;
    private final String[] pickedObjects = new String[MAX_OBJECTS];
    private static Room[] rooms;
    private static Room prevRoom = null;

    public Game() {
        Room firstRoom = new RoomBuilder(this, this, this)
                .setName("Raum1")
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
                .addOption("Verlasse das Spiel.", "QUIT", Action.CUSTOM)
                .build();

        Room secondRoom = new RoomBuilder(this, this, this)
                .setName("Raum2")
                .addOption("Wechsle zu Raum 1.", firstRoom)
                .addOption("Nehme Schlüssel auf.", "Schlüssel", Action.PICK_ITEM)
                .addOption("Wechsle zum Testraum.", "Testraum", Action.CHANGE_ROOM)
                .build();

        Room testRoom = new RoomBuilder(this, this, this)
                .setName("Testraum")
                .setRoomBackOption(true)
                .addOption("First normal option to Room1", firstRoom)
                .build();

        Room hiddenRoom = new RoomBuilder(this, this, this)
                .setName("HiddenRoom")
                .setRoomBackOption(true)
                .addOption("Du bist im versteckten Raum", "HIDDEN_ROOM", Action.CUSTOM)
                .build();

        rooms = new Room[] {
                firstRoom,
                secondRoom,
                testRoom,
                hiddenRoom
        };
    }

    public void start() {
        Room room = rooms[0];
        room.visit(false);
    }

    @Override
    public void pickItem(String object) {
        if (pickedObjectCount >= MAX_OBJECTS || isObjectInList(object))
            return;

        pickedObjects[pickedObjectCount++] = object;
    }


    private boolean isObjectInList(String object) {
        for (int i = 0; i < pickedObjectCount; i++) {
            if (pickedObjects[i].equals(object))
                return true;
        }

        return false;
    }

    @Override
    public String[] getAllItems() {
        return pickedItemsCopy();
    }

    private String[] pickedItemsCopy() {
        String[] itemList = new String[pickedObjectCount];
        for (int i = 0; i < pickedObjectCount; i++) {
            itemList[i] = pickedObjects[i];
        }
        return itemList;
    }

    @Override
    public Room SwitchRoom(Room from, String to) {
        for (Room room : rooms) {
            if (room.name.equals(to)) {
                prevRoom = from;
                return room;
            }
        }
        return null;
    }

    @Override
    public Room GetPrevRoom() {
        return prevRoom;
    }

    @Override
    public void handleAction(String action) {
        if (action.equals("HIDDEN_ROOM")) {
            System.out.println("Du hast den Schlüssel in Raum2 gefunden!");
        } else if (action.equals("QUIT")) {
            System.exit(0);
        }
    }
}
