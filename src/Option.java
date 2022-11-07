public class Option {

    private String optionText;
    private String optionData;
    private Action optionAction;
    private OptionCondition optionCondition;

    public Option(String optionText, String optionData, Action optionAction, OptionCondition optionCondition) {
        this.optionText = optionText;
        this.optionData = optionData;
        this.optionAction = optionAction;
        this.optionCondition = optionCondition;
    }

    public Option(String optionText, String optionData, Action optionAction) {
        this.optionText = optionText;
        this.optionData = optionData;
        this.optionAction = optionAction;
        this.optionCondition = new OptionCondition() {};
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getOptionData() {
        return optionData;
    }

    public void setOptionData(String optionData) {
        this.optionData = optionData;
    }

    public Action getOptionAction() {
        return optionAction;
    }

    public void setOptionAction(Action optionAction) {
        this.optionAction = optionAction;
    }

    public OptionCondition getOptionCondition() {
        return optionCondition;
    }

}
