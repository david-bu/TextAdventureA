import java.util.Scanner;

class Room {

    final String name;

    protected Option[] options;
    protected ItemPicker picker;
    protected RoomSwitcher switcher;
    protected ActionHandler handler;

    public Room(String name, Option[] options, ItemPicker picker, RoomSwitcher switcher, ActionHandler handler) {
        this.name = name;
        this.options = options;
        this.picker = picker;
        this.switcher = switcher;
        this.handler = handler;
    }

    void visit(boolean isSwitch) {
        if (!isSwitch)
            System.out.println("Du bist in Raum " + name);

        int choice = 0;
        Option option;
        while (true) {
            Option[] availableOptions = getAvailableOptions();
            System.out.println("Wähle eine Option:");
            for (int i = 0; i < availableOptions.length; i++)
                System.out.println("  " + i + ": " + availableOptions[i].getOptionText());

            Scanner scanner = new Scanner(System.in);
            choice = scanner.nextInt();
            option = availableOptions[choice];

            if (option.getOptionAction() != Action.CHANGE_ROOM)
            {
                if (option.getOptionAction() == Action.PICK_ITEM) {
                    picker.pickItem(option.getOptionData());
                    System.out.println("Du hast das Item " + option.getOptionData() + " aufgenommen.");
                    printAllItems();
                    System.out.println("Du bleibst in Raum " + name);
                } else {
                    handler.handleAction(option.getOptionData());
                }
            }
            else break;
        }

        Room nextRoom = switcher.SwitchRoom(this, option.getOptionData());
        visitOtherRoom(nextRoom);
    }

    void visitOtherRoom(Room room) {
        System.out.println("Du wechselst vom " + name + " in " + room.name);
        room.visit(true);
    }

    private void printAllItems() {
        String[] items = picker.getAllItems();
        System.out.print("Deine aufgesammelten Items sind: ");
        for (int i = 0; i < items.length - 1; i++) {
            System.out.print(items[i] + ", ");
        }
        System.out.println(items[items.length-1]);
    }

    private Option[] getAvailableOptions() {
        int size = 0;
        Option[] availableOptions = new Option[options.length];

        for (int i = 0; i < availableOptions.length; i++) {
            if (options[i].getOptionCondition().checkCondition(picker.getAllItems()))
                availableOptions[size++] = options[i];
        }

        Option[] availableOptionsTrunc = new Option[size];
        for (int i = 0; i < availableOptionsTrunc.length; i++)
            availableOptionsTrunc[i] = availableOptions[i];

        return availableOptionsTrunc;
    }
}
