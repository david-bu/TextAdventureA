import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Room {

    final String name;

    protected Option[] options;
    protected ItemPicker picker;
    protected RoomSwitcher switcher;
    protected ActionHandler handler;

    /**
     * constructor for a room
     * @param name name of the room
     * @param options all different options which can be displayed in this room
     * @param picker an impl of ItemPicker which handles storing of picked up items
     * @param switcher an impl of RoomSwitcher which manages all different existing rooms
     * @param handler an impl of ActionHandler which handles custom actions via checking the option.optionData value
     */
    public Room(String name, Option[] options, ItemPicker picker, RoomSwitcher switcher, ActionHandler handler) {
        this.name = name;
        this.options = options;
        this.picker = picker;
        this.switcher = switcher;
        this.handler = handler;
    }

    /**
     * prints the room name !isSwitch and outputs all available options
     * if an option is chosen:
     *  if the action of the option is CHANGE_ROOM visitOtherRoom is called
     *  else the action is executed (pick item via ItemPicker or handle custom action via ActionHandler)
     *  and the now available options are printed again
     * @param isSwitch true if no welcome message should be printed
     */
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
            try {
                choice = scanner.nextInt();
                if (choice < 0 || choice >= availableOptions.length) {
                    System.out.println("Die eingegebene Zahl gehört zu keiner Option!");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Die Eingabe wurde nicht als Zahl erkannt!");
                System.out.println("Bitte gib die Nummer der Option ein!");
                continue;
            } catch (NoSuchElementException e) {
                System.out.println("Es wurde keine Eingabe erkannt!");
                System.out.println("Bitte gib die Nummer der Option ein!");
                continue;
            } catch (IllegalStateException e) {
                System.out.println("Die Eingabe ist nicht mehr verfügbar!");
                System.exit(-1);
            }
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

        Room nextRoom = switcher.SwitchRoom(option.getOptionData());
        if (nextRoom == null) {
            System.err.println("Raum mit dem Namen " + option.getOptionData() +
                    " im Raum " + this.name + " nicht gefunden!");
            System.exit(-1);
        }

        visitOtherRoom(nextRoom);
    }

    /**
     * handles prevRoom change, outputs that the room changes and calls visit from the next room
     * @param room the room to visit
     */
    void visitOtherRoom(Room room) {
        switcher.SetPrevRoom(this);
        System.out.println("Du wechselst vom " + name + " in " + room.name);
        room.visit(true);
    }

    /**
     * outputs all picked up items
     */
    private void printAllItems() {
        String[] items = picker.getAllItems();
        System.out.print("Deine aufgesammelten Items sind: ");
        for (int i = 0; i < items.length - 1; i++) {
            System.out.print(items[i] + ", ");
        }
        System.out.println(items[items.length-1]);
    }

    /**
     * checks for every option if the condition is true and returns an array of options where it's true
     * @return array of available options
     */
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
