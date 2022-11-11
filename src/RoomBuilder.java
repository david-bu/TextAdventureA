public class RoomBuilder implements RoomSwitcher, ActionHandler {

    private static final int DEFAULT_ARRAY_SIZE = 20;

    private String name;
    private Option[] options = new Option[DEFAULT_ARRAY_SIZE];
    private int optionIndex = 1;

    private final ItemPicker itemPicker;
    private final RoomSwitcher roomSwitcher;
    private final ActionHandler actionHandler;

    private boolean useGoBackFunction = false;
    private boolean addQuitOption = false;

    /**
     * constructor of a RoomBuilder
     * helper class for creating a room
     * @param picker ItemPicker which should be used for the room to create
     * @param switcher RoomSwitcher which should be used for the room to create
     * @param handler ActionHandler which should be used for the room to create
     */
    public RoomBuilder(ItemPicker picker, RoomSwitcher switcher, ActionHandler handler) {
        itemPicker = picker;
        roomSwitcher = switcher;
        actionHandler = handler;

        Option goBackOption = new Option("Gehe zurück zum vorherigen Raum.", "PREV",
                Action.CHANGE_ROOM, new OptionCondition() {
            @Override
            public boolean checkCondition(String[] items) {
                return roomSwitcher.GetPrevRoom() != null;
            }
        });
        options[0] = goBackOption;
    }

    /**
     * sets the room back option if available (prev room not null)
     * always the first option (if active)
     * @param includeBackOption true if this option should be automatically included
     * @return this RoomBuilder
     */
    public RoomBuilder setRoomBackOptionIfAvailable(boolean includeBackOption) {
        useGoBackFunction = includeBackOption;
        return this;
    }

    /**
     * sets the quit option
     * always the last option (if active)
     * @param addQuitOption true if this option should be automatically included
     * @return this RoomBuilder
     */
    public RoomBuilder setQuitOption(boolean addQuitOption) {
        this.addQuitOption = addQuitOption;
        return this;
    }

    /**
     * sets the room name which should be used
     * @param name name of the room
     * @return this RoomBuilder
     */
    public RoomBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * adds an option at the end of the current options list
     * if action is Action.CHANGE_ROOM and the option data is "PREV" prev room function will be used
     * if action is Action.CUSTOM and the option data is "QUIT" quit function will be used
     * @param option the option which should be added
     * @return this RoomBuilder
     */
    public RoomBuilder addOption(Option option) {
        if (optionIndex >= options.length)
            resizeOptions();

        options[optionIndex++] = option;
        return this;
    }

    /**
     * creates an option object with given parameters
     * adds an option at the end of the current options list
     * if optionAction is Action.CHANGE_ROOM and the optionData is "PREV" prev room function will be used
     * if optionAction is Action.CUSTOM and the optionData is "QUIT" quit function will be used
     * @param optionText display text for this option
     * @param optionData additional data for this option
     * @param optionAction action type for this option
     * @return this RoomBuilder
     */
    public RoomBuilder addOption(String optionText, String optionData, Action optionAction) {
        return addOption(new Option(optionText, optionData, optionAction));
    }

    /**
     * creates an option object with the display text and an action for changing the room to the given room
     * adds an option at the end of the current options list
     * if the room name is "PREV" prev room function will be used
     * @param optionText display text for this option
     * @param room room object to which should be switched
     * @return this RoomBuilder
     */
    public RoomBuilder addOption(String optionText, Room room) {
        return addOption(optionText, room.name, Action.CHANGE_ROOM);
    }

    /**
     * creates an option object with given parameters
     * adds an option with a condition at the end of the current options list
     * if optionAction is Action.CHANGE_ROOM and the optionData is "PREV" prev room function will be used
     * if optionAction is Action.CUSTOM and the optionData is "QUIT" quit function will be used
     * @param optionText display text for this option
     * @param optionData additional data for this option
     * @param optionAction action type for this option
     * @param condition interface for proving the condition
     * @return this RoomBuilder
     */
    public RoomBuilder addConditionalOption(String optionText, String optionData,
                                            Action optionAction, OptionCondition condition) {
        Option option = new Option(optionText, optionData, optionAction, condition);
        return addOption(option);
    }

    public RoomBuilder addItemOptionIfNotPicked(String optionText, String itemName) {
        return addConditionalOption(optionText, itemName, Action.PICK_ITEM, new OptionCondition() {
            @Override
            public boolean checkCondition(String[] items) {
                for (String item : items) {
                    if (itemName.equals(item))
                        return false;
                }
                return true;
            }
        });
    }

    /**
     * doubles the size of the current options array and copies all entries to the new array to store more options
     */
    private void resizeOptions() {
        Option[] newOptions = new Option[options.length * 2];
        for (int i = 0; i < options.length; i++)
            newOptions[i] = options[i];

        options = newOptions;
    }

    /**
     * creates a room with all given options to this RoomBuilder
     * @return the created room
     */
    public Room build() {
        if (addQuitOption) {
            Option quitOption = new Option("Verlasse das Spiel.", "QUIT", Action.CUSTOM);
            addOption(quitOption);
        }

        shrinkOptions();
        if (!useGoBackFunction)
            removeFirstOptionsElement();

        return new Room(
                name,
                options,
                itemPicker,
                this,
                this
        );
    }

    /**
     * removes all null values in the options array
     */
    private void shrinkOptions() {
        Option[] newOptions = new Option[optionIndex];
        for (int i = 0; i < newOptions.length; i++)
            newOptions[i] = options[i];

        options = newOptions;
    }

    /**
     * removes the first array element where the room back option is always stored
     */
    private void removeFirstOptionsElement() {
        Option[] newOptions = new Option[optionIndex-1];
        for (int i = 0; i < newOptions.length; i++)
            newOptions[i] = options[i+1];

        options = newOptions;
    }

    /**
     * RoomSwitcher impl for checking first for keyword "PREV" and else calling passed RoomSwitcher
     * @param to the name of the next room
     * @return the next room
     */
    @Override
    public Room SwitchRoom(String to) {
        if (to.equals("PREV"))
            return GetPrevRoom();

        return roomSwitcher.SwitchRoom(to);
    }

    /**
     * just calling passed RoomSwitcher
     * @param prev room previously visited
     */
    @Override
    public void SetPrevRoom(Room prev) {
        roomSwitcher.SetPrevRoom(prev);
    }

    /**
     * just calling passed RoomSwitcher
     * @return the previous room
     */
    @Override
    public Room GetPrevRoom() {
        return roomSwitcher.GetPrevRoom();
    }

    /**
     * ActionHandler impl for handling the quit action first or if not quit calling the passed ActionHandler
     * @param action the data string stored in an options element
     */
    @Override
    public void handleAction(String action) {
        if (addQuitOption && action.equals("QUIT"))
            System.exit(0);
        actionHandler.handleAction(action);
    }

}
