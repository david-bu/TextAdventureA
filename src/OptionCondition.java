public interface OptionCondition {

    default boolean checkCondition(String[] items) {
        return true;
    }

}