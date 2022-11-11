public interface ItemPicker {

    /**
     * called from a room when an item is picked up
     * @param item the name of the item stored in the optionData
     */
    void pickItem(String item);

    /**
     * called from a room to receive all picked up items
     * @return a string array of all picked up items
     */
    String[] getAllItems();

}