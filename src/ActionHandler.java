/**
 * interface for communicating from a room to the manager of the game
 */
public interface ActionHandler {

    /**
     * called from Room to handle actions with the enum value Action.CUSTOM
     * @param action the data string stored in an options element
     */
    void handleAction(String action);

}