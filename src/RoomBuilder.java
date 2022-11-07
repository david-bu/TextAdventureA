public class RoomBuilder implements RoomSwitcher {

    private static final int DEFAULT_ARRAY_SIZE = 20;

    private String name;
    /*private String[] options = new String[DEFAULT_ARRAY_SIZE];
    private String[] roomItemNames = new String[DEFAULT_ARRAY_SIZE];
    private Action[] optionActionList = new Action[DEFAULT_ARRAY_SIZE];*/
    private Option[] options = new Option[DEFAULT_ARRAY_SIZE];
    private int optionIndex = 1;

    private ItemPicker itemPicker;
    private RoomSwitcher roomSwitcher;
    private ActionHandler actionHandler;

    private boolean useGoBackFunction = false;

    public RoomBuilder(ItemPicker picker, RoomSwitcher switcher, ActionHandler handler) {
        itemPicker = picker;
        roomSwitcher = switcher;
        actionHandler = handler;

        Option goBackOption = new Option("Gehe zurück zum vorherigen Raum", "PREV", Action.CHANGE_ROOM);
        options[0] = goBackOption;
    }

    public RoomBuilder setRoomBackOption(boolean includeBackOption) {
        useGoBackFunction = includeBackOption;
        return this;
    }

    public RoomBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RoomBuilder addOption(Option option) {
        if (optionIndex >= options.length)
            resizeOptions();

        options[optionIndex++] = option;
        return this;
    }

    public RoomBuilder addOption(String optionText, String optionData, Action optionAction) {
        if (optionIndex >= options.length)
            resizeOptions();

        return addOption(new Option(optionText, optionData, optionAction));
    }

    public RoomBuilder addOption(String optionText, Room room) {
        return addOption(optionText, room.name, Action.CHANGE_ROOM);
    }

    public RoomBuilder addConditionalOption(String optionText, String optionData,
                                            Action optionAction, OptionCondition condition) {
        Option option = new Option(optionText, optionData, optionAction, condition);
        return addOption(option);
    }

    private void resizeOptions() {
        Option[] newOptions = new Option[options.length * 2];
        for (int i = 0; i < options.length; i++)
            newOptions[i] = options[i];

        options = newOptions;
    }

    public Room build() {
        shrinkOptions();
        if (!useGoBackFunction)
            removeFirstOptionsElement();

        return new Room(
                name,
                options,
                itemPicker,
                this,
                actionHandler
        );
    }

    private void shrinkOptions() {
        Option[] newOptions = new Option[optionIndex];
        for (int i = 0; i < newOptions.length; i++)
            newOptions[i] = options[i];

        options = newOptions;
    }

    private void removeFirstOptionsElement() {
        Option[] newOptions = new Option[optionIndex-1];
        for (int i = 0; i < newOptions.length; i++)
            newOptions[i] = options[i+1];

        options = newOptions;
    }

    @Override
    public Room SwitchRoom(Room from, String to) {
        if (to.equals("PREV"))
            return GetPrevRoom();

        return roomSwitcher.SwitchRoom(from, to);
    }

    @Override
    public Room GetPrevRoom() {
        return roomSwitcher.GetPrevRoom();
    }
}
