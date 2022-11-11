/**
 * a class for storing information about each option in a room containing its display text, how to handle choice of the
 * option and when to show the action
 */
public class Option {

    private String optionText;
    private String optionData;
    private Action optionAction;
    private final OptionCondition optionCondition;

    /**
     * constructor for an option object
     * @param optionText the display text
     * @param optionData additional information for handling choice - the name of the room if Action.CHANGE_ROOM
     *                   or the name of the item if Action.PICK_ITEM
     * @param optionAction type of action to perform when chosen
     * @param optionCondition condition for the option to be displayed
     */
    public Option(String optionText, String optionData, Action optionAction, OptionCondition optionCondition) {
        this.optionText = optionText;
        this.optionData = optionData;
        this.optionAction = optionAction;
        this.optionCondition = optionCondition;
    }

    /**
     * constructor for an option object
     * set the condition always to true, option will always be displayed
     * @param optionText the display text
     * @param optionData additional information for handling choice - the name of the room if Action.CHANGE_ROOM
     *      *                   or the name of the item if Action.PICK_ITEM
     * @param optionAction type of action to perform when chosen
     */
    public Option(String optionText, String optionData, Action optionAction) {
        this.optionText = optionText;
        this.optionData = optionData;
        this.optionAction = optionAction;
        this.optionCondition = new OptionCondition() {};
    }

    /**
     * getter for the display text of the option
     * @return displayed text
     */
    public String getOptionText() {
        return optionText;
    }

    /**
     * setter for the display text of the option
     * @param optionText the value for the display text
     */
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    /**
     * getter for additional information for handling choice of the option
     * @return additional information for handling choice of the option
     */
    public String getOptionData() {
        return optionData;
    }

    /**
     * setter for additional information for handling choice of the option
     * @param optionData additional information for handling choice of the option to set
     */
    public void setOptionData(String optionData) {
        this.optionData = optionData;
    }

    /**
     * getter for the type of action
     * @return the action to perform when chosen
     */
    public Action getOptionAction() {
        return optionAction;
    }

    /**
     * setter for the type of action
     * @param optionAction type of the action to set
     */
    public void setOptionAction(Action optionAction) {
        this.optionAction = optionAction;
    }

    /**
     * getter for the condition
     * @return an impl of the OptionCondition interface to check if the option should be showed
     */
    public OptionCondition getOptionCondition() {
        return optionCondition;
    }

}
