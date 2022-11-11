public interface RoomSwitcher {

    /**
     * called from a room to get the next room from its name from the manager of all rooms
     * @param to the name of the next room
     * @return the room with the name of the to string
     */
    Room SwitchRoom(String to);

    /**
     * called if prev room changed
     * @param prev room previously visited
     */
    void SetPrevRoom(Room prev);

    /**
     * called from a room (or OptionCondition) to get the last room the user was in
     * @return previous room
     */
    Room GetPrevRoom();

}
