public interface OptionCondition {

    /**
     * called from a room to check if an option should be displayed
     * @param items a string array of all picked up items
     * @return true if the condition is fulfilled and the option should be displayed
     */
    default boolean checkCondition(String[] items) {
        return true;
    }

}